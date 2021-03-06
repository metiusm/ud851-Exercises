package com.example.android.waitlist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.android.waitlist.data.TestUtil;
import com.example.android.waitlist.data.WaitlistContract;
import com.example.android.waitlist.data.WaitlistDbHelper;

import static com.example.android.waitlist.data.WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME;
import static com.example.android.waitlist.data.WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE;
import static com.example.android.waitlist.data.WaitlistContract.WaitlistEntry.TABLE_NAME;


public class MainActivity extends AppCompatActivity {

    private GuestListAdapter mAdapter;
    private SQLiteDatabase mDb;

    // COMPLETED (1) Create local EditText members for mNewGuestNameEditText and mNewPartySizeEditText
    private EditText mNewGuestNameEditText;
    private EditText mNewPartySizeEditText;

    // COMPLETED (13) Create a constant string LOG_TAG that is equal to the class.getSimpleName()
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView waitlistRecyclerView;

        // Set local attributes to corresponding views
        waitlistRecyclerView = (RecyclerView) this.findViewById(R.id.all_guests_list_view);

        // COMPLETED (2) Set the Edit texts to the corresponding views using findViewById
        mNewGuestNameEditText = (EditText) findViewById(R.id.person_name_edit_text);
        mNewPartySizeEditText = (EditText) findViewById(R.id.party_count_edit_text);

        // Set layout for the RecyclerView, because it's a list we are using the linear layout
        waitlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Create a DB helper (this will create the DB if run for the first time)
        WaitlistDbHelper dbHelper = new WaitlistDbHelper(this);

        // Keep a reference to the mDb until paused or killed. Get a writable database
        // because you will be adding restaurant customers
        mDb = dbHelper.getWritableDatabase();

        // COMPLETED (3) Remove this fake data call since we will be inserting our own data now
        //TestUtil.insertFakeData(mDb);

        // Get all guest info from the database and save in a cursor
        Cursor cursor = getAllGuests();

        // Create an adapter for that cursor to display the data
        mAdapter = new GuestListAdapter(this, cursor);

        // Link the adapter to the RecyclerView
        waitlistRecyclerView.setAdapter(mAdapter);

    }

    /**
     * This method is called when user clicks on the Add to waitlist button
     *
     * @param view The calling view (button)
     */
    public void addToWaitlist(View view) {

        // COMPLETED (9) First thing, check if any of the EditTexts are empty, return if so
        Boolean emptyText = false;
        if(mNewGuestNameEditText.getText().toString().trim() == "") emptyText = true;
        if(mNewPartySizeEditText.getText().toString().trim() == "") emptyText = true;
        if(emptyText) return;
        // COMPLETED (10) Create an integer to store the party size and initialize to 1
        Integer partySize = 1;
        // COMPLETED (11) Use Integer.parseInt to parse mNewPartySizeEditText.getText to an integer
        try {
            partySize = Integer.parseInt(mNewPartySizeEditText.getText().toString());
            // COMPLETED (12) Make sure you surround the Integer.parseInt with a try catch and log any exception
        }
        catch(NumberFormatException nfe) {
            Log.e("NumberFormatException","Failed to parse party size text to number: " + nfe.getMessage());
        }
        // COMPLETED (14) call addNewGuest with the guest name and party size
        addGuest(mNewGuestNameEditText.getText().toString().trim(), partySize);
        // COMPLETED (19) call mAdapter.swapCursor to update the cursor by passing in getAllGuests()
        mAdapter.swapCursor(getAllGuests());
        // COMPLETED (20) To make the UI look nice, call .getText().clear() on both EditTexts, also call clearFocus() on mNewPartySizeEditText
        mNewGuestNameEditText.getText().clear();
        mNewPartySizeEditText.getText().clear();
    }



    /**
     * Query the mDb and get all guests from the waitlist table
     *
     * @return Cursor containing the list of guests
     */
    private Cursor getAllGuests() {
        return mDb.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WaitlistContract.WaitlistEntry.COLUMN_TIMESTAMP
        );
    }

    // COMPLETED (4) Create a new addGuest method

    // COMPLETED (5) Inside, create a ContentValues instance to pass the values onto the insert query

    // COMPLETED (6) call put to insert the name value with the key COLUMN_GUEST_NAME

    // COMPLETED (7) call put to insert the party size value with the key COLUMN_PARTY_SIZE

    // COMPLETED (8) call insert to run an insert query on TABLE_NAME with the ContentValues created
    private void addGuest(String name, int partySize)
    {
        ContentValues values = new ContentValues();

        values.put(COLUMN_GUEST_NAME, name);

        values.put(COLUMN_PARTY_SIZE, partySize);

        mDb.insert(TABLE_NAME, null, values);
    }




}
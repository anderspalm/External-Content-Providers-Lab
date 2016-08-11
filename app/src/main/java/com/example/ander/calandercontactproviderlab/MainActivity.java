package com.example.ander.calandercontactproviderlab;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

//    Display title and date for all calendar events in a ListView
//    Display events in descending order by date (reverse chronological order)
//    Allow the user to delete calendar events

    private static final String TAG = "MainActivity";
    private final static int CALANDAR_READER = 0;
    CursorAdapter mCursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        ListView listView = (ListView) findViewById(R.id.list_view);


        mCursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.list_view_items,
                null,
                new String[]{
                        CalendarContract.Events.TITLE,
                        CalendarContract.Events.DTSTART
                },
                new int[]{
                        R.id.title,
                        R.id.date
                },
                0
        );

        getSupportLoaderManager().initLoader(CALANDAR_READER, null, this);

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                EditText editText = (EditText) findViewById(R.id.edit_text);
//                String query = editText.getText().toString();

//                ContentResolver contentResolver = getContentResolver();
//                ContentValues values = new ContentValues();
//                values.put(CalendarContract.Events.DTSTART, 145566);
//                values.put(CalendarContract.Events.TITLE, query);
//                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
//                        return;}
//                contentResolver.insert(CalendarContract.Events.CONTENT_URI, values);
//            }
//        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, l);
                getContentResolver().delete(uri,null,null);
                Log.i(TAG, "onItemClick: " + uri);
                return false;
            }
        });

        listView.setAdapter(mCursorAdapter);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case CALANDAR_READER:
                return new CursorLoader(
                        MainActivity.this,
                        CalendarContract.Events.CONTENT_URI,
                        new String[]{
                                CalendarContract.Events._ID,
                                CalendarContract.Events.TITLE,
                                CalendarContract.Events.DTSTART
                        },
                        null,
                        null,
                        CalendarContract.Events.DTSTART + " DESC"
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.changeCursor(null);
    }
}

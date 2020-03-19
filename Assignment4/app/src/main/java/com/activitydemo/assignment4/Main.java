/**
 * <h1> CS6323 Assignment 4 </h1>
 * This work realized a program
 * display and record scores
 *
 * This is an Android APP
 * with internal storage
 *
 * This program contains 6 java files
 *
 * @author  Dayuan Chen
 * @since   03-15-2020
 */
package com.activitydemo.assignment4;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

/**
 *  This is the start activity
 *  Function: Display the score board list
 */
public class Main extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // onCreate function runs when open this app
        super.onCreate(savedInstanceState);
        // 1. set activity
        setContentView(R.layout.activity_main);
        // 2. initialize the score board list
        ListView mListView = findViewById(R.id.listView);
        ArrayList<Person> peopleList = new ArrayList<>();
        //    including load data
        fileIO fileIO = new fileIO();
        fileIO.loadList(this, peopleList);
        PersonListAdapter adapter = new PersonListAdapter(this, R.layout.adapter_view_layout, peopleList);
        mListView.setAdapter(adapter);
    }

    @Override
    protected void onRestart() {
        // onRestart runs when we switch back to this activity from others
        super.onRestart();
        // 1. initialize the score board list
        ListView mListView = findViewById(R.id.listView);
        ArrayList<Person> peopleList = new ArrayList<>();
        //    including load data
        fileIO fileIO = new fileIO();
        fileIO.loadList(this, peopleList);
        PersonListAdapter adapter = new PersonListAdapter(this, R.layout.adapter_view_layout, peopleList);
        mListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu: add buttons to action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // this function determine whether item in Action bar is being selected
        // and set anim between activity
        switch (item.getItemId()) {
            case R.id.addScore:
                startActivity(new Intent(this, AddItem.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            case R.id.about:
                startActivity(new Intent(this, About.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

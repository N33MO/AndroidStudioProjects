package com.reactiongame.assignment5;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

// This class reference previous homework to display score list
public class ScoreBoard extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        ListView mListView = findViewById(R.id.listView);
        ArrayList<Person> peopleList = new ArrayList<>();
        fileIO fileIO = new fileIO();
        fileIO.loadList(this, peopleList);
        PersonListAdapter adapter = new PersonListAdapter(this, R.layout.adapter_view_layout, peopleList);
        mListView.setAdapter(adapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        ListView mListView = findViewById(R.id.listView);
        ArrayList<Person> peopleList = new ArrayList<>();
        fileIO fileIO = new fileIO();
        fileIO.loadList(this, peopleList);
        PersonListAdapter adapter = new PersonListAdapter(this, R.layout.adapter_view_layout, peopleList);
        mListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu: add buttons to action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_addscore, menu);
        return true;
    }
}

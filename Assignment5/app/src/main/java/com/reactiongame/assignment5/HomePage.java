/**
 * <h1> CS 6326 Assignment 5</h1>
 * This project realize a mini game
 * to test reaction time by clicking on
 * target color shapes
 *
 * @author Dayuan Chen
 * @since 2020-03-30
 */
package com.reactiongame.assignment5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

// This is the first activity when launching this program
public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    // Button 1 - start the game
    public void startGame(View view) {
        Intent intent = new Intent(HomePage.this, GamePage.class);
        startActivity(intent);
    }

    // Button 2 - see the score board
    public void scoreBoard(View view) {
        Intent intent = new Intent(HomePage.this, ScoreBoard.class);
        startActivity(intent);
    }
}

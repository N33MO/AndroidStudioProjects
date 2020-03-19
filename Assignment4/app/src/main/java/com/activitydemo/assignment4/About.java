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

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 *  This is the About activity
 *  Function: Display some info for this application
 */
public class About extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // set anim for back button in action bar
        finish();
        return true;
    }

    @Override
    public void finish() {
        // set anim for backing to the main page
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}

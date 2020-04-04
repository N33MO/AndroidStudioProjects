/**
 *
 * This class reference previous homework
 * to realize PersonList adapter to display
 * on score board
 *
 *
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
package com.reactiongame.assignment5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 *  This file is for Customize ListView Adapter. Not an Activity
 *  Function: inflate data into customized ListView using adapter
 */
public class PersonListAdapter extends ArrayAdapter<Person> {

    private Context mContext;
    private int mResource;

    public PersonListAdapter(Context context, int resource, ArrayList<Person> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Person person = getItem(position);
        String name = person.getName();
        String score = person.getScore();
        String date = person.getDate();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName = convertView.findViewById(R.id.list_name);
        TextView tvScore = convertView.findViewById(R.id.list_score);
        TextView tvDate = convertView.findViewById(R.id.list_date);

        tvName.setText(name);
        tvScore.setText(score);
        tvDate.setText(date);

        return convertView;
    }
}

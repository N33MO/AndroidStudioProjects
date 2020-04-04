/**
 *
 * This class reference previous homework
 * to realize fileIO function
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 *  This file is for FILE IO. Not an Activity
 *  Function: handling file IO
 */
public class fileIO {

    private static final String FILE_NAME = "score_list.txt";

    public void loadList(Context context, ArrayList<Person> arrayList) {
        // load the list from file
        PriorityQueue<Person> personQue = new PriorityQueue<>();
        FileInputStream fis = null;
        BufferedReader br = null;
        try {
            fis = context.openFileInput(FILE_NAME);
            br = new BufferedReader(new InputStreamReader(fis));

            String line;
            line = br.readLine();
            while (line != null) {
                String[] tmp = line.split("\t", -1);
                personQue.add(new Person(tmp[0],tmp[1],tmp[2]));
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        arrayList.add(new Person("Name", "Score", "Date"));
        Person personTmp;
        int iter = Math.min(12, personQue.size());
        for (int i = 0; i < iter; i++) {
            personTmp = personQue.poll();
            arrayList.add(personTmp);
        }
    }

    public void saveNew(Context context, String text) {
        // save new score to the file
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(FILE_NAME, Context.MODE_APPEND);
            fos.write(text.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Toast.makeText(context, "save: " + text, Toast.LENGTH_LONG).show();
    }

    public void func1(Context context, Person person) {
        // 1. Read file and put them into a list with new one
        PriorityQueue<Person> people = new PriorityQueue<>();
        FileInputStream fis = null;
        BufferedReader br = null;
        try {
            fis = context.openFileInput(FILE_NAME);
            br = new BufferedReader(new InputStreamReader(fis));

            String line;
            String[] tmp;
            line = br.readLine();
            while (line != null) {
                tmp = line.split("\t",-1);
                people.add(new Person(tmp[0],tmp[1],tmp[2]));
                line = br.readLine();
            }
            people.add(person);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. Save top 12 item;
        String tmpWrite;
        int len = Math.min(12, people.size());
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            while (len-- > 0) {
                tmpWrite = people.peek().getName() + "\t" + people.peek().getScore() + "\t" + people.peek().getDate() + "\n";
                fos.write(tmpWrite.getBytes());
                people.poll();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

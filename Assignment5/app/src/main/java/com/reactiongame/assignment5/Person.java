/**
 *
 * This class reference previous homework
 * to realize Person structure
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

/**
 *  This file is for class Person definition. Not an Activity
 *  Function: create a class to structure the data
 */
public class Person implements Comparable<Person> {
    private String name;
    private String score;
    private  String date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Person(String name, String score, String date) {
        this.name = name;
        this.score = score;
        this.date = date;
    }

    @Override
    public int compareTo(Person person) {
        // redefine comparable for PriorityQueue use
        // use PriorityQueue to easy sort out highest 12 scores
        if (Integer.parseInt(this.getScore()) < Integer.parseInt(person.getScore())) {
            return  1;
        }
        else if (Integer.parseInt(this.getScore()) > Integer.parseInt(person.getScore())) {
            return -1;
        }
        else {
            int date_cmp = this.getDate().compareTo(person.getDate());
            if (date_cmp < 0) {
                return 1;
            }
            else if (date_cmp > 0) {
                return -1;
            }
            return 0;
        }
    }
}

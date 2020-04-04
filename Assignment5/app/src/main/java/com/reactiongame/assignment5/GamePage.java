package com.reactiongame.assignment5;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

// this class draw everything for the little game
public class GamePage extends AppCompatActivity {
    // set for timer use
    private Chronometer chronometer;
    private long totalTime;
    // set as a flag for clock ticking when instruction string disappear
    private boolean clock_run;
    // set for drawing field
    private int draw_x_min, draw_x_max, draw_y_min, draw_y_max;
    private int test_x, test_y;
    // set for drawing border and background
    private TextView box;
    // set as a scalar to use dp in setX() and setY() in TextView
    private float dp_resize;
    // set a list to store existing points
    private ArrayList<PointCoord> points = new ArrayList<>();
    // set for total score;
    private int score;
    // set to count correct counts
    private int correct_touch;
    // set to count miss touch
    private int miss_touch;
    // set to count wrong touch
    private int wrong_touch;
    // set punishment for touch miss
    private int punish_time;
//    // set as score for each shape
//    // put these declaration into drawShape function to avoid variable conflicts when setting time
//    private long shape_time_start;
//    private long shape_time_end;
//    private long shape_time_elapse;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        // clear score
        score = 0;
        // clear counts
        correct_touch = 0;
        // clear punishment
        punish_time = 0;
        // timer setup
        chronometer = findViewById(R.id.chronometer);
        // starter instruction string setup
        final TextView starterInstruction = findViewById(R.id.textView_starterInstruction);
        String text = "Touch the\nred\ncircle";
        SpannableString ss = new SpannableString(text);
        ForegroundColorSpan fcs = new ForegroundColorSpan(Color.RED);
        ss.setSpan(fcs,10,13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        starterInstruction.setText(ss);
        // setup layout params
        dp_resize = this.getResources().getDisplayMetrics().density;
        // setup game field border
        drawBox();
        // starter instruction string fade out and game begin
        Timer timer = new Timer(false);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        // close the starterInstruction string textview
                        starterInstruction.setVisibility(View.INVISIBLE);
                        // change box color to dark
                        box_update();
                        // start game
                        gameStart();
                    }
                });
            }
        }, 3000);
    }

    public void startTimer() {
        if (!clock_run) {
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            clock_run = true;
        }
    }

    public void pauseTime() {
        if (clock_run) {
            totalTime = SystemClock.elapsedRealtime() - chronometer.getBase();
            chronometer.stop();
            clock_run = false;
        }
    }

    public void gameStart() {
        // start timer of the game
        startTimer();
        // define how many shapes to draw at first
        int num_shapes_init;
        Random random = new Random();
        num_shapes_init = 6 + random.nextInt(7);
        // setup layout params
        ConstraintLayout gameLayout = findViewById(R.id.gameScene);
        // draw shapes in layout
        for (int i = 0; i < num_shapes_init; i++) {
            drawShape(gameLayout);
        }

    }

    public void drawBox() {
        // get screen size info
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screen_height = displayMetrics.heightPixels;
        int screen_width = displayMetrics.widthPixels;
        /* so the displayed field should be (0 , screen_width) and (1/3 * screen_height , screen_height) */
        ConstraintLayout gameLayout = findViewById(R.id.gameScene);
        box = new TextView(getApplicationContext());
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
//        params.topMargin = screen_height/3;
//        Log.d("mytag", String.valueOf(screen_height));
        box.setLayoutParams(params);
        box.setWidth(screen_width);
        box.setHeight(2*screen_height/3);
        int offset = 90; // offset for the bottom control bar
        box.setY((float)(screen_height/3 - offset));
        box.setBackgroundResource(R.drawable.box);
        gameLayout.addView(box);
        /* so the actual drawing area for shapes is
        * X : 0 to screen_width
        * Y : screen_height/3 - 90 to screen_height - 90
        * */

        /*
        *  for a better view we keep distance
        *  not drawing about the edges for 50
        *
        *  consider the bottom toolbar, we also
        *  minus 90 for y axis
        *
        *  Since shape drawing at position (x,y) as
        *  the upper left corner position
        *  we should minus 64 as the max shape size
        *  here we should use dp_resize to calculate
        *  actually offset
        * */
        int border_spacing = 50;
        int bottom_toolbar_spacing = 90;
        int shape_size_limit = (int)(64 * dp_resize);
        draw_x_min =                   border_spacing;
        draw_x_max = screen_width    - border_spacing                          - shape_size_limit;
        draw_y_min = screen_height/3 + border_spacing - bottom_toolbar_spacing;
        draw_y_max = screen_height   - border_spacing - bottom_toolbar_spacing - shape_size_limit;
        test_x = screen_width;
        test_y = screen_height;
    }

    public void box_update() {
        GradientDrawable gradientDrawable = (GradientDrawable)box.getBackground().getCurrent();
        gradientDrawable.setColor(getResources().getColor(R.color.backgroundColor_dark));
    }

    public void drawShape(final ConstraintLayout layout) {
        // if get enough correct touch, game end
        if (correct_touch >= 10) {
            pauseTime();
            return;
        }
        // calculate score for each shape
        final long shape_time_start = SystemClock.elapsedRealtime() - chronometer.getBase();
        // random parameters we need to use
        final int randomSize, randomColor, randomShape, randomTime;
        // setup random class
        final Random random = new Random();
        // setup shape as TextView
        final TextView shape = new TextView(getApplicationContext());
        // random shape size
        randomSize = 32 + random.nextInt(32);
        // setup shape params
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(randomSize*(int)dp_resize,randomSize*(int)dp_resize);
        shape.setLayoutParams(params);
        // set to empty TextView
        shape.setText("");
        // random shape type
        randomShape = random.nextInt(2);
        switch (randomShape) {
            case 0:
                shape.setBackgroundResource(R.drawable.shape_circle);
                break;
            case 1:
                shape.setBackgroundResource(R.drawable.shape_square);
                break;
            default:
                break;
        }
        // random shape color
        GradientDrawable gradientDrawable = (GradientDrawable)shape.getBackground().getCurrent();
        randomColor = random.nextInt(7);
        switch (randomColor) {
            case 0:
                gradientDrawable.setColor(getResources().getColor(R.color.shapeColor_Red));
                break;
            case 1:
                gradientDrawable.setColor(getResources().getColor(R.color.shapeColor_Blue));
                break;
            case 2:
                gradientDrawable.setColor(getResources().getColor(R.color.shapeColor_Green));
                break;
            case 3:
                gradientDrawable.setColor(getResources().getColor(R.color.shapeColor_Orange));
                break;
            case 4:
                gradientDrawable.setColor(getResources().getColor(R.color.shapeColor_Purple));
                break;
            case 5:
                gradientDrawable.setColor(getResources().getColor(R.color.shapeColor_White));
                break;
            case 6:
                gradientDrawable.setColor(getResources().getColor(R.color.shapeColor_Yellow));
                break;
            default:
                break;
        }
        // random shape position
        final PointCoord point;
        point = randomPosition();
        final int pos_x = draw_x_min + random.nextInt(draw_x_max-draw_x_min);
        int pos_y = draw_y_min + random.nextInt(draw_y_max-draw_y_min);
        shape.setX(point.getX());
        shape.setY(point.getY());
        // random shape lifetime
        randomTime = 3000 + random.nextInt(4000);
        final Timer timer = new Timer(false);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        // when life ends remove this shape
                        layout.removeView(shape);
                        // remove the corresponding point in ArrayList
                        points.remove(point);
                        // if this shape we should touch, add time to total time
                        if (randomShape == 0 && randomColor == 0) {
                            punish_time += randomTime;
                            miss_touch++;
                        }
                        // create another shape only when there are less than 12 shapes
                        int remain_space = 12;
                        if (points != null) {
                            remain_space = 12 - points.size();
                        }
                        if (remain_space > 0) {
                            drawShape(layout);
                        }
                    }
                });
            }
        }, randomTime);
        // set onClickListener
        shape.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
                // cancel the timer schedule
                timer.cancel();
                // after click remove this shape
                layout.removeView(shape);
                // remove the corresponding point in ArrayList
                points.remove(point);
                // decide if need to add score - correct touch
                if (randomShape == 0 && randomColor == 0) {
                    correct_touch++;
                    long shape_time_end = SystemClock.elapsedRealtime() - chronometer.getBase();
                    long shape_time_elapse = shape_time_end - shape_time_start;
                    score += timeToScore(shape_time_elapse);
                    TextView textView = findViewById(R.id.textView_score);
                    textView.setText(String.format("%03d",score));
                    // when 10 correct touch reached, game end;
                    if (correct_touch >= 10) {
                        pauseTime();
                        endGame();
                        return;
                    }
                }
                else {
                    wrong_touch++;
                }
                // create another shape only when there are less than 12 shapes
                int remain_space = 12;
                if (points != null) {
                    remain_space = 12 - points.size();
                }
                if (remain_space > 0) {
                    drawShape(layout);
                }
            }
        });
        // draw shape on screen
        layout.addView(shape);
    }

    public PointCoord randomPosition() {
        PointCoord point = new PointCoord();
        Random random = new Random();
        boolean overlap = true;
        int tmp_x = 0, tmp_y = 0;
        while (overlap) {
            overlap = false;
            tmp_x = draw_x_min + random.nextInt(draw_x_max-draw_x_min);
            tmp_y = draw_y_min + random.nextInt(draw_y_max-draw_y_min);
            int len = 0;
            if (points != null) {
                len = points.size();
            }
            for (int i = 0; i < len; i++) {
                if (shapeOverlap(tmp_x - points.get(i).getX(),tmp_y - points.get(i).getY())) {
                    overlap = true;
                }
            }
        }
        point.setX(tmp_x);
        point.setY(tmp_y);
        points.add(point);
//        point.setX(0);point.setY(0);    // for test use
        return point;
    }

    public boolean shapeOverlap(int dist_x, int dist_y) {
        double distance = Math.sqrt(Math.pow(dist_x,2) + Math.pow(dist_y,2));
        if (distance < 64*dp_resize*Math.sqrt(2)) return true;
        return false;
    }

    public void endGame() {
        Toast.makeText(this,"Game Finished!!!",Toast.LENGTH_LONG).show();
        // clear all views
        ConstraintLayout gameLayout = findViewById(R.id.gameScene);
        gameLayout.removeAllViews();
        // create and show result TextView
        showResult();
//        Toast.makeText(this,"punishment: " + punish_time,Toast.LENGTH_LONG).show();

        /*
            calculate the score
            by difference time instead of total time
         */
        /*
            just clean the lower part
            and generate new TextView to show results.
            Maybe go to new high score link to previous assignment
         */
    }

    public int timeToScore(long time) {
        /*
            this function give a corresponding score
            according to correct touch difference time
         */
        int score = 0;
        if(time < 300)
            score = 10;
        else if (time < 600)
            score = 9;
        else if (time < 1000)
            score = 8;
        else if (time < 2000)
            score = 7;
        else if (time < 3000)
            score = 6;
        else if (time < 4000)
            score = 5;
        else if (time < 5000)
            score = 4;
        else if (time < 6000)
            score = 3;
        else if (time < 7000)
            score = 2;
        return score;
    }

    public void showResult() {
        // refer this layout
        ConstraintLayout gameLayout = findViewById(R.id.gameScene);
        // setup result TextView
        TextView result = new TextView(getApplicationContext());
        result.setId(R.id.textView_score);
        ConstraintLayout.LayoutParams params_tv = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        // setup addScore Button if new high score occur
        Button addScore = new Button(getApplicationContext());
        addScore.setId(R.id.addscore_button);
        ConstraintLayout.LayoutParams params_btn1 = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        // setup return Button if new high score occur but not adding this score
        Button not_addScore = new Button(getApplicationContext());
        not_addScore.setId(R.id.not_addScore_button);
        ConstraintLayout.LayoutParams params_btn2 = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        // put TextView to the center of the screen on Button
        params_tv.topToTop = R.id.gameScene;
        params_tv.leftToLeft = R.id.gameScene;
        params_tv.rightToRight = R.id.gameScene;
        params_tv.bottomToBottom = R.id.gameScene;
        // put Button1 to the center of the screen under TextView
        params_btn1.topToBottom = R.id.textView_score;
        params_btn1.leftToLeft = R.id.gameScene;
        params_btn1.rightToLeft = R.id.not_addScore_button;
        params_btn1.bottomToBottom = R.id.gameScene;
        // put Button2 to the center of the screen under TextView
        params_btn2.topToTop = R.id.addscore_button;
        params_btn2.leftToRight = R.id.addscore_button;
        params_btn2.rightToRight = R.id.gameScene;
        params_btn2.bottomToBottom = R.id.addscore_button;
        // apply LayoutParams
        result.setLayoutParams(params_tv);
        result.setGravity(Gravity.CENTER_VERTICAL);
        addScore.setLayoutParams(params_btn1);
        addScore.setGravity(Gravity.CENTER_VERTICAL);
        not_addScore.setLayoutParams(params_btn2);
        not_addScore.setGravity(Gravity.CENTER_VERTICAL);

        addScore.setText(R.string.add_score);
        addScore.setVisibility(View.INVISIBLE);
        not_addScore.setText(R.string.not_add_score);
        not_addScore.setVisibility(View.INVISIBLE);

        // set result text info
        String text_h1 = ""; // if new high score
        String text_h2 = "Your score: ";
        @SuppressLint("DefaultLocale")
        String text_h2_add = String.format("%03d",score);
        String text_h2_endl = "\n";
        @SuppressLint("DefaultLocale")
        String text_h3 = "You missed " + miss_touch + " target(s)\n";
        String text_h3_add = "";
        String text_h4 = "";

        // check high score
        ArrayList<Person> peopleList = new ArrayList<>();
        fileIO fileIO = new fileIO();
        fileIO.loadList(this,peopleList);
        boolean new_high_score = true;
        /*
            NOTICE : loadlist will automatically add first row as header
                     so it must have at least 1 item
         */
        if (peopleList.size() == 13) {
            int last_person = peopleList.size()-1;
            int min_score_in_list = Integer.parseInt(peopleList.get(last_person).getScore());
            if (min_score_in_list > score) {
                new_high_score = false;
            }
        }
//        else if (Integer.parseInt(peopleList.get(peopleList.size()-1).getScore()) < score) {
//            new_high_score = true;
//        }
        if (new_high_score) {
            text_h1 = "New High Score!!!\n";
            text_h4 = "Want to upload your score?";
            params_tv.bottomToTop = R.id.addscore_button;

            addScore.setVisibility(View.VISIBLE);
            not_addScore.setVisibility(View.VISIBLE);
            gameLayout.addView(addScore);
            gameLayout.addView(not_addScore);
        }
        else {

        }
        if (miss_touch > 0) {
            text_h3_add = "with extra punish: " + punish_time + "\n";
        }

        String text = text_h1 + text_h2 + text_h2_add + text_h2_endl + text_h3 + text_h3_add + text_h4;
        result.setTextSize(TypedValue.COMPLEX_UNIT_SP,28);
        result.setText(text);
        gameLayout.addView(result);

        addScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewScore();
            }
        });
        not_addScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMain();
            }
        });
    }

    public void addNewScore() {
        String final_score = String.valueOf(score);
        Intent intent = new Intent(this, AddScore.class);
        intent.putExtra("score",final_score);
        startActivity(intent);
    }

    public void backToMain() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }
}

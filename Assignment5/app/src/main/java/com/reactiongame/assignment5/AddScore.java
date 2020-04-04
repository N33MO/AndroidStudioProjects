package com.reactiongame.assignment5;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

// This class reference previous homework to add a score
// Something changed: to make the score as the game score we get and "enable = false"
public class AddScore extends AppCompatActivity {

    // set basic variables
    EditText name, score, date;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addscore);

        name = findViewById(R.id.name);
        score = findViewById(R.id.score);
        date = findViewById(R.id.time);

        String final_score = "0";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final_score = extras.getString("score");
        }
        score.setText(final_score);

        // There I integrate DatePickerDialog in OnCreate function
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize variables for DatePickerDialog
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Initialize the DatePickerDialog
                DatePickerDialog dialog = new DatePickerDialog(AddScore.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());  // Disable future Date
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                String yyyy = String.format(Locale.getDefault(),"%04d",year);
                String mm = String.format(Locale.getDefault(),"%02d",month);
                String dd = String.format(Locale.getDefault(),"%02d", dayOfMonth);
                String textDate = yyyy + "-" + mm + "-" + dd;
                date.setText(textDate);
            }
        };
    }

    public void save(View v) throws IOException {
        // function to save a score
        String name_ck = name.getText().toString();
        String score_ck = score.getText().toString();
        String date_ck = date.getText().toString();
        String regDate =  "^((?:(?:1[6-9]|2[0-9])\\d{2})(-)(?:(?:(?:0[13578]|1[02])(-)31)|((0[1,3-9]|1[0-2])(-)(29|30))))$|^(?:(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00)))(-)02(-)29)$|^(?:(?:1[6-9]|2[0-9])\\d{2})(-)(?:(?:0[1-9])|(?:1[0-2]))(-)(?:0[1-9]|1\\d|2[0-8])$";
        boolean score_ok = score_ck.matches("[1-9][0-9]*");
        boolean date_ok = date_ck.matches(regDate);
//        date_ok = true;
//        Toast.makeText(this,"regex: " + score_ok + " and " + date_ok, Toast.LENGTH_LONG).show();
        if (score_ok && date_ok) {
            String text = name_ck + "\t" + score_ck + "\t" + date_ck + "\n";
            // save in FILE IO class
            fileIO fileIO = new fileIO();
            Person person = new Person(name.getText().toString(), score.getText().toString(), date.getText().toString());
//            fileIO.saveNew(this, text);
            fileIO.func1(this, person);
            name.getText().clear();
            score.getText().clear();
            date.getText().clear();
            Toast.makeText(this, "item saved.", Toast.LENGTH_LONG).show();
            finish();
        }
        else {
            if (!score_ok && date_ok) {
                Toast.makeText(this,"Wrong score input: " + score_ck, Toast.LENGTH_LONG).show();
                score.getText().clear();
            }
            else if (score_ok && !date_ok) {
                Toast.makeText(this,"Wrong date input: " + date_ck, Toast.LENGTH_LONG).show();
                date.getText().clear();
            }
            else {
                Toast.makeText(this,"Wrong score and date input.", Toast.LENGTH_LONG).show();
                score.getText().clear();
                date.getText().clear();
            }
        }
    }
}

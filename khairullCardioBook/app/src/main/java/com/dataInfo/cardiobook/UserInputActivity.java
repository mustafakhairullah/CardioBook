package com.dataInfo.cardiobook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class UserInputActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{
    EditText systolic, diastolic, heartRate, comment;
    TextView date, time;
    Button saveButton;

    int positionToEdit = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);

        //initializing the views in the activity
        systolic = findViewById(R.id.systolic);
        diastolic = findViewById(R.id.diastolic);
        heartRate = findViewById(R.id.heartRate);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        comment = findViewById(R.id.comment);

        //listen for incoming data
        Bundle incomingIntent = getIntent().getExtras();

        if(incomingIntent != null) {
            //capture incoming data
            String systolicIntent = incomingIntent.getString("systolic");
            String diastolicIntent = incomingIntent.getString("diastolic");
            String heartRateIntent = incomingIntent.getString("heartRate");
            String dateIntent = incomingIntent.getString("date");
            String timeIntent = incomingIntent.getString("time");
            String commentIntent = incomingIntent.getString("comment");

            positionToEdit = incomingIntent.getInt("edit");

            //fill in the form
            systolic.setText(systolicIntent);
            diastolic.setText(diastolicIntent);
            heartRate.setText(heartRateIntent);
            date.setText(dateIntent);
            time.setText(timeIntent);
            comment.setText(commentIntent);

        }

        //initializing the button
        saveButton = findViewById(R.id.saveButton);

        //setting the button pressing activity
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //get strings from the editText objects
                String SystolicPressure = systolic.getText().toString();
                String DiastolicPressure = diastolic.getText().toString();
                String HeartRateBPM = heartRate.getText().toString();
                String Date = date.getText().toString();
                String Time = time.getText().toString();
                String Comment = comment.getText().toString();

                Intent mainActivity = new Intent(v.getContext(), MainActivity.class);

                mainActivity.putExtra("systolic", SystolicPressure);
                mainActivity.putExtra("diastolic", DiastolicPressure);
                mainActivity.putExtra("BPM", HeartRateBPM);
                mainActivity.putExtra("date", Date);
                mainActivity.putExtra("time", Time);
                mainActivity.putExtra("comment", Comment);

                mainActivity.putExtra("edit", positionToEdit);

                startActivity(mainActivity);
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showDatePickerDialog();
            }
        });

        time.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showTimePickerDialog();
            }
        }));
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        String dateInput = year + "-"+ convertDate(month+1) +"-"+convertDate(dayOfMonth);
        date.setText(dateInput);
    }

    private void showTimePickerDialog()
    {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {
        String timeInput = convertDate(hourOfDay) + ":" + convertDate(minute);
        time.setText(timeInput);
    }

    private String convertDate(int input) {
        if (input >= 10) {
            return String.valueOf(input);
        } else {
            return "0" + input;
        }
    }
}

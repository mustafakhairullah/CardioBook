package com.dataInfo.cardiobook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    ListView measurementList;
    Button addMeasurements;

    //initializing Datalist and objects in the list
    ArrayList<Measurement> measurementDataList;
    private ArrayList<Measurement> measurements = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //loading data
        loadData();

        //button to add an object into the list
        addMeasurements = findViewById(R.id.addMeasureButton);

        //initializing the data-list and the list from the view
        measurementDataList = new ArrayList<>();
        measurementList = findViewById(R.id.measureList);

        //initializing the custom list adaptor
        final ArrayAdapter measurementAdaptor = new MeasurementAdapter(measurements, this);

        measurementList.setAdapter(measurementAdaptor);

        //listen for incoming messages
        Bundle incomingMessages = getIntent().getExtras();

        if (incomingMessages != null)
        {
            //capture incoming data
            String systolicIncoming = incomingMessages.getString("systolic");
            String diastolicIncoming = incomingMessages.getString("diastolic");
            String heartRateIncoming = incomingMessages.getString("BPM");
            String dateIncoming = incomingMessages.getString("date");
            String timeIncoming = incomingMessages.getString("time");
            String commentIncoming = incomingMessages.getString("comment");

            int positionEdited = incomingMessages.getInt("edit");

            //create new measurement objects
            Measurement measurementObject = new Measurement(dateIncoming, timeIncoming, systolicIncoming, diastolicIncoming, heartRateIncoming, commentIncoming);

            //add this object to the list and update the adapter
            if (positionEdited > -1)
            {
                //removing an object from the list on a long click
                measurements.remove(positionEdited);

                //notifying the adapter that the data-set has changed
                measurementAdaptor.notifyDataSetChanged();

                //saving data after removing an object from the list
                saveData();
            }

            measurements.add(measurementObject);
            measurementAdaptor.notifyDataSetChanged();

            //saving data
            saveData();
        }

        //listener for adding objects(calls the input activity)
        addMeasurements.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //calling user activity to input data
                Intent UserActivity = new Intent(MainActivity.this, UserInputActivity.class);
                startActivity(UserActivity);
                finish();
            }
        });

        //listener for editing an object
        measurementList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //calling user activity to edit data
                editMeasurement(position);
            }
        });

        //long press listener to delete an object
        measurementList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                view.setSelected(true);

                //removing an object from the list on a long click
                measurements.remove(position);

                //notifying the adapter that the data-set has changed
                measurementAdaptor.notifyDataSetChanged();

                //saving data after removing an object from the list
                saveData();

                return true;
            }
        });
    }

    //saving data that is entered locally
    private void saveData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(measurements);
        editor.putString("task list", json);
        editor.apply();
    }

    //method for loading data(called onCreate)
    private void loadData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Measurement>>() {}.getType();
        measurements = gson.fromJson(json, type);

        if(measurements == null)
        {
            measurements = new ArrayList<>();
        }
    }

    public void editMeasurement(int position)
    {
        Intent intent = new Intent(getApplicationContext(), UserInputActivity.class);


        //get the contents of the object clicked
        Measurement measurementClicked = measurements.get(position);

        intent.putExtra("edit", position);
        intent.putExtra("systolic", measurementClicked.getSystolicPressure());
        intent.putExtra("diastolic", measurementClicked.getDiastolicPressure());
        intent.putExtra("heartRate", measurementClicked.getHeartRate());
        intent.putExtra("date", measurementClicked.getDate());
        intent.putExtra("time", measurementClicked.getTime());
        intent.putExtra("comment", measurementClicked.getComment());

        startActivity(intent);
    }
}
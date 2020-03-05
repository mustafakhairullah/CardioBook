package com.dataInfo.cardiobook;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MeasurementAdapter extends ArrayAdapter<Measurement> {

    private ArrayList<Measurement> measurements;
    private Context context;

    public MeasurementAdapter(ArrayList<Measurement> measurements, Context context) {
        super(context, 0, measurements);
        this.measurements = measurements;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null)
        {
            view = LayoutInflater.from(context).inflate(R.layout.content_list, parent, false);
        }

        Measurement measurementObject = measurements.get(position);

        TextView systolic = view.findViewById(R.id.systolicPressure);
        TextView diastolic = view.findViewById(R.id.diastolicPressure);
        TextView heart_rate = view.findViewById(R.id.heartRate);
        TextView date = view.findViewById(R.id.date);

        //getting the Strings from the object list
        String systolicObject = measurementObject.getSystolicPressure();
        String diastolicObject = measurementObject.getDiastolicPressure();

        //displaying the Strings on the GUI
        systolic.setText("Systolic Pressure:\t"+ measurementObject.getSystolicPressure());
        diastolic.setText("Diastolic Pressure:\t"+ measurementObject.getDiastolicPressure());
        heart_rate.setText("Heart Rate (BPM):\t"+ measurementObject.getHeartRate());
        date.setText(measurementObject.getDate());

        //converting the Pressures into integers
        int SystolicPressureInt = Integer.parseInt(systolicObject);
        int DiastolicPressureInt = Integer.parseInt(diastolicObject);

        boolean notNormalSystolic = false;
        boolean notNormalDiastolic =  false;

        if (SystolicPressureInt < 90 || SystolicPressureInt > 140)
        {
            notNormalSystolic = true;
            systolic.setBackgroundColor(Color.parseColor("#ff0000"));
        }
        else
        {
            systolic.setBackgroundColor(Color.parseColor("WHITE"));
        }
        if (DiastolicPressureInt < 60 || DiastolicPressureInt > 90)
        {
            notNormalDiastolic = true;
            diastolic.setBackgroundColor(Color.parseColor("#ff0000"));
        }
        else
        {
            diastolic.setBackgroundColor(Color.parseColor("WHITE"));
        }

        return view;
    }
}

package com.github.badoualy.datepicker.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.github.badoualy.datepicker.DatePickerTimeline;
import com.github.badoualy.datepicker.MonthView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        DatePickerTimeline timeline = (DatePickerTimeline) findViewById(R.id.timeline);
        final MonthView.DateLabelAdapter adapter = new MonthView.DateLabelAdapter() {

            @Override
            public CharSequence getLabel(int year, int month, int day) {
                return Integer.toString(month + 1) + "/" + (year % 2000);
            }
        };
        timeline.setDateLabelAdapter(adapter);

        tes();
    }

    private void tes() {
        Log.d("ya", "ay");
        for (int i = 0; i < 24; i++) {
            Log.e("Yann", i + ": " + getYearForPosition(i));
        }
    }


    private int getYearForPosition(int position) {
        return (position + 1) / 12 + 2016;
    }

}

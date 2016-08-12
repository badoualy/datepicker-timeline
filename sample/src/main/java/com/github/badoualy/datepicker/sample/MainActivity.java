package com.github.badoualy.datepicker.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

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
            public CharSequence getLabel(int year, int month, int day, int index) {
                return Integer.toString(month + 1) + "/" + (year % 2000);
            }
        };
        timeline.setDateLabelAdapter(adapter);
    }

}

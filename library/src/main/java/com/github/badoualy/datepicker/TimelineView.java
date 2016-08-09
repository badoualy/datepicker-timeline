package com.github.badoualy.datepicker;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

class TimelineView extends RecyclerView {

    private static final String[] WEEK_DAYS = DateFormatSymbols.getInstance().getShortWeekdays();
    private Random random = new Random(System.currentTimeMillis());

    private final Calendar calendar = Calendar.getInstance(Locale.getDefault());
    private LinearLayoutManager layoutManager;
    private DatePickerTimeline.OnDateSelectedListener onDateSelectedListener;
    private MonthView.DateLabelAdapter dateLabelAdapter;

    private int selectedYear, selectedMonth, selectedDay;
    private int selectedPosition = 0;
    private TimelineAdapter adapter;

    // Day letter
    private int lblDayColor;
    // Day number label
    private int lblDateColor, lblDateSelectedColor;
    // Label
    private int lblLabelColor;

    public TimelineView(Context context) {
        super(context);
        init();
    }

    public TimelineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimelineView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        calendar.setTimeInMillis(System.currentTimeMillis());
        selectedYear = DatePickerTimeline.startYear;
        selectedMonth = DatePickerTimeline.startMonth;
        selectedDay = DatePickerTimeline.startDay;
        setSelectedDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        resetCalendar();

        lblDayColor = DatePickerTimeline.lblDayColor;
        lblDateColor = DatePickerTimeline.lblDateColor;
        lblDateSelectedColor = DatePickerTimeline.lblDateSelectedColor;
        lblLabelColor = DatePickerTimeline.lblLabelColor;

        setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        adapter = new TimelineAdapter();
        setLayoutManager(layoutManager);
        setAdapter(adapter);
    }

    private void resetCalendar() {
        calendar.set(DatePickerTimeline.startYear, DatePickerTimeline.startMonth, DatePickerTimeline.startDay, 1, 0, 0);
    }

    private void onDateSelected(int position, int year, int month, int day) {
        if (position == selectedPosition)
            return;

        int oldPosition = selectedPosition;
        selectedPosition = position;

        this.selectedYear = year;
        this.selectedMonth = month;
        this.selectedDay = day;

        if (adapter != null && layoutManager != null) {
            adapter.notifyItemChanged(oldPosition);
            adapter.notifyItemChanged(position);

            // Animate scroll
            int offset = getMeasuredWidth() / 2 - getChildAt(0).getMeasuredWidth() / 2;
            layoutManager.scrollToPositionWithOffset(selectedPosition, offset);

            if (onDateSelectedListener != null)
                onDateSelectedListener.onDateSelected(selectedYear, selectedMonth, selectedDay);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    // Animate scroll
                    int offset = getMeasuredWidth() / 2 - getChildAt(0).getMeasuredWidth() / 2;
                    layoutManager.scrollToPositionWithOffset(selectedPosition, offset);
                }
            });
        }
    }

    public void setSelectedDate(int year, int month, int day) {
        // Get new selected dayOfYear
        calendar.set(year, month, day, 1, 0, 0);
        final long newTimestamp = calendar.getTimeInMillis();

        // Get current selected dayOfYear
        calendar.set(selectedYear, selectedMonth, selectedDay, 1, 0, 0);
        final long oldTimestamp = calendar.getTimeInMillis();

        // IMPORTANT: since we only use year/month/day, and we don't care about the time, we'll always have a good day difference
        int dayDifference = (int) ((newTimestamp - oldTimestamp) / TimeUnit.DAYS.toMillis(1));
        onDateSelected(selectedPosition + dayDifference, year, month, day);
    }

    public void setOnDateSelectedListener(DatePickerTimeline.OnDateSelectedListener onDateSelectedListener) {
        this.onDateSelectedListener = onDateSelectedListener;
    }

    public DatePickerTimeline.OnDateSelectedListener getOnDateSelectedListener() {
        return onDateSelectedListener;
    }

    public void setDateLabelAdapter(@Nullable MonthView.DateLabelAdapter dateLabelAdapter) {
        this.dateLabelAdapter = dateLabelAdapter;
    }

    private class TimelineAdapter extends RecyclerView.Adapter<TimelineViewHolder> {

        public TimelineAdapter() {

        }

        @Override
        public TimelineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext())
                                            .inflate(R.layout.mti_item_day, parent, false);
            return new TimelineViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TimelineViewHolder holder, int position) {
            // Set calendar
            resetCalendar();
            calendar.add(Calendar.DAY_OF_YEAR, position);

            // Get values
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            boolean isToday = DateUtils.isToday(calendar.getTimeInMillis());

            holder.bind(position, year, month, day, dayOfWeek, dateLabelAdapter != null ? dateLabelAdapter.getLabel(year, month, day) : "",
                        position == selectedPosition, isToday);
        }

        @Override
        public int getItemCount() {
            return Integer.MAX_VALUE;
        }
    }

    public class TimelineViewHolder extends RecyclerView.ViewHolder {

        private final TextView lblDay;
        private final TextView lblDate;
        private final TextView lblValue;

        private int position;
        private int year, month, day;

        public TimelineViewHolder(View root) {
            super(root);

            lblDay = (TextView) root.findViewById(R.id.lbl_day);
            lblDate = (TextView) root.findViewById(R.id.lbl_date);
            lblValue = (TextView) root.findViewById(R.id.lbl_value);

            lblDay.setTextColor(lblDayColor);
            lblDate.setTextColor(lblDateColor);
            lblValue.setTextColor(lblLabelColor);

            root.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDateSelected(position, year, month, day);
                }
            });
        }

        public void bind(int position, int year, int month, int day, int dayOfWeek, CharSequence label, boolean selected, boolean isToday) {
            this.position = position;
            this.year = year;
            this.month = month;
            this.day = day;

            lblDay.setText(WEEK_DAYS[dayOfWeek]);
            lblDate.setText(String.valueOf(day));
            lblValue.setText(label);

            lblDate.setBackgroundResource(selected ? R.drawable.mti_bg_lbl_date_selected
                                                  : (isToday ? R.drawable.mti_bg_lbl_date_today : 0));
            lblDate.setTextColor(selected || isToday ? lblDateSelectedColor : lblDateColor);
        }
    }
}

package com.github.badoualy.datepicker;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Calendar;

public final class DatePickerTimeline extends LinearLayout implements MonthView.OnMonthSelectedListener {

    private MonthView monthView;
    private TimelineView timelineView;
    private OnDateSelectedListener onDateSelectedListener;
    private TimelineScrollListener timelineScrollListener;

    public DatePickerTimeline(Context context) {
        this(context, null);
    }

    public DatePickerTimeline(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public DatePickerTimeline(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DatePickerTimeline(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        final Calendar calendar = Calendar.getInstance();
        int startYear = calendar.get(Calendar.YEAR);
        //noinspection WrongConstant
        if (calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
            // If we are in january, we'll probably want to have previous year :)
            startYear--;
        }
        final int startMonth = Calendar.JANUARY;
        final int startDay = 1;

        // Load default values
        int primaryColor = Utils.getPrimaryColor(getContext());
        int primaryDarkColor = Utils.getPrimaryDarkColor(getContext());
        int tabSelectedColor = ContextCompat.getColor(getContext(), R.color.mti_lbl_tab_selected);
        int tabBeforeSelectionColor = ContextCompat.getColor(getContext(), R.color.mti_lbl_tab_before_selection);
        int lblDayColor = ContextCompat.getColor(getContext(), R.color.mti_lbl_day);
        int lblDateColor = ContextCompat.getColor(getContext(), R.color.mti_lbl_date);
        int lblDateSelectedColor = ContextCompat.getColor(getContext(), R.color.mti_lbl_date_selected);
        int bgLblDateSelectedColor = ContextCompat.getColor(getContext(), R.color.mti_bg_lbl_date_selected_color);
        int ringLblDateSelectedColor = ContextCompat.getColor(getContext(), R.color.mti_ring_lbl_date_color);
        int bgLblTodayColor = ContextCompat.getColor(getContext(), R.color.mti_bg_lbl_today);
        int lblLabelColor = ContextCompat.getColor(getContext(), R.color.mti_lbl_label);

        // Load xml attrs
        final TypedArray a = getContext()
                .obtainStyledAttributes(attrs, R.styleable.DatePickerTimeline, defStyleAttr, 0);
        primaryColor = a.getColor(R.styleable.DatePickerTimeline_mti_primaryColor, primaryColor);
        primaryDarkColor = a.getColor(R.styleable.DatePickerTimeline_mti_primaryDarkColor, primaryDarkColor);
        tabSelectedColor = a.getColor(R.styleable.DatePickerTimeline_mti_tabSelectedColor, tabSelectedColor);
        tabBeforeSelectionColor = a
                .getColor(R.styleable.DatePickerTimeline_mti_tabBeforeSelectionColor, tabBeforeSelectionColor);
        lblDayColor = a.getColor(R.styleable.DatePickerTimeline_mti_lblDayColor, lblDayColor);
        lblDateColor = a.getColor(R.styleable.DatePickerTimeline_mti_lblDateColor, lblDateColor);
        lblDateSelectedColor = a
                .getColor(R.styleable.DatePickerTimeline_mti_lblDateSelectedColor, lblDateSelectedColor);
        bgLblDateSelectedColor = a
                .getColor(R.styleable.DatePickerTimeline_mti_bgLblDateSelectedColor, bgLblDateSelectedColor);
        ringLblDateSelectedColor = a
                .getColor(R.styleable.DatePickerTimeline_mti_ringLblDateSelectedColor, ringLblDateSelectedColor);
        bgLblTodayColor = a.getColor(R.styleable.DatePickerTimeline_mti_bgLblTodayColor, bgLblTodayColor);
        lblLabelColor = a.getColor(R.styleable.DatePickerTimeline_mti_lblLabelColor, lblLabelColor);
        boolean followScroll = a.getBoolean(R.styleable.DatePickerTimeline_mti_followScroll, true);
        a.recycle();

        final LayerDrawable selectedDrawable = (LayerDrawable) ContextCompat
                .getDrawable(getContext(), R.drawable.mti_bg_lbl_date_selected);
        ((GradientDrawable) selectedDrawable.getDrawable(0)).setColor(ringLblDateSelectedColor);
        ((GradientDrawable) selectedDrawable.getDrawable(1)).setColor(bgLblDateSelectedColor);
        final LayerDrawable todayDrawable = (LayerDrawable) ContextCompat
                .getDrawable(getContext(), R.drawable.mti_bg_lbl_date_today);
        ((GradientDrawable) todayDrawable.getDrawable(1)).setColor(bgLblTodayColor);

        setOrientation(VERTICAL);
        final View view = inflate(getContext(), R.layout.mti_datepicker_timeline, this);

        monthView = (MonthView) view.findViewById(R.id.mti_month_view);
        timelineView = (TimelineView) view.findViewById(R.id.mti_timeline);

        monthView.setBackgroundColor(primaryColor);
        monthView.setFirstDate(startYear, startMonth);
        monthView.setDefaultColor(primaryDarkColor);
        monthView.setColorSelected(tabSelectedColor);
        monthView.setColorBeforeSelection(tabBeforeSelectionColor);
        monthView.setOnMonthSelectedListener(this);

        timelineView.setBackgroundColor(Color.WHITE);
        timelineView.setFirstDate(startYear, startMonth, startDay);
        timelineView.setDayLabelColor(lblDayColor);
        timelineView.setDateLabelColor(lblDateColor);
        timelineView.setDateLabelSelectedColor(lblDateSelectedColor);
        timelineView.setLabelColor(lblLabelColor);
        timelineView.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int index) {
                monthView.setSelectedMonth(year, month, false, timelineScrollListener == null);

                if (onDateSelectedListener != null) {
                    onDateSelectedListener.onDateSelected(year, month, day, index);
                }
            }
        });

        timelineView.setSelectedDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                     calendar.get(Calendar.DAY_OF_MONTH));

        if (followScroll) {
            timelineScrollListener = new TimelineScrollListener(monthView, timelineView);
            timelineView.addOnScrollListener(timelineScrollListener);
        }
    }

    public int getSelectedYear() {
        return timelineView.getSelectedYear();
    }

    public int getSelectedMonth() {
        return timelineView.getSelectedMonth();
    }

    public int getSelectedDay() {
        return timelineView.getSelectedDay();
    }

    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        this.onDateSelectedListener = onDateSelectedListener;
    }

    public OnDateSelectedListener getOnDateSelectedListener() {
        return onDateSelectedListener;
    }

    public void setDateLabelAdapter(@Nullable MonthView.DateLabelAdapter dateLabelAdapter) {
        timelineView.setDateLabelAdapter(dateLabelAdapter);
    }

    public MonthView getMonthView() {
        return monthView;
    }

    public TimelineView getTimelineView() {
        return timelineView;
    }

    public void setSelectedDate(int year, int month, int day) {
        timelineView.setSelectedDate(year, month, day);
    }

    @Override
    public void onMonthSelected(int year, int month, int index) {
        timelineView.setSelectedDate(year, month, 1);
    }

    public void setFirstVisibleDate(int year, int month, int day) {
        monthView.setFirstDate(year, month);
        timelineView.setFirstDate(year, month, day);
    }

    public void setLastVisibleDate(int year, int month, int day) {
        monthView.setLastDate(year, month);
        timelineView.setLastDate(year, month, day);
    }

    public void centerOnSelection() {
        monthView.centerOnSelection();
        timelineView.centerOnSelection();
    }

    public int getMonthSelectedPosition() {
        return monthView.getSelectedPosition();
    }

    public int getTimelineSelectedPosition() {
        return timelineView.getSelectedPosition();
    }

    public void setFollowScroll(boolean followScroll) {
        if (!followScroll && timelineScrollListener != null) {
            timelineView.removeOnScrollListener(timelineScrollListener);
            timelineScrollListener = null;
        } else if (followScroll && timelineScrollListener == null) {
            timelineScrollListener = new TimelineScrollListener(monthView, timelineView);
            timelineView.addOnScrollListener(timelineScrollListener);
        }
    }

    public interface OnDateSelectedListener {

        void onDateSelected(int year, int month, int day, int index);
    }
}

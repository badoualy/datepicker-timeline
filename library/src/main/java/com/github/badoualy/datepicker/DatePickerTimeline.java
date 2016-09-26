package com.github.badoualy.datepicker;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
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

    static int startYear, startMonth, startDay;

    // Attrs, a bit ugly
    static int primaryColor, primaryDarkColor;
    // Month view
    static int tabSelectedColor, tabBeforeSelectionColor;
    // Day letter
    static int lblDayColor;
    // Day number label
    static int lblDateColor, lblDateSelectedColor;
    static int bgLblDateSelectedColor, ringLblDateSelectedColor;
    static int bgLblTodayColor;
    // Label
    static int lblLabelColor;

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
        final Calendar instance = Calendar.getInstance();
        startYear = instance.get(Calendar.YEAR);
        if (instance.get(Calendar.MONTH) == Calendar.JANUARY) {
            // If we are in january, we'll probably want to have previous year :)
            startYear--;
        }
        startMonth = Calendar.JANUARY;
        startDay = 1;

        // Load default values
        primaryColor = Utils.getPrimaryColor(getContext());
        primaryDarkColor = Utils.getPrimaryDarkColor(getContext());
        tabSelectedColor = ContextCompat.getColor(getContext(), R.color.mti_lbl_tab_selected);
        tabBeforeSelectionColor = ContextCompat.getColor(getContext(), R.color.mti_lbl_tab_before_selection);
        lblDayColor = ContextCompat.getColor(getContext(), R.color.mti_lbl_day);
        lblDateColor = ContextCompat.getColor(getContext(), R.color.mti_lbl_date);
        lblDateSelectedColor = ContextCompat.getColor(getContext(), R.color.mti_lbl_date_selected);
        bgLblDateSelectedColor = ContextCompat.getColor(getContext(), R.color.mti_bg_lbl_date_selected_color);
        ringLblDateSelectedColor = ContextCompat.getColor(getContext(), R.color.mti_ring_lbl_date_color);
        bgLblTodayColor = ContextCompat.getColor(getContext(), R.color.mti_bg_lbl_today);
        lblLabelColor = ContextCompat.getColor(getContext(), R.color.mti_lbl_label);

        // Load xml attrs
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DatePickerTimeline, defStyleAttr, 0);
        primaryColor = a.getColor(R.styleable.DatePickerTimeline_mti_primaryColor, primaryColor);
        primaryDarkColor = a.getColor(R.styleable.DatePickerTimeline_mti_primaryDarkColor, primaryDarkColor);
        tabSelectedColor = a.getColor(R.styleable.DatePickerTimeline_mti_tabSelectedColor, tabSelectedColor);
        tabBeforeSelectionColor = a.getColor(R.styleable.DatePickerTimeline_mti_tabBeforeSelectionColor, tabBeforeSelectionColor);
        lblDayColor = a.getColor(R.styleable.DatePickerTimeline_mti_lblDayColor, lblDayColor);
        lblDateColor = a.getColor(R.styleable.DatePickerTimeline_mti_lblDateColor, lblDateColor);
        lblDateSelectedColor = a.getColor(R.styleable.DatePickerTimeline_mti_lblDateSelectedColor, lblDateSelectedColor);
        bgLblDateSelectedColor = a.getColor(R.styleable.DatePickerTimeline_mti_bgLblDateSelectedColor, bgLblDateSelectedColor);
        ringLblDateSelectedColor = a.getColor(R.styleable.DatePickerTimeline_mti_ringLblDateSelectedColor, ringLblDateSelectedColor);
        bgLblTodayColor = a.getColor(R.styleable.DatePickerTimeline_mti_bgLblTodayColor, bgLblTodayColor);
        lblLabelColor = a.getColor(R.styleable.DatePickerTimeline_mti_lblLabelColor, lblLabelColor);
        a.recycle();

        final LayerDrawable selectedDrawable = (LayerDrawable) ContextCompat.getDrawable(getContext(), R.drawable.mti_bg_lbl_date_selected);
        ((GradientDrawable) selectedDrawable.getDrawable(0)).setColor(ringLblDateSelectedColor);
        ((GradientDrawable) selectedDrawable.getDrawable(1)).setColor(bgLblDateSelectedColor);
        final LayerDrawable todayDrawable = (LayerDrawable) ContextCompat.getDrawable(getContext(), R.drawable.mti_bg_lbl_date_today);
        ((GradientDrawable) todayDrawable.getDrawable(1)).setColor(bgLblTodayColor);

        setOrientation(VERTICAL);
        final View view = inflate(getContext(), R.layout.mti_datepicker_timeline, this);

        monthView = (MonthView) view.findViewById(R.id.mti_month_view);
        timelineView = (TimelineView) view.findViewById(R.id.mti_timeline);

        monthView.setOnMonthSelectedListener(this);
        timelineView.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int index) {
                monthView.setSelectedMonth(year, month, false);

                if (onDateSelectedListener != null)
                    onDateSelectedListener.onDateSelected(year, month, day, index);
            }
        });
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

    @Override
    public void onMonthSelected(int year, int month, int index) {
        timelineView.setSelectedDate(year, month, 1);
    }

    public void setFirstVisibleDate(int year, int month, int day) {
        DatePickerTimeline.startYear = year;
        DatePickerTimeline.startMonth = month;
        DatePickerTimeline.startDay = day;

        monthView.update();
    }

    public interface OnDateSelectedListener {
        void onDateSelected(int year, int month, int day, int index);
    }
}

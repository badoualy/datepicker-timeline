package com.github.badoualy.datepicker;


import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.Calendar;

class TimelineScrollListener extends RecyclerView.OnScrollListener {

    private MonthView monthView;
    private TimelineView timelineView;

    private Calendar calendar = Calendar.getInstance();
    private int year = -1;
    private int yearOffsetStart = -1;
    private int yearOffsetEnd = -1;

    private int timelineItemWidth;

    TimelineScrollListener(MonthView monthView, TimelineView timelineView) {
        this.monthView = monthView;
        this.timelineView = timelineView;

        timelineItemWidth = timelineView.getResources().getDimensionPixelSize(R.dimen.mti_timeline_width);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int scrollOffset = recyclerView.computeHorizontalScrollOffset();
        int scrollOffsetCenter = scrollOffset + (recyclerView.getMeasuredWidth() / 2);
        int centerPosition = scrollOffsetCenter / timelineItemWidth;

        // TODO: handle first year offset starting left
        if (!(scrollOffsetCenter >= yearOffsetStart && scrollOffsetCenter <= yearOffsetEnd)) {
            calendar.set(timelineView.getStartYear(), timelineView.getStartMonth(), timelineView.getStartDay());
            calendar.add(Calendar.DAY_OF_YEAR, centerPosition);

            year = calendar.get(Calendar.YEAR);
            int yearDayCount = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
            int yearDay = calendar.get(Calendar.DAY_OF_YEAR);

            yearOffsetEnd = yearDayCount * timelineItemWidth;
            if (year != timelineView.getStartYear()) {
                yearOffsetStart = (scrollOffsetCenter - scrollOffset % timelineItemWidth) / yearDay;
                yearOffsetEnd += yearOffsetStart;
            } else {
                yearOffsetStart = timelineView.getMeasuredWidth() / 2;
            }
        }
        Log.v("Yann", "yearOffsetStart: " + yearOffsetStart + ", " + "yearOffsetEnd: " + yearOffsetEnd + ", "
                + "scrollOffsetCenter: " + scrollOffsetCenter);

        float progress = (float) (scrollOffsetCenter - yearOffsetStart) / (yearOffsetEnd - yearOffsetStart);
        int yearOffset = (int) ((1 - progress) * monthView.getYearWidth());
        Log.d("Yann", "progress: " + progress + ", monthOffset: " + yearOffset);
        monthView.scrollToYearPosition(year, yearOffset);
    }
}

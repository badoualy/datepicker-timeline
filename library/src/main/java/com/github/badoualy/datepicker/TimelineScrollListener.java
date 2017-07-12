package com.github.badoualy.datepicker;


import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.Calendar;

class TimelineScrollListener extends RecyclerView.OnScrollListener {

    private MonthView monthView;
    private TimelineView timelineView;

    private Calendar calendar = Calendar.getInstance();
    private int year = -1;
    private int yearStartOffset = 0;
    private int yearEndOffset = 0;
    private int monthCount = 12;

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

        if (!(scrollOffsetCenter >= yearStartOffset && scrollOffsetCenter <= yearEndOffset)) {
            calendar.set(timelineView.getStartYear(), timelineView.getStartMonth(), timelineView.getStartDay());
            int startDay = calendar.get(Calendar.DAY_OF_YEAR);
            calendar.add(Calendar.DAY_OF_YEAR, centerPosition);

            year = calendar.get(Calendar.YEAR);
            int yearDayCount = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);

            if (year != timelineView.getStartYear()) {
                int yearDay = calendar.get(Calendar.DAY_OF_YEAR);
                yearStartOffset = scrollOffsetCenter
                        - yearDay * timelineItemWidth
                        - scrollOffsetCenter % timelineItemWidth;
                monthCount = 12;
            } else {
                yearStartOffset = 0;
                monthCount = 12 - timelineView.getStartMonth();
                yearDayCount -= startDay;
            }

            yearEndOffset = yearStartOffset + yearDayCount * timelineItemWidth;
        }

        Log.v("TimeScrollListener", "yearStartOffset: " + yearStartOffset + ", " + "yearEndOffset: " + yearEndOffset + ", "
                + "scrollOffsetCenter: " + scrollOffsetCenter);
        float progress = (float) (scrollOffsetCenter - yearStartOffset) / (yearEndOffset - yearStartOffset);
        int yearOffset = (int) ((1 - progress) * (monthCount * monthView.getItemWidth()));
        Log.v("TimeScrollListener", "progress: " + progress + ", monthOffset: " + yearOffset);
        monthView.scrollToYearPosition(year, yearOffset);
    }
}

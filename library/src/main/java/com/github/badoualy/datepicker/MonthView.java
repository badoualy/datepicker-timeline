package com.github.badoualy.datepicker;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

public class MonthView extends RecyclerView {

    private static String[] MONTHS = DateFormatSymbols.getInstance().getShortMonths();

    private MonthAdapter adapter;
    private LinearLayoutManager layoutManager;
    private OnMonthSelectedListener onMonthSelectedListener;

    private int defaultColor, colorSelected, colorBeforeSelection;

    private int startYear, startMonth;

    private int selectedYear, selectedMonth;
    private int selectedPosition = -1;

    public MonthView(Context context) {
        super(context);
        init();
    }

    public MonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MonthView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        final Calendar calendar = Calendar.getInstance();
        setSelectedMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), false);

        for (int i = 0; i < MONTHS.length; i++) {
            if (MONTHS[i] != null && MONTHS[i].length() > 3)
                MONTHS[i] = MONTHS[i].substring(0, 3).toUpperCase(Locale.US);
        }

        setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        adapter = new MonthAdapter();
        setLayoutManager(layoutManager);
        setAdapter(adapter);
    }

    public void setSelectedMonth(int year, int month, boolean callListener) {
        onMonthSelected(year, month, callListener);
    }

    public int getSelectedYear() {
        return selectedYear;
    }

    public int getSelectedMonth() {
        return selectedMonth;
    }

    private void onMonthSelected(int year, int month, boolean callListener) {
        int oldPosition = selectedPosition;
        selectedPosition = getPositionForDate(year, month);
        selectedYear = year;
        selectedMonth = month;

        if (selectedPosition == oldPosition) {
            centerOnPosition(selectedPosition);
            return;
        }

        if (adapter != null && layoutManager != null) {
            final int rangeStart = Math.min(oldPosition, selectedPosition);
            final int rangeEnd = Math.max(oldPosition, selectedPosition);
            adapter.notifyItemRangeChanged(rangeStart, rangeEnd - rangeStart + 1);

            // Animate scroll
            centerOnPosition(selectedPosition);

            if (callListener && onMonthSelectedListener != null)
                onMonthSelectedListener.onMonthSelected(year, month, selectedPosition);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    centerOnPosition(selectedPosition);
                }
            });
        }
    }

    private void centerOnPosition(int position) {
        if (getChildCount() == 0) {
            return;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!isLaidOut())
                return;
        }
        // Animate scroll
        int offset = getMeasuredWidth() / 2 - getChildAt(0).getMeasuredWidth() / 2;
        layoutManager.scrollToPositionWithOffset(position, offset);
    }

    private int getYearForPosition(int position) {
        return (position + startMonth) / 12 + startYear;
    }

    private int getMonthForPosition(int position) {
        return (startMonth + position) % 12;
    }

    private int getPositionForDate(int year, int month) {
        return (12 * (year - startYear) + month) - startMonth;
    }

    public void setOnMonthSelectedListener(OnMonthSelectedListener onMonthSelectedListener) {
        this.onMonthSelectedListener = onMonthSelectedListener;
    }

    public OnMonthSelectedListener getOnMonthSelectedListener() {
        return onMonthSelectedListener;
    }

    /** Default indicator and text color */
    public void setDefaultColor(int defaultColor) {
        this.defaultColor = defaultColor;
    }

    /** Color when month is selected */
    public void setColorSelected(int colorSelected) {
        this.colorSelected = colorSelected;
    }

    /** Color when month is before the current selected month */
    public void setColorBeforeSelection(int colorBeforeSelection) {
        this.colorBeforeSelection = colorBeforeSelection;
    }

    public void setFirstDate(int startYear, int startMonth) {
        this.startYear = startYear;
        this.startMonth = startMonth;
        selectedYear = startYear;
        selectedMonth = startMonth;
        selectedPosition = 0;
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    private class MonthAdapter extends RecyclerView.Adapter<MonthViewHolder> {

        MonthAdapter() {

        }

        @Override
        public MonthViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext())
                                            .inflate(R.layout.mti_item_month, parent, false);
            return new MonthViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MonthViewHolder holder, int position) {
            final int year = getYearForPosition(position);
            final int month = getMonthForPosition(position);
            holder.bind(year, month, position == selectedPosition, position < selectedPosition);
        }

        @Override
        public int getItemCount() {
            return Integer.MAX_VALUE;
        }

    }

    public class MonthViewHolder extends RecyclerView.ViewHolder {

        private final TextView lbl;
        private final DotView indicator;

        private int year, month;

        public MonthViewHolder(View root) {
            super(root);

            indicator = (DotView) root.findViewById(R.id.mti_view_indicator);
            lbl = (TextView) root.findViewById(R.id.mti_lbl_tab);

            root.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onMonthSelected(year, month, true);
                }
            });
        }

        public void bind(int year, int month, boolean selected, boolean beforeSelection) {
            this.year = year;
            this.month = month;

            lbl.setText(MONTHS[month]);
            int color = selected ? colorSelected : beforeSelection ? colorBeforeSelection : defaultColor;
            lbl.setTextColor(color);
            indicator.setColor(color);
            indicator.setCircleSizeDp(selected ? 12 : 5);
        }
    }

    public interface OnMonthSelectedListener {
        void onMonthSelected(int year, int month, int index);
    }

    public interface DateLabelAdapter {
        CharSequence getLabel(int year, int month, int day, int index);
    }
}

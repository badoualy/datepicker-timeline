package com.github.badoualy.datepicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

final class Utils {

    private Utils() {

    }

    public static int getPrimaryColor(final Context context) {
        int color = context.getResources().getIdentifier("colorPrimary", "attr", context.getPackageName());
        if (color != 0) {
            // If using support library v7 primaryColor
            TypedValue t = new TypedValue();
            context.getTheme().resolveAttribute(color, t, true);
            color = t.data;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // If using native primaryColor (SDK >21)
            TypedArray t = context.obtainStyledAttributes(new int[]{android.R.attr.colorPrimary});
            color = t.getColor(0, ContextCompat.getColor(context, R.color.mti_default_primary));
            t.recycle();
        } else {
            TypedArray t = context.obtainStyledAttributes(new int[]{R.attr.colorPrimary});
            color = t.getColor(0, ContextCompat.getColor(context, R.color.mti_default_primary));
            t.recycle();
        }

        return color;
    }

    public static int getPrimaryDarkColor(final Context context) {
        int color = context.getResources().getIdentifier("colorPrimaryDark", "attr", context.getPackageName());
        if (color != 0) {
            // If using support library v7 primaryColor
            TypedValue t = new TypedValue();
            context.getTheme().resolveAttribute(color, t, true);
            color = t.data;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // If using native primaryColor (SDK >21)
            TypedArray t = context.obtainStyledAttributes(new int[]{android.R.attr.colorPrimaryDark});
            color = t.getColor(0, ContextCompat.getColor(context, R.color.mti_default_primary_dark));
            t.recycle();
        } else {
            TypedArray t = context.obtainStyledAttributes(new int[]{R.attr.colorPrimaryDark});
            color = t.getColor(0, ContextCompat.getColor(context, R.color.mti_default_primary_dark));
            t.recycle();
        }

        return color;
    }
}

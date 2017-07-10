[![Release](https://jitpack.io/v/badoualy/datepicker-timeline.svg)](https://jitpack.io/#badoualy/datepicker-timeline) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-DatePicker%20Timeline-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/5965)

<img src="https://github.com/badoualy/datepicker-timeline/blob/master/ART/ic_brand.jpg" width="300">

> ### Designed by the awesome https://dribbble.com/LeslyPyram :)

<img src="https://github.com/badoualy/datepicker-timeline/blob/master/ART/demo.gif" width="300">

Setup
----------------

First, add jitpack in your build.gradle at the end of repositories:
 ```gradle
repositories {
    // ...
    maven { url "https://jitpack.io" }
}
```

Then, add the library dependency:
```gradle
compile 'com.github.badoualy:datepicker-timeline:c6dcd05737'
```

Now go do some awesome stuff!

Usage
----------------

**Warning**: Note that the month value is always between 0 and 11 due to the use of the Calendar API.

### Add the view to your xml
```xml
 <com.github.badoualy.datepicker.DatePickerTimeline
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
```

### Setup the first visible date via the code
```java
timeline.setFirstVisibleDate(2016, Calendar.JULY, 19);
```

### You can also set the limit date
```java
timeline.setLastVisibleDate(2020, Calendar.JULY, 19);
```

### Supply a label adapter to add a label below each date if needed
```java
timeline.setDateLabelAdapter(new MonthView.DateLabelAdapter() {
    @Override   
    public CharSequence getLabel(Calendar calendar, int index) {
        return Integer.toString(calendar.get(Calendar.MONTH) + 1) + "/" + (calendar.get(Calendar.YEAR) % 2000);
    }
});
```

### Set a listener to be notified when the user select a date
```java
timeline.setOnDateSelectedListener(new DatePickerTimeline.OnDateSelectedListener() {
    @Override
    public void onDateSelected(int year, int month, int day, int index) {
                
    }
});
```

### You can the the date manually
```java
timeline.setSelectedDate(2017, Calendar.JULY, 19);
```

Licence
----------------
```
The MIT License (MIT)

Copyright (c) 2016 Yannick Badoual

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

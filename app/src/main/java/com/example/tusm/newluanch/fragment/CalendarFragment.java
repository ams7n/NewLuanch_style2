package com.example.tusm.newluanch.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iboard.tusm.newluanch.R;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Date;

/**
 * Created by tusm on 17/7/4.
 */

public class CalendarFragment extends Fragment {

    MaterialCalendarView widget;
    private View v;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (v == null) {
           v = inflater.inflate(R.layout.fragment_calendar,null);
            widget=(MaterialCalendarView)v.findViewById(R.id.calendarView);

             widget.setSelectedDate(new Date());
             widget.setPagingEnabled(false);
             

            }

            return v;
    }

}

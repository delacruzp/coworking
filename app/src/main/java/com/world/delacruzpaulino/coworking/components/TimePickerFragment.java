package com.world.delacruzpaulino.coworking.components;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by delacruzpaulino on 5/15/16.
 */
public  class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    int viewId;
    String currentDate;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        viewId = getArguments().getInt("VIEW_ID");
        String tempDate = getArguments().getString("CURRENT_VALUE");
        Date currentDate = new SimpleDateFormat("hh:mm a").parse(tempDate,new ParsePosition(0));

        if (currentDate != null){
            // Use the current time as the default values for the picker
            c.setTime(currentDate);
        }

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = 0;


        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour,minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user

        String am_pm = "";

        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        datetime.set(Calendar.MINUTE, minute);

        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";

        Button tempView = (Button)getActivity().findViewById(viewId);
        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";

        String text = new SimpleDateFormat("hh:mm a").format(datetime.getTime());

        tempView.setText(text);

    }
}

package com.george.balasca.articleregistry.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog picker = new DatePickerDialog(getActivity(), this, year, month, day);
        picker.getDatePicker().setMaxDate(c.getTimeInMillis());
        // Create a new instance of DatePickerDialog and return it
        return picker;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {

        Intent intent = new Intent();
        intent.putExtra("key_date", year + "-" + (month + 1) + "-" + day);
        getTargetFragment().onActivityResult(getTargetRequestCode(), getTargetRequestCode(), intent);
    }

}
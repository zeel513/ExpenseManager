package com.example.user1.expensemanager;

import android.support.v4.app.DialogFragment;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;


/**
 * Created by LENOVO on 21-08-2016.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    static Calendar c = Calendar.getInstance();;
    private static String activity_name;
    static DatePickerFragment newInstance(String name)
    {
        DatePickerFragment dpf=new DatePickerFragment();
        dpf.activity_name=name;
        return dpf;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //Log.d("Hello","Hello");
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,day);
        Log.d("Year", String.valueOf(c.get(Calendar.YEAR)));
        //EditText date1  = (EditText)getActivity().findViewById(R.id.income_date);
        //EditText date2 = (EditText)getActivity().findViewById(R.id.expense_date);
        String text = String.valueOf(day) + "-" +  String.valueOf(month+1) + "-" + String.valueOf(year);
        if(activity_name.equals("income"))
        {
            EditText date=(EditText)getActivity().findViewById(R.id.income_date);
            date.setText(text);
        }
        if(activity_name.equals("expense"))
        {
            EditText date=(EditText)getActivity().findViewById(R.id.expense_date);
            date.setText(text);
        }
       /* try
        {
            date2.setText(text);
        }
        catch (Exception ex)
        {
            date1.setText(text);
        }*/

    }
}

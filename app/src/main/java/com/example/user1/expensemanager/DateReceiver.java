package com.example.user1.expensemanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;

/**
 * Created by Admin on 06/10/2016.
 */
public class DateReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar c=Calendar.getInstance();
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=sp.edit();
        editor.putFloat("TODAYS_EXPENSE",(float) 0.0);
        if(c.get(Calendar.DAY_OF_MONTH)==1)
        {
            editor.putFloat("MONTHLY_INCOME",(float) 0.0);
            editor.putFloat("MONTHLY_BALANCE",(float) 0.0);
            editor.putFloat("MONTHLY_EXPENSE",(float) 0.0);
        }
        editor.commit();
    }
}

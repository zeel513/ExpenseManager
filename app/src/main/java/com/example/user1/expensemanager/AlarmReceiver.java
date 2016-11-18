package com.example.user1.expensemanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Admin on 16/11/2016.
 */
public class AlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
     //   Log.d("TEST","in on receive");
        Intent background=new Intent(context,BackgroundService.class);
        context.startService(background);
    }
}

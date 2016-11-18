package com.example.user1.expensemanager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.ServiceConfigurationError;

/**
 * Created by Admin on 16/11/2016.
 */
public class BackgroundService extends Service {
    private boolean isRunning;
    private Context context;
    private Thread backgroundThread;

    int notiId=1;
  //  int noMessages=1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        this.context=this;
        this.isRunning=false;
        this.backgroundThread=new Thread(myTask);
//        Log.d("TEST","oncreate");
    //    SharedPreferences activitySP = PreferenceManager.getDefaultSharedPreferences(this);
    //    noMessages = activitySP.getInt("noMsg",1);
    }
    private Runnable myTask=new Runnable() {
        @Override
        public void run() {
            Log.d("TEST SERVICE","Send Notification");
            sendMyNotification();
            stopSelf();
        }
    };

    @Override
    public void onDestroy() {
        this.isRunning=false;
      //  SharedPreferences activitySP = PreferenceManager.getDefaultSharedPreferences(this);
      //  SharedPreferences.Editor editor = activitySP.edit();
      //  editor.putInt("noMsg",noMessages);
      //  editor.commit();

         //NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
         //notificationManager.cancelAll();
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId)
    {
        if(!this.isRunning)
        {
            this.isRunning=true;
            this.backgroundThread.start();
        }
        return START_STICKY;
    }
    public void sendMyNotification(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean clear = sp.getBoolean("clear",false);
        boolean isAvail=checkBudgets();
        if(!isAvail)
            return;
        if(clear) {
         //   noMessages = 1;
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("clear", false);
            editor.commit();
        }

        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this, budget.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack
        stackBuilder.addNextIntent(new Intent(this,MainActivity.class));
        stackBuilder.addParentStack(budget.class);
        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(intent);
        // Gets a PendingIntent containing the entire back stack
        PendingIntent pIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        // Build notification
        for(ListItem li:items){
            String message=li.getCategory()+" Budget: "
                    +"\nAlert: "+li.getAlert_amt()
                    +"\nExpense: "+li.getExpense()
                    +"\nAmount: "+li.getAmt();
            NotificationCompat.Builder noti = new NotificationCompat.Builder(this)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentTitle("Expense Manager")
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                //.setNumber(noMessages++)
                .setAutoCancel(true)
                .setDefaults(
                        NotificationCompat.DEFAULT_LIGHTS
                                | NotificationCompat.DEFAULT_SOUND
                                | NotificationCompat.DEFAULT_VIBRATE)
                .setTicker("Budget is exceeding");

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(notiId, noti.build());
        }
    }
    ArrayList<ListItem> items;
    public boolean checkBudgets(){
        boolean isAvail=false;
        DatabaseHandler db = new DatabaseHandler(context);
        items = db.getAlertBudgets();
        if(!items.isEmpty())
            isAvail=true;
        return isAvail;
    }
}

package com.example.user1.expensemanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences sp;
    TextView curr_bal1;
    TextView this_month_bal1;
    TextView this_month_income1;
    TextView today_ex1;
    TextView month_ex1;
    TextView total_ex1;
    public double curr_bal=0; //get it from database
    public double mon_bal=0;
    public double mon_income=0;
    public double total_ex=0;
    public double mon_ex=0;
    public double today_ex=0;

    static Calendar c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        curr_bal1=(TextView)findViewById((R.id.curr_bal));
        this_month_bal1=(TextView)findViewById((R.id.monthly_bal));
        this_month_income1=(TextView)findViewById((R.id.this_month_income));
        today_ex1=(TextView)findViewById((R.id.today_ex));
        month_ex1=(TextView)findViewById((R.id.this_month_ex));
        total_ex1=(TextView)findViewById((R.id.total_ex));
    /*    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
   */
        Button inc_button=(Button)findViewById(R.id.button);
        inc_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(),income.class);
                startActivity(intent);
            }
        });
        Button exp_button=(Button)findViewById(R.id.button2);
        exp_button.setOnClickListener(new View.OnClickListener(){
                                          @Override
                                          public void onClick(View view)
                                          {
                                              Intent intent = new Intent(getApplicationContext(),expense.class);
                                              startActivity(intent);
                                          }
                                      }
        );
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor=sp.edit();
        c=Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        curr_bal=sp.getFloat("CURRENT_BALANCE", (float) 0.0);
        mon_bal=sp.getFloat("MONTHLY_BALANCE", (float) curr_bal);
        mon_income=sp.getFloat("MONTHLY_INCOME",(float) 0.0);
        total_ex=sp.getFloat("TOTAL_EXPENSE",(float) 0.0);
        mon_ex=sp.getFloat("MONTHLY_EXPENSE",(float) 0.0);
        today_ex=sp.getFloat("TODAYS_EXPENSE",(float) 0.0);
        int STORED_MONTH=sp.getInt("STORED_MONTH",-1);
        int STORED_DAY=sp.getInt("STORED_DAY",-1);

        curr_bal1.setText(String.valueOf(curr_bal));
        if(STORED_MONTH!=month)
        {
            STORED_MONTH=month;
            STORED_DAY=day;
            editor.putInt("STORED_MONTH",STORED_MONTH);
            editor.putInt("STORED_DAY",day);
            mon_income=0;
            mon_bal=0;
            today_ex=0;
            mon_ex=0;
            editor.putFloat("MONTHLY_BALANCE",(float)0.0);
            editor.putFloat("MONTHLY_INCOME",(float)0.0);
            editor.putFloat("MONTHLY_EXPENSE",(float)0.0);
            editor.putFloat("TODAYS_EXPENSE",(float)0.0);
        }
        this_month_income1.setText(String.valueOf(mon_income));
        this_month_bal1.setText(String.valueOf(mon_bal));
        if(STORED_DAY!=day)
        {
            STORED_DAY=day;
            today_ex=0;
            editor.putInt("STORED_DAY",STORED_DAY);
            editor.putFloat("TODAYS_EXPENSE",(float)0.0);
        }
        editor.putBoolean("clear",true);
        editor.commit();

//        editor.commit();
        today_ex1.setText(String.valueOf(today_ex));
        month_ex1.setText(String.valueOf(mon_ex));
        total_ex1.setText(String.valueOf(total_ex));

   //     IntentFilter filter = new IntentFilter("android.intent.action.DATE_CHANGED");

  //      DateReceiver myReceiver = new DateReceiver();
    //    registerReceiver(myReceiver, filter);

        //alarm for notification

        Intent alarm=new Intent(this,AlarmReceiver.class);
        boolean alarmRunning=(PendingIntent.getBroadcast(this,0,alarm,PendingIntent.FLAG_NO_CREATE)!=null);
        if(alarmRunning==false)
        {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarm, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 15000, pendingIntent);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_budget) {
            Intent intent = new Intent(getApplicationContext(),budget.class);
            startActivity(intent);

        } else if (id == R.id.nav_income) {
            Intent intent = new Intent(getApplicationContext(),income.class);
            startActivity(intent);

        } else if (id == R.id.nav_expense) {
            Intent intent = new Intent(getApplicationContext(),expense.class);
            startActivity(intent);
        } else if (id == R.id.nav_calc) {
            Intent intent = new Intent(getApplicationContext(),Calculator.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

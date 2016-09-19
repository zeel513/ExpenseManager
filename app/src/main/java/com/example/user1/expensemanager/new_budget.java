package com.example.user1.expensemanager;

import android.support.annotation.BoolRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Date;
import java.util.Calendar;
import java.util.StringTokenizer;

public class new_budget extends AppCompatActivity {
    FloatingActionButton add;
    DatabaseHandler handler;
    Date from,to;
    public EditText nb_category;
    public EditText nb_fromdate;
    public EditText nb_todate;
    public EditText nb_amnt;
    public EditText nb_alert;
    DialogFragment newFragment;
    Calendar c,c2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_budget);

        nb_category=(EditText)findViewById(R.id.nb_category);
        nb_fromdate=(EditText)findViewById(R.id.nb_fromdate);
        nb_todate=(EditText)findViewById(R.id.nb_todate);
        nb_amnt=(EditText)findViewById(R.id.nb_amnt);
        nb_alert=(EditText)findViewById(R.id.nb_alrt_amnt);
        add= (FloatingActionButton) findViewById(R.id.nb_add);

        c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String text = String.valueOf(day) + "-" +  String.valueOf(month+1) + "-" + String.valueOf(year);
        nb_fromdate.setText(text);

        c2 = Calendar.getInstance();
        int year2 = c2.get(Calendar.YEAR);
        int month2 = c2.get(Calendar.MONTH);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        String text2 = String.valueOf(day2) + "-" +  String.valueOf(month2+1) + "-" + String.valueOf(year2);
        nb_todate.setText(text2);
    }
    public void showDatePickerDialog(View v){
        if(v.getId()==R.id.nb_fromdate_btn) {
            newFragment = DatePickerFragment.newInstance("new_budget_from");
            newFragment.show(getSupportFragmentManager(), "datePicker");
            /*if(nb_fromdate.getText()!=null)
            {
                String temp = nb_fromdate.getText().toString();
                StringTokenizer st=new StringTokenizer(temp,"-");
                int day=Integer.parseInt(st.nextToken());
                int month=Integer.parseInt(st.nextToken());
                int year=Integer.parseInt(st.nextToken());
                c.set(Calendar.YEAR,year);
                c.set(Calendar.MONTH,month);
                c.set(Calendar.DAY_OF_MONTH,day);
            }*/
        }
        else if(v.getId()==R.id.nb_todate_btn) {
            newFragment = DatePickerFragment.newInstance("new_budget_to");
            newFragment.show(getSupportFragmentManager(), "datePicker");
           /* if(nb_todate.getText()!=null)
            {
                String temp = nb_todate.getText().toString();
                StringTokenizer st=new StringTokenizer(temp,"-");
                int day=Integer.parseInt(st.nextToken());
                int month=Integer.parseInt(st.nextToken());
                int year=Integer.parseInt(st.nextToken());
                c.set(Calendar.YEAR,year);
                c.set(Calendar.MONTH,month);
                c.set(Calendar.DAY_OF_MONTH,day);
            }*/
        }
    }
    public void addBudget(View v) {
        handler=new DatabaseHandler(getApplication());

        String category="food";
        String temp = nb_fromdate.getText().toString();
        StringTokenizer st=new StringTokenizer(temp,"-");
        int day=Integer.parseInt(st.nextToken());
        int month=Integer.parseInt(st.nextToken());
        int year=Integer.parseInt(st.nextToken());
        from=new Date(year-1900,month-1,day);

        String temp2 = nb_todate.getText().toString();
        StringTokenizer st2=new StringTokenizer(temp2,"-");
        int day2=Integer.parseInt(st2.nextToken());
        int month2=Integer.parseInt(st2.nextToken());
        int year2=Integer.parseInt(st2.nextToken());
        to=new Date(year2-1900,month2-1,day2);
        int amnt = Integer.parseInt(String.valueOf(nb_amnt.getText()));
        int alert_amnt = Integer.parseInt(String.valueOf(nb_alert.getText()));

        Boolean done=handler.insertBudget(category,from,to,amnt,alert_amnt);
        if(!done) {
            Toast.makeText(this,"Insertion Unsuccessful",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this,"Insertion Successful",Toast.LENGTH_LONG).show();
        }
    }
}

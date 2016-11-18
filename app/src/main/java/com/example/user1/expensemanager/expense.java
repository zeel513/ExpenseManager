package com.example.user1.expensemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;


import java.util.Calendar;
import java.sql.Date;
import java.util.StringTokenizer;

public class expense extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private String ctgy;
    private String pay_method;

    private EditText exp_date;
    private EditText exp_amt;
    private EditText exp_ref;
    private Spinner exp_ctgy;
    private Spinner exp_pay_method;

    Calendar c;
    DialogFragment newFragment;
    private FloatingActionButton exp_btn_add;
    Date d;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        exp_date = (EditText) findViewById(R.id.expense_date);
        exp_amt = (EditText) findViewById(R.id.expense_amt);
        exp_ctgy = (Spinner) findViewById(R.id.expense_ctgy);
        exp_ctgy.setOnItemSelectedListener(this);
//        exp_ctgy.setOnItemClickListener(this);
        exp_pay_method = (Spinner) findViewById(R.id.expense_pay_method);
        exp_pay_method.setOnItemSelectedListener(this);
        exp_ref = (EditText) findViewById(R.id.expense_rc_no);
        exp_btn_add = (FloatingActionButton)findViewById(R.id.expense_btn_add);

        c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String text = String.valueOf(day) + "-" +  String.valueOf(month+1) + "-" + String.valueOf(year);
        exp_date.setText(text);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.exp_ctgy,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exp_ctgy.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.pay_method,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exp_pay_method.setAdapter(adapter);

    }

    public void showDatePickerDialog(View v){
        newFragment = DatePickerFragment.newInstance("expense");
        newFragment.show(getSupportFragmentManager(),"datePicker");

    }

    public void showCategory (View v) {
        //startActivityForResult();
    }



    public void save(View v) {
        DatabaseHandler dbHandler = new DatabaseHandler(this);
        String temp = exp_date.getText().toString();
        StringTokenizer st=new StringTokenizer(temp,"-");
        int day=Integer.parseInt(st.nextToken());
        int month=Integer.parseInt(st.nextToken());
        int year=Integer.parseInt(st.nextToken());
        d=new Date(year-1900,month-1,day);
        int amt;
        if(exp_amt.getText()==null)
            amt=0;
        else
            amt = Integer.parseInt(String.valueOf(exp_amt.getText()));
        String checkid;
        if(exp_ref.getText()==null)
            checkid="0";
        else
            checkid = String.valueOf(exp_ref.getText());
        pay_method = String.valueOf(exp_pay_method.getSelectedItem());
       // ctgy = String.valueOf(exp_ctgy.getText());
        //Log.d("date",d.toString());
        //Log.d("pay_method",pay_method);
        //Toast.makeText(this,d.toString(),Toast.LENGTH_LONG).show();
        boolean done = dbHandler.insertExpense(d,amt,category,pay_method,checkid);
        //boolean done = dbHandler.insertExpense(d,100,"Food",pay_method,"xyz");
        if(!done) {
            Toast.makeText(this,"Insertion Unsuccessful",Toast.LENGTH_LONG).show();

        }
        else {
            Toast.makeText(this,"Insertion Successful",Toast.LENGTH_LONG).show();
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor=sp.edit();
            float curr_bal=sp.getFloat("CURRENT_BALANCE", (float) 0.0);
            float mon_bal=sp.getFloat("MONTHLY_BALANCE",(float) 0.0);
            float total_ex=sp.getFloat("TOTAL_EXPENSE",(float) 0.0);
            float mon_ex=sp.getFloat("MONTHLY_EXPENSE",(float) 0.0);
            float today_ex=sp.getFloat("TODAYS_EXPENSE",(float) 0.0);

            curr_bal-=amt;
            mon_bal-=amt;

            total_ex +=amt;
            mon_ex +=amt;
            today_ex +=amt;
            editor.putFloat("CURRENT_BALANCE",curr_bal);
            editor.putFloat("MONTHLY_BALANCE",mon_bal);
            editor.putFloat("TOTAL_EXPENSE",total_ex);
            editor.putFloat("MONTHLY_EXPENSE",mon_ex);
            editor.putFloat("TODAYS_EXPENSE",today_ex);

            editor.commit();

        }
        Intent background=new Intent(this,BackgroundService.class);
        startService(background);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Spinner clicked = (Spinner) view;
        switch(view.getId())
        {
            case R.id.expense_pay_method:
                pay_method = exp_pay_method.getSelectedItem().toString();
//                Toast.makeText(this,"in onitemselectedppp"+pay_method,Toast.LENGTH_LONG).show();
                break;
            case R.id.expense_ctgy:
                category=exp_ctgy.getSelectedItem().toString();
  //              Toast.makeText(this,"in onitemselected"+category,Toast.LENGTH_LONG).show();
                break;
            default:
                category=exp_ctgy.getSelectedItem().toString();
                pay_method = exp_pay_method.getSelectedItem().toString();
    //            Toast.makeText(this,"in onitemselectedccc"+category+pay_method,Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(),"in onitemCLICK "+view.getId(),Toast.LENGTH_LONG).show();
        switch(view.getId())
        {
            case R.id.expense_pay_method:
                //        pay_method = exp_pay_method.getSelectedItem().toString();
                pay_method=parent.getItemAtPosition(position).toString();
                Log.d("pay_method",pay_method);
      //          Toast.makeText(getApplicationContext(),"in onitem CLICK"+pay_method,Toast.LENGTH_LONG).show();
                break;
            case R.id.expense_ctgy:
                category=exp_ctgy.getSelectedItem().toString();
        //        Toast.makeText(getApplicationContext(),"in onitem CLICK "+category,Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }
}

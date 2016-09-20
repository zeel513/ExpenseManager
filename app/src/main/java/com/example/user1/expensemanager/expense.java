package com.example.user1.expensemanager;

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

public class expense extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        exp_date = (EditText) findViewById(R.id.expense_date);
        exp_amt = (EditText) findViewById(R.id.expense_amt);
        exp_ctgy = (Spinner) findViewById(R.id.expense_ctgy);
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
        int amt = Integer.parseInt(String.valueOf(exp_amt.getText()));
        String checkid = String.valueOf(exp_ref.getText());
        pay_method = String.valueOf(exp_pay_method.getSelectedItem());
       // ctgy = String.valueOf(exp_ctgy.getText());
        //Log.d("date",d.toString());
        //Log.d("pay_method",pay_method);
        Toast.makeText(this,d.toString(),Toast.LENGTH_LONG).show();
        boolean done = dbHandler.insertExpense(d,amt,"household",pay_method,checkid);
        //boolean done = dbHandler.insertExpense(d,100,"Food",pay_method,"xyz");
        if(!done) {
            Toast.makeText(this,"Insertion Unsuccessful",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this,"Insertion Successful",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Spinner clicked = (Spinner) view;
       /* Toast.makeText(getApplicationContext(),"in onitemselected",Toast.LENGTH_LONG).show();
        switch(view.getId())
        {
            case R.id.expense_pay_method:
                pay_method = String.valueOf(exp_pay_method.getSelectedItem());
                break;
            default:
                break;
        }*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

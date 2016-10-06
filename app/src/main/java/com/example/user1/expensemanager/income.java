package com.example.user1.expensemanager;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.IntegerRes;
import android.support.annotation.StringDef;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.DialogFragment;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.sql.Date;
import java.util.StringTokenizer;

public class income extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

   /* private ImageButton income_date_btn;
    private ImageButton income_amt_btn;
*/
    private String category;
    private String payer;
    private String pay_method;

    private EditText income_date;
    private EditText income_amt;
    private EditText income_ref;
    private Spinner income_payer;
    private Spinner income_ctgy;
    private Spinner income_pay_method;
    Calendar c;
    DialogFragment newFragment;
    private FloatingActionButton income_btn_add;
    Date d;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
       /* income_date_btn = (ImageButton)findViewById(R.id.income_date_btn);
        income_amt_btn = (ImageButton)findViewById(R.id.income_amt_btn);
*/
        income_date = (EditText) findViewById(R.id.income_date);
        income_amt = (EditText) findViewById(R.id.income_amt);
        income_payer = (Spinner) findViewById(R.id.income_payer);
        income_payer.setOnItemSelectedListener(this);
        income_ctgy = (Spinner) findViewById(R.id.income_ctgy);
        income_ctgy.setOnItemSelectedListener(this);
        income_pay_method = (Spinner) findViewById(R.id.income_pay_method);
        income_pay_method.setOnItemSelectedListener(this);
        income_ref = (EditText) findViewById(R.id.income_rc_no);
        income_btn_add = (FloatingActionButton)findViewById(R.id.income_btn_add);

        c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String text = String.valueOf(day) + "-" +  String.valueOf(month+1) + "-" + String.valueOf(year);
        income_date.setText(text);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.inc_ctgy,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        income_ctgy.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.inc_payer,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        income_payer.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.pay_method,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        income_pay_method.setAdapter(adapter2);
    }

    public void showDatePickerDialog(View v){
        newFragment = DatePickerFragment.newInstance("income");
        newFragment.show(getSupportFragmentManager(),"datePicker");
        /*Calendar c = DatePickerFragment.getCalendar();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String text = String.valueOf(day) + "-" +  String.valueOf(month) + "-" + String.valueOf(year);
        income_date.setText(text);*/

    }

    public void save(View v) {
        DatabaseHandler dbHandler = new DatabaseHandler(this);
        Float amt = Float.parseFloat(String.valueOf(income_amt.getText()));
        String temp = income_date.getText().toString();
        StringTokenizer st=new StringTokenizer(temp,"-");
        int day=Integer.parseInt(st.nextToken());
        int month=Integer.parseInt(st.nextToken());
        int year=Integer.parseInt(st.nextToken());
        d=new Date(year-1900,month-1,day);

        String checkid = String.valueOf(income_ref.getText());

        boolean done = dbHandler.insertIncome(d,amt,payer,category,pay_method,checkid);
        if(done) {
            Toast.makeText(this,"Insertion successful",Toast.LENGTH_LONG).show();

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor=sp.edit();
            float curr_bal=sp.getFloat("CURRENT_BALANCE", (float) 0.0);
            float mon_bal=sp.getFloat("MONTHLY_BALANCE",(float) 0.0);
            float mon_income=sp.getFloat("MONTHLY_INCOME",(float) 0.0);
            curr_bal+=amt;
            mon_bal+=amt;
            mon_income+=amt;
            editor.putFloat("CURRENT_BALANCE",curr_bal);
            Log.d("curr bal",String.valueOf(curr_bal));
            editor.putFloat("MONTHLY_BALANCE",mon_bal);
            editor.putFloat("MONTHLY_INCOME",mon_income);
            editor.commit();

        }
        else {
            Toast.makeText(this,"Insertion Unsuccessful",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      //  Spinner clicked = (Spinner) view;
        switch(view.getId())
        {
            case R.id.income_ctgy:
                category = String.valueOf(income_ctgy.getSelectedItem());
                break;
            case R.id.income_pay_method:
                pay_method = String.valueOf(income_pay_method.getSelectedItem());
                break;

            case R.id.income_payer:
                payer = String.valueOf(income_payer.getSelectedItem());
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

package com.example.user1.expensemanager;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.net.sip.SipRegistrationListener;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.StringTokenizer;


/**
 * Created by LENOVO on 21-08-2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper{

    public static final String DB_NAME = "em_db";
    public static final String INC_TABLE = "Income";
    public static final String INC_COLUMN_DATE = "Date";
    public static final String INC_COLUMN_AMOUNT = "Amount";
    public static final String INC_COLUMN_PAYER ="Payer";
    public static final String INC_COLUMN_CATEGORY = "Category";
    public static final String INC_COLUMN_PAY_METHOD = "PAY_METHOD";
    public static final String INC_COLUMN_CHECK_ID = "Check_id";
    public static final int DATABASE_VERSION = 1;

    public static final String EXP_TABLE = "Expense";
    public static final String EXP_COLUMN_DATE = "Date";
    public static final String EXP_COLUMN_AMOUNT = "Amount";
    public static final String EXP_COLUMN_CATEGORY = "Category";
    public static final String EXP_COLUMN_PAY_METHOD = "PAY_METHOD";
    public static final String EXP_COLUMN_CHECK_ID = "Check_id";

    public static final String BUDGET_TABLE="Budget";
    public static final String BUDGET_COLUMN_FROM="Fromdate";
    public static final String BUDGET_COLUMN_TO="Todate";
    public static final String BUDGET_COLUMN_AMOUNT="Amount";
    public static final String BUDGET_COLUMN_ALERT="Alert_Amount";
    public static final String BUDGET_COLUMN_EXP="Expense";
    public static final String BUDGET_COLUMN_CATEGORY="Category";

    public DatabaseHandler(Context context) {
        super(context,DB_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createIncomeQuery = "create table " + INC_TABLE+ " (" + INC_COLUMN_DATE + " date, " + INC_COLUMN_AMOUNT +
                " float," + INC_COLUMN_PAYER + " text," + INC_COLUMN_CATEGORY + " text," + INC_COLUMN_PAY_METHOD +
                " text," + INC_COLUMN_CHECK_ID + " text)";
        db.execSQL(createIncomeQuery);
        String createExpeneQuery = "create table " + EXP_TABLE + " (" + EXP_COLUMN_DATE + " date, " + EXP_COLUMN_AMOUNT +
                " integer," + EXP_COLUMN_CATEGORY + " text," + EXP_COLUMN_PAY_METHOD + " text," + EXP_COLUMN_CHECK_ID +
                " text)";
        db.execSQL(createExpeneQuery);
        String createBudgetQuery="create table " + BUDGET_TABLE + " ( " + BUDGET_COLUMN_FROM + " date, " + BUDGET_COLUMN_TO +
                " date, " + BUDGET_COLUMN_AMOUNT + " integer," + BUDGET_COLUMN_ALERT + " integer, " +BUDGET_COLUMN_EXP +
                " integer," + BUDGET_COLUMN_CATEGORY + " text)";
        db.execSQL(createBudgetQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropIncQuery = "DROP TABLE IF EXISTS " + INC_TABLE;
        db.execSQL(dropIncQuery);

        String dropExpQuery = "DROP TABLE IF EXISTS " + EXP_TABLE;
        db.execSQL(dropExpQuery);

        String dropBudgetQuery = "DROP TABLE IF EXISTS " + BUDGET_TABLE;
        db.execSQL(dropBudgetQuery);
    }

    public boolean insertIncome(Date date , Float amount, String payer, String category, String pay_method, String check_id){

        boolean done = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        contentValues.put(INC_COLUMN_DATE,date.toString());
        contentValues.put(INC_COLUMN_AMOUNT,amount);
        contentValues.put(INC_COLUMN_PAYER,payer);
        contentValues.put(INC_COLUMN_CATEGORY,category);
        contentValues.put(INC_COLUMN_PAY_METHOD,pay_method);
        contentValues.put(INC_COLUMN_CHECK_ID,check_id);

        long id = db.insert(INC_TABLE,null,contentValues);// For time being it is null it should not be though.
        if (id > 0) {
            done = true;
        }
        return done;
    }

    public boolean insertExpense(Date date , int amount, String category, String pay_method, String check_id){
        boolean done = false;
        SQLiteDatabase db = this.getWritableDatabase();

        boolean flag = updateBudget(amount,category,date);

        ContentValues contentValues = new ContentValues();
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");

        contentValues.put(EXP_COLUMN_DATE,date.toString());
        contentValues.put(EXP_COLUMN_AMOUNT,amount);
        contentValues.put(EXP_COLUMN_CATEGORY,category);
        contentValues.put(EXP_COLUMN_PAY_METHOD,pay_method);
        contentValues.put(EXP_COLUMN_CHECK_ID,check_id);
        long id = 0;
        id = db.insert(EXP_TABLE,null,contentValues);// For time being it is null it should not be though
        if (id > 0)
            done = true;
        return done;
    }
    public boolean insertBudget(String category,Date from,Date to,int amt,int alert_amt)
    {
        boolean done = false;
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
       // Log.d("to","to"+to.toString());
       // Log.d("from","from"+from.toString());
        contentValues.put(BUDGET_COLUMN_FROM,from.toString());
        contentValues.put(BUDGET_COLUMN_TO,to.toString());
//        Log.d("to","to"+contentValues.getAsString(BUDGET_COLUMN_TO));
        //Log.d("from","from"+from.toString());
        contentValues.put(BUDGET_COLUMN_CATEGORY,category);
        contentValues.put(BUDGET_COLUMN_AMOUNT,amt);
        contentValues.put(BUDGET_COLUMN_ALERT,alert_amt);
        contentValues.put(BUDGET_COLUMN_EXP,0);
        Log.d("BUDGET","adding budget "+category);
        long id = 0;
        id = db.insert(BUDGET_TABLE,null,contentValues);// For time being it is null it should not be though
        if (id > 0)
            done = true;
        return done;
    }

    public boolean updateBudget(int amount,String category,Date date){
        Cursor cursor;
        String d=date.toString();

        SQLiteDatabase db = getReadableDatabase();
        String selectionClause = BUDGET_COLUMN_CATEGORY+" = ? AND " + BUDGET_COLUMN_TO + " >= ? AND "
                + BUDGET_COLUMN_FROM + "<= ?";
        String [] selectionArgs= new String[] {category,d,d};

      /*  String query = "select * from " + BUDGET_TABLE+ " where " +BUDGET_COLUMN_CATEGORY+" ="+
                category +" AND" +date+ " <= " + BUDGET_COLUMN_TO + " AND "+
                date + " >= "+ BUDGET_COLUMN_FROM;;*/
        //Log.d("hmm","we have found something.........");
        try {
            cursor = db.query(BUDGET_TABLE,null,selectionClause,selectionArgs,null,null,null);
        }
        catch (Exception ex){
            Log.d("X",ex.toString());

        }
        cursor = db.query(BUDGET_TABLE,null,selectionClause,selectionArgs,null,null,null);
        int exp=0,alert=0;
        if (cursor.moveToFirst())
        {
            //Log.d("hmm","we have found something");
            exp = cursor.getInt((cursor.getColumnIndex(BUDGET_COLUMN_EXP)));
            exp += amount;
            alert = cursor.getInt((cursor.getColumnIndex(BUDGET_COLUMN_ALERT)));

            ContentValues cv = new ContentValues();
            cv.put(BUDGET_COLUMN_EXP,exp);
            int x = db.update(BUDGET_TABLE,cv,selectionClause,selectionArgs);
            if (x>0)
            {
              //  Log.d("hmm","we have found "+x);
                if(exp >= alert)
                {
                    //Notification
                }
                return true;
            }

        }

        return false;

    }
    public ArrayList<ListItem> getBudgets(String category)
    {
        ArrayList<ListItem> items=new ArrayList<ListItem>();
        Cursor cursor;
        SQLiteDatabase db = getReadableDatabase();
        long longDate = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = sdf.format(longDate);
        StringTokenizer st=new StringTokenizer(dateString,"-");
        int day=Integer.parseInt(st.nextToken());
        int month=Integer.parseInt(st.nextToken());
        int year=Integer.parseInt(st.nextToken());
        Date today=new Date(year-1900,month-1,day);
       // Log.d("DATE",today.toString());

        String selectionClause = BUDGET_COLUMN_CATEGORY+" =? AND "+BUDGET_COLUMN_TO+" >= "+today;
        String [] selectionArgs= new String[] {category};
        cursor = db.query(BUDGET_TABLE,null,selectionClause,selectionArgs,null,null,null);
        ListItem temp=new ListItem();
        while(cursor.moveToNext())
        {
            temp.setAmt(cursor.getFloat(cursor.getColumnIndex(BUDGET_COLUMN_AMOUNT)));
            temp.setAlert_amt(cursor.getFloat(cursor.getColumnIndex(BUDGET_COLUMN_ALERT)));
            temp.setExpense(cursor.getFloat(cursor.getColumnIndex(BUDGET_COLUMN_EXP)));
            temp.setFromdate(cursor.getString(cursor.getColumnIndex(BUDGET_COLUMN_FROM)));
            temp.setTodate(cursor.getString(cursor.getColumnIndex(BUDGET_COLUMN_TO)));
            temp.setCategory(category);
         //   Log.d("DATE","to"+temp.getTodate());
         //   Log.d("from","from"+temp.getFromdate());
            temp.setProgressVal((int) ((temp.getExpense()/temp.getAmt()) *100));
            items.add(temp);
        }
        return items;
    }
    public ArrayList<ListItem> getAlertBudgets()
    {
        ArrayList<ListItem> items=new ArrayList<ListItem>();
        Cursor cursor;
        SQLiteDatabase db = getReadableDatabase();
        long longDate = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = sdf.format(longDate);
        StringTokenizer st=new StringTokenizer(dateString,"-");
        int day=Integer.parseInt(st.nextToken());
        int month=Integer.parseInt(st.nextToken());
        int year=Integer.parseInt(st.nextToken());
        Date today=new Date(year-1900,month-1,day);
        String selectionClause = BUDGET_COLUMN_EXP+" >= "+BUDGET_COLUMN_ALERT
                +" AND "+BUDGET_COLUMN_AMOUNT+" >= "+BUDGET_COLUMN_EXP+" AND "+BUDGET_COLUMN_TO+" >= "+today;;

        cursor = db.query(BUDGET_TABLE,null,selectionClause,null,null,null,null);
        ListItem temp=new ListItem();
        while(cursor.moveToNext())
        {
            temp.setAmt(cursor.getFloat(cursor.getColumnIndex(BUDGET_COLUMN_AMOUNT)));
            temp.setAlert_amt(cursor.getFloat(cursor.getColumnIndex(BUDGET_COLUMN_ALERT)));
            temp.setExpense(cursor.getFloat(cursor.getColumnIndex(BUDGET_COLUMN_EXP)));
            temp.setFromdate(cursor.getString(cursor.getColumnIndex(BUDGET_COLUMN_FROM)));
            temp.setTodate(cursor.getString(cursor.getColumnIndex(BUDGET_COLUMN_TO)));
            temp.setCategory(cursor.getString(cursor.getColumnIndex(BUDGET_COLUMN_CATEGORY)));
           // Log.d("to","to"+temp.getTodate());
           // Log.d("from","from"+temp.getFromdate());
            temp.setProgressVal((int) ((temp.getExpense()/temp.getAmt()) *100));
            items.add(temp);
        }
        return items;
    }
}

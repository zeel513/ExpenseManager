package com.example.user1.expensemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

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

    public DatabaseHandler(Context context) {
        super(context,DB_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createIncomeQuery = "create table " + INC_TABLE+ " (" + INC_COLUMN_DATE + " date," + INC_COLUMN_AMOUNT +
                " integer," + INC_COLUMN_PAYER + " text," + INC_COLUMN_CATEGORY + " text," + INC_COLUMN_PAY_METHOD +
                " text," + INC_COLUMN_CHECK_ID + " text)";
        db.execSQL(createIncomeQuery);
        String createExpeneQuery = "create table " + EXP_TABLE + " (" + EXP_COLUMN_DATE + " date," + EXP_COLUMN_AMOUNT +
                " integer," + EXP_COLUMN_CATEGORY + " text," + EXP_COLUMN_PAY_METHOD + " text," + EXP_COLUMN_CHECK_ID +
                " text)";
        db.execSQL(createExpeneQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropIncQuery = "DROP TABLE IF EXISTS " + INC_TABLE;
        db.execSQL(dropIncQuery);

        String dropExpQuery = "DROP TABLE IF EXISTS " + EXP_TABLE;
        db.execSQL(dropExpQuery);
    }

    public boolean insertIncome(Date date , int amount, String payer, String category, String pay_method, String check_id){
        boolean done = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        contentValues.put(INC_COLUMN_DATE,sdf.format(date));
        contentValues.put(INC_COLUMN_AMOUNT,amount);
        contentValues.put(INC_COLUMN_PAYER,payer);
        contentValues.put(INC_COLUMN_CATEGORY,category);
        contentValues.put(INC_COLUMN_PAY_METHOD,pay_method);
        contentValues.put(INC_COLUMN_CHECK_ID,check_id);

        long id = db.insert(INC_TABLE,null,contentValues);// For time being it is null it should not be though.
        if (id > 0)
            done = true;
        return done;
    }

    public boolean insertExpense(Date date , int amount, String category, String pay_method, String check_id){
        boolean done = false;
       // Toast.makeText(this,"hekki",Toast.LENGTH_LONG).show();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");

        contentValues.put(EXP_COLUMN_DATE,sdf.format(date));
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
}

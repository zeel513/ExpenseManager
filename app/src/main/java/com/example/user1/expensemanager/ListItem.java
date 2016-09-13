package com.example.user1.expensemanager;

/**
 * Created by Admin on 13/09/2016.
 */
public class ListItem {
    private float amt;
    private float alert_amt;
    private String fromdate;
    private String todate;
    private int progressVal;

    public float getAmt() {
        return amt;
    }

    public void setAmt(float amt) {
        this.amt = amt;
    }

    public float getAlert_amt() {
        return alert_amt;
    }

    public void setAlert_amt(float alert_amt) {
        this.alert_amt = alert_amt;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public int getProgressVal() {
        return progressVal;
    }

    public void setProgressVal(int progressVal) {
        this.progressVal = progressVal;
    }
}

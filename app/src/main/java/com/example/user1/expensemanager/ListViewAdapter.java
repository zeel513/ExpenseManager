package com.example.user1.expensemanager;

/**
 * Created by zeelsoni_11 on 13/09/16.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    private Context context;
    private ArrayList<ListItem> items;
    private LayoutInflater inflater;
	
    public ListViewAdapter(Context context, ArrayList<ListItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {        
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Declare Variables
        ProgressBar pbar;
        TextView from;
        TextView to;
        TextView amt;
        TextView alert_amt;
        TextView exp;

        View itemView;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null) 
            itemView = inflater.inflate(R.layout.list_item_layout, parent, false);                    
        else
            itemView = convertView;




        // Locate the TextViews in list_item_layout.xml
        from = (TextView) itemView.findViewById(R.id.from);
        to = (TextView) itemView.findViewById(R.id.to);
        amt = (TextView) itemView.findViewById(R.id.amt);
        alert_amt = (TextView) itemView.findViewById(R.id.alert_amt);
        pbar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        exp=(TextView)itemView.findViewById(R.id.bud_exp);
        // Capture position and set to the TextViews
        from.setText(items.get(position).getFromdate());
        to.setText(items.get(position).getTodate());
        amt.setText(String.valueOf(items.get(position).getAmt()));
        alert_amt.setText(String.valueOf(items.get(position).getAlert_amt()));
        pbar.setProgress(items.get(position).getProgressVal());
        exp.setText(String.valueOf(items.get(position).getExpense()));

        return itemView;
    }

}

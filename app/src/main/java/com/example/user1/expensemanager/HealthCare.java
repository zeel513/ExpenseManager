package com.example.user1.expensemanager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link HealthCare#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HealthCare extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView list;
    ListViewAdapter adapter;
    ArrayList<ListItem> items;
    Context context;

    public HealthCare() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HealthCare.
     */
    // TODO: Rename and change types and number of parameters
    public static HealthCare newInstance(String param1, String param2) {
        HealthCare fragment = new HealthCare();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_household, container, false);
        list = (ListView) view.findViewById(R.id.household_list);

        /*items = new ArrayList<ListItem>();
        ListItem my = new ListItem();
        my.setTodate("B4");
        my.setFromdate("Now");
        my.setAlert_amt(100);
        my.setAmt(1000);my.setExpense(100);
        my.setProgressVal((int) ((my.getExpense()/my.getAmt()) *100));
        items.add(my);
        */
        DatabaseHandler db = new DatabaseHandler(context);
        items = db.getBudgets("Healthcare");
        adapter = new ListViewAdapter(context, items);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        // Capture ListView item click
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ListItem selectedItem = items.get(position);
                // Toast.makeText(getActivity(), "App : " + selectedItem.getFromdate() + " selected",
                //         Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

}

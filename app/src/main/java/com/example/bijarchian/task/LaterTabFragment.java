package com.example.bijarchian.task;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by amir on 9/1/17.
 */

public class LaterTabFragment extends Fragment {


    TextView pricetxt, maxstaytxt, fromtimetxt, totimetxt, titletxt, noparkingtxt;
    Bundle bundle;
    LinearLayout mlayout, noparking_layout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabitem_later_fragment, container, false);
        bundle = getArguments();
        init(view, bundle);
        return view;

    }

    //Initilizing content of Later Tab
    private void init(View v, Bundle data) {

        noparking_layout = (LinearLayout) v.findViewById(R.id.forbiden_linear_layout);
        mlayout = (LinearLayout) v.findViewById(R.id.later_linear_layout);
        noparkingtxt = (TextView) v.findViewById(R.id.forbidentxt);
        titletxt = (TextView) v.findViewById(R.id.title);
        pricetxt = (TextView) v.findViewById(R.id.price);
        maxstaytxt = (TextView) v.findViewById(R.id.maxstay);
        fromtimetxt = (TextView) v.findViewById(R.id.fromtime);
        totimetxt = (TextView) v.findViewById(R.id.totime);
        ////////////////////////////////////////////
        try {
            if (data.getString("ftype").equals("notallow")) {
                noparking_layout.setVisibility(View.VISIBLE);
                mlayout.setVisibility(View.GONE);
                noparkingtxt.setText("You are not allowed to park here");
                titletxt.setText(data.getString("ftitle"));
                fromtimetxt.setText(data.getString("ffromtime"));
                totimetxt.setText(data.getString("ftotime"));
            } else {
                noparking_layout.setVisibility(View.GONE);
                mlayout.setVisibility(View.VISIBLE);
                pricetxt.setText(data.getDouble("fprice")+ " EUR");
                titletxt.setText(data.getString("ftitle"));
                maxstaytxt.setText("Max "+data.getInt("fmaxstay"));
                fromtimetxt.setText(data.getString("ffromtime"));
                totimetxt.setText(data.getString("ftotime"));


            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }
}
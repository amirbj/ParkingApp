package com.example.bijarchian.task;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by amir on 8/31/17.
 */

public class NowTabFragment extends Fragment {

    TextView pricetxt, chargetxt, fromtimetxt, totimetxt, titletxt, noparkingtxt;
    Bundle bundle;
    LinearLayout mlayout, noparking_lay;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabitem_now_fragment, container, false);
        bundle = getArguments();
        init(view, bundle);
        return view;

    }

    private void init(View v, Bundle data) {
        noparking_lay= (LinearLayout) v.findViewById(R.id.forbiden_linear_layout);
        mlayout = (LinearLayout) v.findViewById(R.id.now_linear_layout);
        noparkingtxt = (TextView) v.findViewById(R.id.forbidentxt);
        titletxt = (TextView) v.findViewById(R.id.title);
        pricetxt = (TextView) v.findViewById(R.id.price);
        chargetxt = (TextView) v.findViewById(R.id.charge);
        fromtimetxt = (TextView) v.findViewById(R.id.fromtime);
        totimetxt = (TextView) v.findViewById(R.id.totime);
        ////////////////////////////////////////////

        try {
            if (data.getString("type").equals("notallow")) {
                noparking_lay.setVisibility(View.VISIBLE);
                mlayout.setVisibility(View.GONE);
                noparkingtxt.setText("You are not allowed to park here");
                titletxt.setText(data.getString("title"));
                fromtimetxt.setText(data.getString("fromtime"));
                totimetxt.setText(data.getString("totime"));
            } else {
                noparking_lay.setVisibility(View.GONE);
                mlayout.setVisibility(View.VISIBLE);
                pricetxt.setText(data.getDouble("price")+ " EUR");
                titletxt.setText(data.getString("title"));
                fromtimetxt.setText(data.getString("fromtime"));
                totimetxt.setText(data.getString("totime"));
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

    }
}

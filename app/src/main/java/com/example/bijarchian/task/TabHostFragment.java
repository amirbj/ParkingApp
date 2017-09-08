package com.example.bijarchian.task;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.parkbob.models.GeoSpace;
import com.parkbob.models.RulesContext;
import com.parkbob.models.TrafficRule;

import java.util.List;

import static com.example.bijarchian.task.MainActivity.rulesContext;

/**
 * Created by amir on 9/2/17.
 */

public class TabHostFragment extends Fragment {
    FragmentTabHost mTabhost;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tabhost_layout, container, false);
        initTab(view);


        return view;
    }

    private void initTab(View v) {

    mTabhost = (FragmentTabHost) v.findViewById(R.id.tabhost);
        Bundle bundle = getArguments();
        TabHost.TabSpec tab1 = mTabhost.newTabSpec("NOW").setIndicator("NOW");
        TabHost.TabSpec tab2 = mTabhost.newTabSpec("LATER").setIndicator("LATER");
        mTabhost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);
        mTabhost.addTab(tab1 ,  NowTabFragment.class, bundle);
        mTabhost.addTab(tab2, LaterTabFragment.class, bundle);


    }



    public static Fragment newInstance(int pos){
       Bundle bundle = getBundle(pos);
        TabHostFragment fragment = new TabHostFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    private static Bundle getBundle(int pos){
        Bundle bundle = new Bundle();
        TrafficRule currentRule = rulesContext.getGeoSpaceList().get(pos).getCurrentTrafficRule();

    try {
        //put current rule data in bundle
        if (!currentRule.getType().name().equals("NO_PARKING")) {
                bundle.putString("type", "allow");
                    bundle.putDouble("price", currentRule.getParkingCost().get(0).getPrice());
                bundle.putInt("maxstay", currentRule.getMaxStay());
                bundle.putString("title", currentRule.getRuleName().toString());
                bundle.putString("fromtime", currentRule.getStartTime().toString());
                bundle.putString("totime", currentRule.getEndTime().toString());
            } else {
                bundle.putString("type", "notallow");
                bundle.putString("title", currentRule.getRuleName().toString());

            }

    } catch (NullPointerException e){
        e.printStackTrace();
    }


     try {
         //put future rule data in bundle
         TrafficRule futureRule = rulesContext.getGeoSpaceList().get(pos).getFutureTrafficeRule();


             if (futureRule.getType().name().equals("NO_PARKING")) {

                 bundle.putString("ftype", "allow");
                     bundle.putDouble("fprice", futureRule.getParkingCost().get(0).getPrice());
                 bundle.putString("ftitle", futureRule.getRuleName());
                 bundle.putInt("fmaxstay", futureRule.getMaxStay());

                 bundle.putString("ffromtime", futureRule.getStartTime().toString());

                 bundle.putString("ftotime", futureRule.getEndTime().toString());

             } else {
                 bundle.putString("ftype", "notallow");
                 bundle.putString("ftitle", futureRule.getRuleName().toString());

             }

     }catch (NullPointerException e){
         e.printStackTrace();
     }

        return  bundle;
    }


}

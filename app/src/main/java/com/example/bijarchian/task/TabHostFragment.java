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

import com.parkbob.models.RulesContext;

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


    try {
        //put current rule data in bundle
        if (!rulesContext.getGeoSpaceList().get(pos).getCurrentTrafficRule().getType().name().equals("NO_PARKING")) {
                bundle.putString("type", "allow");
                    bundle.putDouble("price", rulesContext.getGeoSpaceList().get(pos).getCurrentTrafficRule().getParkingCost().get(1).getPrice());
                bundle.putString("title", rulesContext.getGeoSpaceList().get(pos).getCurrentTrafficRule().getRuleName().toString());
                bundle.putString("fromtime", rulesContext.getGeoSpaceList().get(pos).getCurrentTrafficRule().getStartTime().toString());
                bundle.putString("totime", rulesContext.getGeoSpaceList().get(pos).getCurrentTrafficRule().getEndTime().toString());
            } else {
                bundle.putString("type", "notallow");
                bundle.putString("title", rulesContext.getGeoSpaceList().get(pos).getCurrentTrafficRule().getRuleName().toString());

            }

    } catch (NullPointerException e){
        e.printStackTrace();
    }


     try {
         //put future rule data in bundle


             if (!rulesContext.getGeoSpaceList().get(pos).getFutureTrafficeRule().getType().name().equals("NO_PARKING")) {

                 bundle.putString("ftype", "allow");
                     bundle.putDouble("fprice", rulesContext.getGeoSpaceList().get(pos).getFutureTrafficeRule().getParkingCost().get(1).getPrice());
                 bundle.putString("ftitle", rulesContext.getGeoSpaceList().get(pos).getFutureTrafficeRule().getRuleName());

                 bundle.putString("ffromtime", rulesContext.getGeoSpaceList().get(pos).getFutureTrafficeRule().getStartTime().toString());

                 bundle.putString("ftotime", rulesContext.getGeoSpaceList().get(pos).getFutureTrafficeRule().getEndTime().toString());

             } else {
                 bundle.putString("ftype", "notallow");
                 bundle.putString("ftitle", rulesContext.getGeoSpaceList().get(pos).getFutureTrafficeRule().getRuleName().toString());

             }

     }catch (NullPointerException e){
         e.printStackTrace();
     }

        return  bundle;
    }


}

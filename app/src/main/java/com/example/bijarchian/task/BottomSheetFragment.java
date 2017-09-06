package com.example.bijarchian.task;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TableLayout;

import com.parkbob.models.RulesContext;

import static com.example.bijarchian.task.MainActivity.rulesContext;

/**
 * Created by amir on 9/2/17.
 */

public class BottomSheetFragment extends Fragment {
    TabLayout mTablyout;
    ViewPager mPager;
    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_content, container, false);
        bundle = getArguments();
        initTab(bundle, view);
        return view;
    }

    private void initTab(Bundle bundle, View v) {

       mTablyout = (TabLayout) v.findViewById(R.id.tablayout);

        mPager = (ViewPager) v.findViewById(R.id.pager);
        PagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(), bundle);
        mPager.setAdapter(adapter);
        mPager.setOffscreenPageLimit(rulesContext.getGeoSpaceList().size());
        mTablyout.setupWithViewPager(mPager);

    }
}

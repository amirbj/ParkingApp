package com.example.bijarchian.task;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.parkbob.models.RulesContext;

import java.util.ArrayList;
import java.util.List;

import static com.example.bijarchian.task.MainActivity.rulesContext;

/**
 * Created by amir on 9/1/17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    Bundle bundle;
    Context context;
    FragmentTabHost mTabhost;
    FragmentManager fm;
    List<View> views = new ArrayList<View>();

    public ViewPagerAdapter(FragmentManager fm, Bundle bundle) {
        super(fm);
        this.fm = fm;
        this.bundle = bundle;
    }





    @Override
    public Fragment getItem(int position) {


        return TabHostFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return rulesContext.getGeoSpaceList().size();
    }
}



package com.example.bijarchian.task;

import android.app.Application;

import com.parkbob.ParkbobManager;

/**
 * Created by Bijarchian on 8/29/2017.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParkbobManager.initInstance(this);
    }
}

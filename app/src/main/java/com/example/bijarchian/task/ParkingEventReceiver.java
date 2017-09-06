package com.example.bijarchian.task;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.parkbob.ParkbobConfiguration;
import com.parkbob.backend.entity.ParkingSpot;
import com.parkbob.models.RulesContext;

/**
 * Created by Bijarchian on 8/29/2017.
 */

public class ParkingEventReceiver extends WakefulBroadcastReceiver {
    RulesContext rulesContext;


    @Override
    public void onReceive(Context context, Intent intent) {
        //Information about nearby parking regulations.

        if (intent.hasExtra(ParkbobConfiguration.RECEIVER_PARKING_EVENT_OCCURRED_EXTRA_RULES_CONTEXT)) {
            rulesContext = (RulesContext) intent.getSerializableExtra(ParkbobConfiguration.RECEIVER_PARKING_EVENT_OCCURRED_EXTRA_RULES_CONTEXT);

            Log.e("here", rulesContext.toString());
        }
        //Information about the parked car.

       /* if (intent.hasExtra(ParkbobConfiguration.RECEIVER_PARKING_EVENT_OCCURRED_EXTRA_PARKING_SPOT)) {
            ParkingSpot parkingSpot = (ParkingSpot) intent.getSerializableExtra(ParkbobConfiguration.RECEIVER_PARKING_EVENT_OCCURRED_EXTRA_PARKING_SPOT);
            Log.e("here" ,parkingSpot.toString()); */
    }


}



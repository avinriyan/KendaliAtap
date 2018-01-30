package com.larapin.kendaliatap.scheduler;

import android.content.Context;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;

/**
 * Created by asus on 25/01/2018.
 */

public class SchedulerTask {
    private GcmNetworkManager mGcmNetworkManager;
    public SchedulerTask(Context context){
        mGcmNetworkManager = GcmNetworkManager.getInstance(context);
    }
    public void createPeriodicTask() {
        Task periodicTask = new PeriodicTask.Builder()
                .setService(SchedulerService.class)
                .setPeriod(60)
                .setFlex(10)
                .setTag(SchedulerService.TAG_TASK_WEATHER_LOG)
                .setPersisted(true)
                .build();
        mGcmNetworkManager.schedule(periodicTask);
    }
    public void cancelPeriodicTask(){
        if (mGcmNetworkManager != null){
            mGcmNetworkManager.cancelTask(SchedulerService.TAG_TASK_WEATHER_LOG, SchedulerService.class);
        }
    }
}

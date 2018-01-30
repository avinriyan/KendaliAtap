package com.larapin.kendaliatap.scheduler;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.larapin.kendaliatap.R;
import com.larapin.kendaliatap.activity.MainActivity;
import com.larapin.kendaliatap.preference.UserPreference;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by asus on 25/01/2018.
 */

public class SchedulerService extends GcmTaskService {
    public static final String TAG = "GetWeather";
    public static String TAG_TASK_WEATHER_LOG = "WeatherTask";
    String host;
    UserPreference userPreference;
    @Override
    public int onRunTask(TaskParams taskParams) {
        int result = 0;
        userPreference = new UserPreference(this);
        if (taskParams.getTag().equals(TAG_TASK_WEATHER_LOG)){
            getCurrentWeather();
            result = GcmNetworkManager.RESULT_SUCCESS;
        }
        return result;
    }

    private void getCurrentWeather() {
        Log.d("GetWeather", "Running");
        host = userPreference.getHost();
        SyncHttpClient client = new SyncHttpClient();
        String url = "http://"+host+"/hujan/select.php";
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.d(TAG, result);
                try {
                    JSONObject responseObject = new JSONObject(result);
                    String cuaca = responseObject.getJSONArray("data").getJSONObject(0).getString("cuaca");
                    String suhu = responseObject.getJSONArray("data").getJSONObject(0).getString("suhu");
                    String kelembaban = responseObject.getJSONArray("data").getJSONObject(0).getString("kelembaban");
                    String atap = responseObject.getJSONArray("data").getJSONObject(0).getString("atap");
                    String title = "Atap Jemuran";
                    String cuaca2 = cuaca.substring(1, 6);
                    String cuaca3 = cuaca2.substring(0,1).toUpperCase() + cuaca2.substring(1);
                    String message = "Status: "+atap+", Cuaca: "+cuaca3;
                    String message2 = "Suhu: "+suhu+"°C"+", Kelembaban: "+kelembaban+"%";
                    int notifId = 100;
                        showNotification(getApplicationContext(), title, message, message2, notifId);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("GetWeather", "Failed");
            }
        });
    }

    @Override
    public void onInitializeTasks() {
        super.onInitializeTasks();
        SchedulerTask mSchedulerTask = new SchedulerTask(this);
        mSchedulerTask.createPeriodicTask();
    }

    private void showNotification(Context context, String title, String message, String message2, int notifId) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(this, 0, intent, 0);
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_home_black_24dp)
                .setContentText(message)
                .setSubText(message2)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context, android.R.color.black))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);
        notificationManagerCompat.notify(notifId, builder.build());
    }
}

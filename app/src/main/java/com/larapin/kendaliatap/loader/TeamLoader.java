package com.larapin.kendaliatap.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.larapin.kendaliatap.entity.TeamItems;
import com.larapin.kendaliatap.preference.UserPreference;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by asus on 24/12/2017.
 */

public class TeamLoader extends AsyncTaskLoader<ArrayList<TeamItems>> {
    private ArrayList<TeamItems> mData;
    public boolean hasResult = false;
    private UserPreference mUserPreference;
    private String host;

    public TeamLoader(final Context context){
        super(context);
        onContentChanged();
        Log.d("INIT ASYNCLOADER","1");
    }
    @Override
    protected void onStartLoading() {
        mUserPreference = new UserPreference(getContext());
        Log.d("Content Changed","1");
        if (takeContentChanged())
            forceLoad();
        else if (hasResult)
            deliverResult(mData);
    }

    @Override
    public void deliverResult(final ArrayList<TeamItems> data) {
        mData = data;
        hasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (hasResult) {
            onReleaseResources(mData);
            mData = null;
            hasResult = false;
        }
    }
    
    @Override
    public ArrayList<TeamItems> loadInBackground() {
        Log.d("LOAD BG","1");
        SyncHttpClient client = new SyncHttpClient();
        final ArrayList<TeamItems> teamItemses = new ArrayList<>();
        host = mUserPreference.getHost();
        String url = "http://"+host+"/hujan/team.php";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("team");

                    for (int i = 0 ; i < list.length() ; i++){
                        JSONObject team = list.getJSONObject(i);
                        TeamItems teamItems = new TeamItems(team);
                        teamItemses.add(teamItems);
                    }
                    Log.d("REQUEST SUCCESS","1");
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("REQUEST FAILED","1");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
            }
        });

        for (int i = 0 ; i< teamItemses.size() ; i++){
            Log.d("TEAM",teamItemses.get(i).getNama());
        }
        Log.d("BEFORE RETURN","1");
        return teamItemses;
    }
    protected void onReleaseResources(ArrayList<TeamItems> data) {
        //nothing to do.
    }
}

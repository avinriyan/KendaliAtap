package com.larapin.kendaliatap.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.larapin.kendaliatap.entity.CuacaItems;
import com.larapin.kendaliatap.preference.UserPreference;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by asus on 25/12/2017.
 */

public class CuacaLoader extends AsyncTaskLoader<ArrayList<CuacaItems>>{
    private ArrayList<CuacaItems> mData;
    public boolean hasResult = false;
    private UserPreference mUserPreference;
    private String host;

    public CuacaLoader(final Context context){
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
    public void deliverResult(final ArrayList<CuacaItems> data) {
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
    public ArrayList<CuacaItems> loadInBackground() {
        Log.d("LOAD BG","1");
        SyncHttpClient client = new SyncHttpClient();
        final ArrayList<CuacaItems> cuacaItemses = new ArrayList<>();
        host = mUserPreference.getHost();
        String url = "http://"+host+"/hujan/select.php";

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
                    JSONArray list = responseObject.getJSONArray("data");

                    for (int i = 0 ; i < list.length() ; i++){
                        JSONObject cuaca = list.getJSONObject(i);
                        CuacaItems cuacaItems = new CuacaItems(cuaca);
                        cuacaItemses.add(cuacaItems);
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

        for (int i = 0 ; i< cuacaItemses.size() ; i++){
            Log.d("CUACA",cuacaItemses.get(i).getCuaca());
        }
        Log.d("BEFORE RETURN","1");
        return cuacaItemses;
    }
    protected void onReleaseResources(ArrayList<CuacaItems> data) {
        //nothing to do.
    }
}

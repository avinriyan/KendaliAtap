package com.larapin.kendaliatap.activity;

import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.larapin.kendaliatap.R;
import com.larapin.kendaliatap.adapter.TeamAdapter;
import com.larapin.kendaliatap.entity.TeamItems;
import com.larapin.kendaliatap.loader.TeamLoader;
import com.larapin.kendaliatap.preference.UserPreference;

import java.util.ArrayList;

public class TeamActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<TeamItems>> {
    RecyclerView recyclerView;
    TeamAdapter adapter;
    private static String EXTRA = "extra";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        setTitle("Kelompok 1");
        
        adapter = new TeamAdapter(this);
        adapter.notifyDataSetChanged();
        recyclerView = (RecyclerView)findViewById(R.id.list_team);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<ArrayList<TeamItems>> onCreateLoader(int id, Bundle args) {
        Log.d("Create loader","1");
        return new TeamLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<TeamItems>> loader, ArrayList<TeamItems> data) {
        Log.d("Load Finish","1");
        adapter.setData(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<TeamItems>> loader) {
        Log.d("Load Reset","1");
        adapter.setData(null);
    }
}

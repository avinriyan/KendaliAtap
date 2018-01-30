package com.larapin.kendaliatap.activity;

import android.app.LoaderManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Loader;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.larapin.kendaliatap.R;
import com.larapin.kendaliatap.adapter.CuacaAdapter;
import com.larapin.kendaliatap.entity.CuacaItems;
import com.larapin.kendaliatap.loader.CuacaLoader;
import com.larapin.kendaliatap.preference.UserPreference;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener,
        LoaderManager.LoaderCallbacks<ArrayList<CuacaItems>>{

    TextView tvHost;
    Button btnBuka, btnTutup;
    private UserPreference mUserPreference;
    private boolean isPreferenceEmpty = false;
    private String host;
    private CuacaAdapter adapter;
    private RecyclerView recyclerView;
    public static final int NOTIFICAITION_ID = 1;
    private NotificationCompat.Builder notification;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btnBuka = (Button)findViewById(R.id.btn_atap_buka);
        btnTutup = (Button)findViewById(R.id.btn_atap_tutup);
        btnBuka.setOnClickListener(this);
        btnTutup.setOnClickListener(this);

        tvHost = (TextView) findViewById(R.id.tv_host);
        mUserPreference = new UserPreference(this);

        showExistingPreference();

        adapter = new CuacaAdapter(this);
        adapter.notifyDataSetChanged();
        recyclerView = (RecyclerView)findViewById(R.id.list_cuaca);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        getLoaderManager().restartLoader(0,null,MainActivity.this);

    }

    private void showExistingPreference() {
        if (!TextUtils.isEmpty(mUserPreference.getHost())){
            tvHost.setText("Host: "+mUserPreference.getHost());
        }else {
            final String TEXT_EMPTY = "(Atur di halaman Pengaturan)";
            tvHost.setText("Host: "+TEXT_EMPTY);
            isPreferenceEmpty = true;
        }

        if (TextUtils.equals(mUserPreference.getMode(),"Otomatis")){
            btnBuka.setVisibility(View.GONE);
            btnTutup.setVisibility(View.GONE);
        } else if (TextUtils.equals(mUserPreference.getMode(),"Manual")){
            btnBuka.setVisibility(View.VISIBLE);
            btnTutup.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivityForResult(intent, SettingActivity.REQUEST_CODE);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            return true;
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivityForResult(intent, SettingActivity.REQUEST_CODE);
        } else if (id == R.id.nav_kelompok) {
            Intent intent = new Intent(MainActivity.this, TeamActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        host = mUserPreference.getHost();
        if (view.getId() == R.id.btn_atap_buka){
            new HttpRequest().execute("http://"+host+"/hujan/update.php?id=1&status=1");
            Toast.makeText(this, "Buka", Toast.LENGTH_SHORT).show();

        }else if(view.getId() == R.id.btn_atap_tutup){
            new HttpRequest().execute("http://"+host+"/hujan/update.php?id=1&status=0");
            Toast.makeText(this, "Tutup", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public Loader<ArrayList<CuacaItems>> onCreateLoader(int i, Bundle bundle) {
        Log.d("Create loader","1");
        return new CuacaLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<CuacaItems>> loader, ArrayList<CuacaItems> data) {
        Log.d("Load Finish","1");
        adapter.setData(data);
        getLoaderManager().restartLoader(0,null,MainActivity.this);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<CuacaItems>> loader) {
        Log.d("Load Reset","1");
        adapter.setData(null);
    }

    public static class HttpRequest extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try{
                response = httpClient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                } else{
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
            return responseString;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SettingActivity.REQUEST_CODE){
            showExistingPreference();
        }
    }
}

package com.larapin.kendaliatap.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.larapin.kendaliatap.R;
import com.larapin.kendaliatap.preference.UserPreference;
import com.larapin.kendaliatap.scheduler.SchedulerTask;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtHost;
    Button btnSave;
    public static String EXTRA_TYPE_FORM = "extra_type_form";
    public static int REQUEST_CODE = 100;
    public static int TYPE_ADD = 1;
    public static int TYPE_EDIT = 2;
    int formType;
    final String FIELD_REQUIRED = "Field tidak boleh kosong";

    private UserPreference mUserPreference;
    private Button btnAuto;
    private Button btnManu;

    public String host;
    private Button btnCancelScheduler, btnSetScheduler;

    private SchedulerTask mSchedulerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setTitle("Pengaturan");

        edtHost = (EditText)findViewById(R.id.edt_host);
        btnSave = (Button)findViewById(R.id.btn_simpan);
        btnAuto = (Button)findViewById(R.id.btn_auto);
        btnManu = (Button)findViewById(R.id.btn_manual);
        btnAuto.setOnClickListener(this);
        btnManu.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnCancelScheduler = (Button)findViewById(R.id.btn_notif_off);
        btnSetScheduler = (Button)findViewById(R.id.btn_notif_on);
        btnSetScheduler.setOnClickListener(this);
        btnCancelScheduler.setOnClickListener(this);

        formType = getIntent().getIntExtra(EXTRA_TYPE_FORM, 0);
        mUserPreference = new UserPreference(this);

        showPreferenceInForm();

        mSchedulerTask = new SchedulerTask(this);
    }

    private void showPreferenceInForm(){
        edtHost.setText(mUserPreference.getHost());
    }

    @Override
    public void onClick(View view) {
        String host = mUserPreference.getHost();
        if (view.getId() == R.id.btn_simpan){
            String hostbaru = edtHost.getText().toString().trim();
            boolean isEmpty = false;

            if (TextUtils.isEmpty(hostbaru)){
                isEmpty = true;
                edtHost.setError(FIELD_REQUIRED);
            }
            if (!isEmpty){
                mUserPreference.setHost(hostbaru);
                Toast.makeText(this, "Data tersimpan", Toast.LENGTH_SHORT).show();

                finish();
            }
        } else if(view.getId() == R.id.btn_auto){
            String mode = "Otomatis";
            mUserPreference.setMode(mode);
            new MainActivity.HttpRequest().execute("http://"+host+"/hujan/update.php?id=2&status=1");
            Toast.makeText(this, "Mode Otomatis", Toast.LENGTH_SHORT).show();

            finish();
        }else if (view.getId() == R.id.btn_manual){
            String mode = "Manual";
            mUserPreference.setMode(mode);
            new MainActivity.HttpRequest().execute("http://"+host+"/hujan/update.php?id=2&status=0");
            Toast.makeText(this, "Mode Manual", Toast.LENGTH_SHORT).show();;

            finish();
        } else if(view.getId() == R.id.btn_notif_on){
            mSchedulerTask.createPeriodicTask();
            Toast.makeText(this, "Notifikasi On", Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.btn_notif_off){
            mSchedulerTask.cancelPeriodicTask();
            Toast.makeText(this, "Notifikasi Off", Toast.LENGTH_SHORT).show();
        }
    }
}

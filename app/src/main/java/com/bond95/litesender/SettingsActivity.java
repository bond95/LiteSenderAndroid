package com.bond95.litesender;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;


public class SettingsActivity extends ActionBarActivity {

    final private int REQUEST_SAVE_PATH = 1;

    private TextView showPath;
    private Button openDirBtn;
    private EditText deviceNameField;
    private String path;
    private String deviceName;
    private LiteSender liteSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        GlobalState gs = (GlobalState) getApplication();
        liteSender = gs.getLiteSender();

//        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
//        path = settings.getString("save_path", Constants.DEFAULT_DIR);
//        deviceName = settings.getString("device_name", Constants.DEFAULT_DEV_NAME);

//        showPath = (TextView) findViewById(R.id.showText);
//        openDirBtn = (Button) findViewById(R.id.chooseDirectoryBtn);
        deviceNameField = (EditText) findViewById(R.id.deviceNameField);

//        showPath.setText(path);
        deviceNameField.setText(liteSender.getName());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    public void ChooseDir(View v) {
        Intent intent = new Intent(this, DirectoryList.class);
        startActivityForResult(intent, REQUEST_SAVE_PATH);
    }

    public void Save(View v) {
//        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putString("save_path", path);
//        deviceName = deviceNameField.getText().toString();
//        editor.putString("device_name", deviceName);

        // Commit the edits!
//        editor.commit();
        liteSender.setName(deviceNameField.getText().toString());
        liteSender.sendNameChange();

        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case REQUEST_SAVE_PATH:
                    if (intent == null) {
                        return;
                    }
                    path = intent.getStringExtra("path");
                    showPath.setText(path);

                    break;
            }
        }
    }
}

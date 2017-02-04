package com.bond95.litesender;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by bond95 on 7/24/16.
 */
public class SettingsDriver {
    private HashMap<String, String> settings = new HashMap<String, String>();
    private String dir_path;
    static private Context context;
    private HashMap<String, String> default_settings = new HashMap<String, String>();

    public SettingsDriver() {
        default_settings.put("name", Constants.DEFAULT_DEV_NAME);
        default_settings.put("dir", Constants.DEFAULT_DIR);
        default_settings.put("key", "");
    }

    public void LoadSettings() {
        SharedPreferences settings_temp = context.getSharedPreferences(Constants.PREFS_NAME, 0);
        for (String currentKey : default_settings.keySet()) {
            settings.put(currentKey, settings_temp.getString(currentKey, default_settings.get(currentKey)));
        }
        LogDriver.println("LOAD_SETTINGS", "Load settings");

        File dir = new File(settings.get("dir"));
        LogDriver.println("LOAD_SETTINGS", "Creating dir");
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public void SaveSettings() {
        SharedPreferences settings_temp = context.getSharedPreferences(Constants.PREFS_NAME, 0);
        Iterator it = settings.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            settings_temp.edit().putString((String) pair.getKey(), (String) pair.getValue());
            LogDriver.println("SAVE_SETTINGS", pair.getKey() + " = " + pair.getValue());
            //it.remove(); // avoids a ConcurrentModificationException
        }
    }

    public void setDirPath(String dir_path) {
        this.dir_path = dir_path;
    }

    public void setSetting(String key, String value) {
        settings.put(key, value);
    }

    public String getSetting(String key) {
        return settings.get(key);
    }

    static public void setContext(Context context1) {
        context = context1;
    }


}

package com.bond95.litesender;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;


public class DirectoryList extends ActionBarActivity {
    ListView list_dir;
    TextView textPath;
    Context _context;
    int select_id_list = -1;
    String path = "/";

    ArrayList<String> ArrayDir = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    protected void onCreate(Bundle savedInstanceState)
    {
        _context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory_list);

        list_dir = (ListView) findViewById(R.id.listDirView);
        textPath = (TextView) findViewById(R.id.textDirPath);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ArrayDir);
        list_dir.setAdapter(adapter);

        updateListDir();

        list_dir.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                select_id_list = (int)id;
                updateListDir();
            }
        });

    }

    public void onClickBack(View view)
    {
        path = (new File(path)).getParent();
        updateListDir();
    }

    public void onClickGo(View view)
    {
        Intent intent = new Intent();
        intent.putExtra("path", path);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void updateListDir(){
        if(select_id_list != -1) path = path + ArrayDir.get(select_id_list) + "/";
        select_id_list = -1;
        ArrayDir.clear();
        File[] files = new File(path).listFiles();
        for ( File aFile : files ){
            if ( aFile.isDirectory() ) {
                if(dirOpened(aFile.getPath())){
                    ArrayDir.add(aFile.getName());
                }
            }
        }
        adapter.notifyDataSetChanged();
        textPath.setText(path);
    }

    private boolean dirOpened(String url) {
        try {
            File[] files = new File(url).listFiles();
            for (@SuppressWarnings("unused") File aFile : files) {
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

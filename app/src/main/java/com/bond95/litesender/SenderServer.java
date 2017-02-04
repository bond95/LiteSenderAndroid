package com.bond95.litesender;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SenderServer extends Service {
    LiteSender liteSender;

    public SenderServer() {

    }

    public void onCreate() {
        super.onCreate();
        Log.d("SERVICE_TEST", "MyService onCreate");
        GlobalState gs = (GlobalState) getApplication();
        liteSender = gs.getLiteSender();
        liteSender.Start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}

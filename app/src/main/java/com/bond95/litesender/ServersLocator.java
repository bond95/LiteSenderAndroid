package com.bond95.litesender;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;


public class ServersLocator extends ActionBarActivity {

    ArrayAdapter<DeviceListItem> adapter;
    ListView list_device;
    HashMap<String, String[]> server_list;
    private String item_id;
    final private String filename = "devices.dat";
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servers_locator);

        final GlobalState gs = (GlobalState) getApplication();

        FeedbackClass feedbackClass = new FeedbackClass() {
            @Override
            void addToList(DeviceListItem item, boolean last) {
                final DeviceListItem item2 = item;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        adapter.add(item2);
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            void removeFromList(DeviceListItem item) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            void changeLabel(DeviceListItem item) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        gs.getLiteSender().setFeedback(feedbackClass);

        list_device = (ListView)findViewById(R.id.deviceList);

        adapter = new DeviceArrayAdapter(this, R.layout.device_item_layout, gs.getLiteSender().getDevices());
//        adapter.setNotifyOnChange(true);
        list_device.setAdapter(adapter);

        Bundle b = getIntent().getExtras();
        key = b.getString("key");

        list_device.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView v = (TextView) view;
//                item_id = v.getText().toString();
                DeviceListItem item = (DeviceListItem) parent.getItemAtPosition(position);
                gs.getLiteSender().setSelectedItem(item);
                setResult(Activity.RESULT_OK);
                finish();
//                if (!DeviceExist(item_id)) {
//                    Log.d("ERROR", "Still work");
//                    AlertDialog.Builder builder = new AlertDialog.Builder(ServersLocator.this);
//                    AlertDialog alertDialog = builder.setMessage("Add this \"" + item_id + "\" device to trusted list?")
//                            .setPositiveButton("Add",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int whichButton) {
//                                            FileOutputStream outputStream;
//
//                                            try
//                                            {
//                                                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(openFileOutput(filename, (Context.MODE_PRIVATE | Context.MODE_APPEND)))));
//                                                out.println(item_id);
//                                                out.close();
//                                                Log.d("APPEND", "ADD new line");
//                                            } catch (IOException e) {
//                                                Log.d("APPEND", "Error");
//                                                //exception handling left as an exercise for the reader
//                                            }
//                                            ServersLocator.this.addDeviceToList();
//                                        }
//                                    }
//                            )
//                            .setNegativeButton("Cancel",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int whichButton) {
////                                            ServersLocator.this.doNegativeClick();
//                                        }
//                                    }
//                            )
//                            .create();
//                    alertDialog.show();
//
//                }
//                else {
//                    Log.d("ERROR", "Still work2");
//
////                    addDeviceToList();
//                }
            }
        });

//        LocatorThread thr = new LocatorThread(3000, 9509);
//        thr.execute();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_servers_locator, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
//
//    public class LocatorThread extends AsyncTask<Void, Void, HashMap<String, String[]>> {
//
//        int timeout;
//        short response = 0;
//        int socket;
//
//        LocatorThread(int time_out, int sock) {
//            timeout = time_out;
//            socket = sock;
//        }
//
//        @Override
//        protected HashMap<String, String[]> doInBackground(Void... arg0) {
//
//            HashMap<String, String[]> al = new HashMap<String, String[]>();
//            DatagramSocket c;
//            byte[] buffer = "HI_SERVER".getBytes();
//            String resp_phrase = "HELLO_MY_NAME_IS:";
//            try {
//                //Open a random port to send the package
//                c = new DatagramSocket();
//                c.setBroadcast(true);
//
//                //Try the 255.255.255.255 first
//                try {
//                    DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("255.255.255.255"), socket);
//                    c.send(sendPacket);
//                    Log.d("LOCATOR",">>> Request packet sent to: 255.255.255.255 (DEFAULT)");
//                } catch (Exception e) {
//                }
//
//                // Broadcast the message over all the network interfaces
//                Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
//                while (interfaces.hasMoreElements()) {
//                    NetworkInterface networkInterface = (NetworkInterface)interfaces.nextElement();
//
//                    if (networkInterface.isLoopback() || !networkInterface.isUp()) {
//                        continue; // Don't want to broadcast to the loopback interface
//                    }
//
//                    for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
//                        InetAddress broadcast = interfaceAddress.getBroadcast();
//                        if (broadcast == null) {
//                            continue;
//                        }
//
//                        // Send the broadcast package!
//                        try {
//                            DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, broadcast, socket);
//                            c.send(sendPacket);
//                        } catch (Exception e) {
//                        }
//
//                        Log.d("LOCATOR",">>> Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());
//                    }
//                }
//
//                Log.d("LOCATOR",">>> Done looping over all network interfaces. Now waiting for a reply!");
//                c.setSoTimeout(timeout);
//                //Wait for a response
//                byte[] recvBuf = new byte[15000];
//                DatagramPacket receivePacket;
//                receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
//                for (int i = 0; i < 10; i++) {
//                    try {
//                        c.receive(receivePacket);
//
//                        //We have a response
//                        Log.d("LOCATOR", ">>> Broadcast response from server: " + receivePacket.getAddress().getHostAddress());
//
//                        //Check if the message is correct
//                        String message = new String(receivePacket.getData()).trim();
//                        if (message.startsWith(resp_phrase)) {
//                            String[] inner_list = new String[2];
//                            String[] list = message.split(":");
//                            inner_list[0] = list[2];
//                            inner_list[1] = receivePacket.getAddress().getHostAddress();
//                            al.put(list[1], inner_list);
//                        }
//                        receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
//                    }
//                    catch (Exception e)
//                    {
//                        break;
//                    }
//                }
//                //Close the port!
//                c.close();
//            } catch (IOException ex) {
//                System.out.println(ex.getMessage());
//            }
//            return al;
//        }
//
//        @Override
//        protected void onPostExecute(HashMap<String, String[]> result) {
//            server_list = result;
//            for ( String k : result.keySet() ) {
//                if (!server_list.get(k)[0].equals(key))
//                    adapter.add(k);
//            }
//        }
//
//        public String getSubnet(InetAddress address)
//        {
//            byte[] a = address.getAddress();
//            Log.d("HOST_ADDRESS", address.getHostAddress());
//            String subnet = "";
//            for(int i = 0; i < a.length - 1; i++)
//            {
//                if(a[i] != 0)
//                    subnet += Integer.toString(a[i]) + ".";
//                else
//                    subnet += 0 + ".";
//            }
//            return subnet;
//        }
//    }
//    public void addDeviceToList() {
//        String[] device_info = server_list.get(item_id);
//        Intent intent = new Intent();
//        intent.putExtra("ip", device_info);
//        setResult(Activity.RESULT_OK, intent);
//        finish();
//    }
//
//    public void doNegativeClick() {
//        // Do stuff here.
//        Log.i("FragmentAlertDialog", "Negative click!");
//    }
//    public boolean DeviceExist(String device) {
//        FileInputStream inputStream = null;
//        BufferedReader reader = null;
//        String temp_device = "";
//        try {
//            inputStream = openFileInput(filename);
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//            while ((temp_device = reader.readLine()) != null) {
//                if (temp_device.equals(device)) {
//                    return true;
//                }
//            }
//            inputStream.close();
//        } catch (Exception e) {
//            Log.d("ERROR", "File open error");
//            //e.printStackTrace();
//            return false;
//        }
//        return false;
//    }
}

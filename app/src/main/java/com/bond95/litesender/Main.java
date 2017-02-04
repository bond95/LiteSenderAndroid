package com.bond95.litesender;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class Main extends ActionBarActivity {
    GridView list_dir;
    TextView textPath;

    final private int REQUEST_SERVER = 1;
    final private int REQUEST_SETTINGS = 2;

    ArrayList<Item> ArrayDir = new ArrayList<Item>();
    FileArrayAdapter adapter;
    int select_id_list = -1;
    String path = "/";

    private String key;
    private String last_dev;
    private LiteSender liteSender;
    private GlobalState gs;

    private NotificationDriver notificationDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sender);
        list_dir = (GridView) findViewById(R.id.list_dir);
        textPath = (TextView) findViewById(R.id.textPath);
        gs = (GlobalState) getApplication();
        notificationDriver = new NotificationDriver();
        Notification.setContext(getApplicationContext());
        SettingsDriver.setContext(getApplicationContext());

        //Create LiteSender object
        liteSender = new LiteSender();
        liteSender.setNotificationDriver(notificationDriver);

        //Mock
        liteSender.setFeedback(new FeedbackClass() {
            @Override
            void addToList(DeviceListItem item, boolean last) {
                if (last) {
                    Toast.makeText(getApplicationContext(), "Connected to the last device", Toast.LENGTH_LONG);
                }
            }

            @Override
            void removeFromList(DeviceListItem item) {

            }

            @Override
            void changeLabel(DeviceListItem item) {

            }
        });
        gs.setLiteSender(liteSender);

        //Start server service
        startService(new Intent(this, SenderServer.class));

        //Part for work with files
        adapter = new FileArrayAdapter(this, R.layout.file_item_layout, ArrayDir);
        list_dir.setAdapter(adapter);

        updateListDir();

        //Setup drug&drop
        findViewById(R.id.textPath).setOnDragListener(DropListener);

        list_dir.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                select_id_list = (int) id;
                updateListDir();
            }
        });
        list_dir.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int pos, long id) {
                if ((new File(path + ArrayDir.get((int) id).getName())).isFile()) {
                    //Starting drag
                    DragShadow dragShadow = new DragShadow(v);

                    ClipData data = ClipData.newPlainText("", "");

                    v.startDrag(data, dragShadow, v, 0);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Get list of files from current directory and put it into layout
     */
    private void updateListDir() {
        // Get path to a current directory
        if (select_id_list != -1) {
            if ((new File(path + ArrayDir.get(select_id_list).getName())).isDirectory()
                    && dirOpened(path + ArrayDir.get(select_id_list).getName())) {
                path = path + ArrayDir.get(select_id_list).getName() + "/";
            } else {
                return;
            }
        }

        // Get files
        select_id_list = -1;
        ArrayDir.clear();
        ArrayList<Item> fs = new ArrayList<Item>();
        File[] files = (new File(path)).listFiles();
        for (File aFile : files) {
            if (aFile.isDirectory())
                ArrayDir.add(new Item(aFile.getName(), "directory"));
            else
                fs.add(new Item(aFile.getName(), "file"));
        }
        ArrayDir.addAll(fs);

        // Set layout update signal
        adapter.notifyDataSetChanged();
        textPath.setText(path);
    }

    /**
     * Check if direstory can be opened and readed
     *
     * @param url
     * @return
     */
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

    /**
     * Callback for "back" button
     *
     * @param view
     */
    public void onClickBack(View view) {
        if ((new File(path)).getParent() == null) {
            path = "/";
        } else {
            path = (new File(path)).getParent() + "/";
        }
        updateListDir();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_sender, menu);
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
            Log.d("SETTING", "Work");
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityForResult(intent, REQUEST_SETTINGS);
        } else {
            if (id == R.id.action_locator) {
                Intent intent = new Intent(this, ServersLocator.class);
                Bundle b = new Bundle();
                b.putString("key", key); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivityForResult(intent, REQUEST_SERVER);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Class for drag shadow rendering
     */
    private class DragShadow extends View.DragShadowBuilder {

        ColorDrawable greyBox;

        public DragShadow(View view) {
            super(view);
            greyBox = new ColorDrawable(Color.LTGRAY);
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            greyBox.draw(canvas);
        }

        @Override
        public void onProvideShadowMetrics(Point shadowSize,
                                           Point shadowTouchPoint) {
            View v = getView();

            int height = v.getHeight() / 2;
            int width = v.getWidth() / 2;

            greyBox.setBounds(0, 0, width, height);
            shadowSize.set(width, height);

            shadowTouchPoint.set(width / 2, height / 2);

        }


    }

    /**
     * Drop listener
     */
    View.OnDragListener DropListener = new View.OnDragListener() {

        @Override
        public boolean onDrag(View v, DragEvent event) {

            int dragEvent = event.getAction();

            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_STARTED:
                    textPath.setHeight(60);
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.i("Drag ", "Entered");
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.i("Drag ", "Exit");
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    textPath.setHeight(35);
                    break;
                case DragEvent.ACTION_DROP:
                    Log.i("Drag ", "Drop");

                    //Get dropped file
                    View dragg = (View) event.getLocalState();
                    TextView dragg_text = (TextView) dragg.findViewById(R.id.fileName);
                    String fileName = path + dragg_text.getText().toString();
                    File aFile = new File(fileName);

                    //Send it
                    liteSender.sendFiles(aFile);

                    break;
                default:
                    break;
            }

            return true;
        }
    };

    /**
     * Handle response from actvity
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        String[] url;
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case REQUEST_SERVER:
                    //Mock again
                    liteSender.setFeedback(new FeedbackClass() {
                        @Override
                        void addToList(DeviceListItem item, boolean last) {
                            if (last) {
                                Toast.makeText(getApplicationContext(), "Connected to the last device", Toast.LENGTH_LONG);
                            }
                        }

                        @Override
                        void removeFromList(DeviceListItem item) {

                        }

                        @Override
                        void changeLabel(DeviceListItem item) {

                        }
                    });

                    break;
            }
        }
    }

    //    public class LastDeviceLocator extends AsyncTask<Void, Void, HashMap<String, String[]>> {
//
//        int timeout;
//        int socket;
//
//        LastDeviceLocator(int time_out, int sock) {
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
//                    Log.d("LOCATOR", ">>> Request packet sent to: 255.255.255.255 (DEFAULT)");
//                } catch (Exception e) {
//                }
//
//                // Broadcast the message over all the network interfaces
//                Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
//                while (interfaces.hasMoreElements()) {
//                    NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();
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
//                        Log.d("LOCATOR", ">>> Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());
//                    }
//                }
//
//                Log.d("LOCATOR", ">>> Done looping over all network interfaces. Now waiting for a reply!");
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
//                    } catch (Exception e) {
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
//            Log.d("LAST_DEV", last_dev);
//            for (String key : result.keySet()) {
//                Log.d("SEARCH", result.get(key)[0]);
//                if (result.get(key)[0].equals(last_dev)) {
//                    ip_addr = result.get(key)[1] + ":" + port;
//                    Context context = getApplicationContext();
//                    CharSequence text = "Last device was connected.";
//                    int duration = Toast.LENGTH_SHORT;
//                    Log.d("FOUND", "I found this");
//                    Toast toast = Toast.makeText(context, text, duration);
//                    toast.show();
//                }
//            }
//        }
//    }
    public NotificationDriver getNotificationDriver() {
        return notificationDriver;
    }
}

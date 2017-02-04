package com.bond95.litesender;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bohdan on 02.10.15.
 */
public class DiscoveryThread implements Runnable {

    @Override
    public void run() {
        HashMap<String, InetAddress> result = checkHosts(9509, 10);
        for ( String key : result.keySet() ) {

        }
    }

    public static String getSubnet(InetAddress address)
    {
        byte[] a = address.getAddress();
        String subnet = "";
        for(int i = 0; i < a.length - 1; i++)
        {
            if(a[i] != 0)
                subnet += (256 + a[i]) + ".";
            else
                subnet += 0 + ".";
        }
        return subnet;
    }

    public static HashMap<String, InetAddress> checkHosts(int socket, int timeout)
    {
        HashMap<String, InetAddress> al = new HashMap<String, InetAddress>();
        try
        {
            byte[] buffer = "HI_SERVER".getBytes();
            String resp_phrase = "HELLO_MY_NAME_IS:";
            DatagramSocket ds = new DatagramSocket();
            ds.setSoTimeout(timeout);
            DatagramPacket dp;
            InetAddress local = InetAddress.getLocalHost();
            String subnet = getSubnet(local);
            System.out.println(subnet);
            byte[] recvBuf = new byte[15000];
            for(int i = 1; i <= 255; i++)
            {
                String host = subnet + i;
                try {
                    ds.send(new DatagramPacket(buffer, buffer.length, InetAddress.getByName(host), socket));
                    dp = new DatagramPacket(recvBuf, recvBuf.length);
                    ds.receive(dp);
                    String message = new String(dp.getData()).trim();
                    if(message.startsWith(resp_phrase))
                        al.put(message.substring(resp_phrase.length()), dp.getAddress());
                } catch(Exception e) {
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return al;
    }

    public static DiscoveryThread getInstance() {
        return DiscoveryThreadHolder.INSTANCE;
    }

    private static class DiscoveryThreadHolder {

        private static final DiscoveryThread INSTANCE = new DiscoveryThread();
    }

}


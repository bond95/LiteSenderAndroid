package com.bond95.litesender;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class GlobalState extends Application {
    private LiteSender liteSender;
    private NotificationDriver notificationDriver = new NotificationDriver();

    public void setLiteSender(LiteSender liteSender) {
        this.liteSender = liteSender;
    }

    public LiteSender getLiteSender() {
        return liteSender;
    }



}

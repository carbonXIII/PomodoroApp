package org.team.app.view;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

// Make notification "clickable" to open the app again.
public class NotificationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Links back to main screen, might need to change this
        setContentView(R.layout.screen_timer);
    }
}
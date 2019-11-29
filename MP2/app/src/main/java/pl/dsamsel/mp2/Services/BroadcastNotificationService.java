package pl.dsamsel.mp2.Services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.annotation.Nullable;

import pl.dsamsel.mp2.BroadcastReceivers.NotificationBroadcastReceiver;

public class BroadcastNotificationService extends Service {

    private NotificationBroadcastReceiver notificationBroadcastReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationBroadcastReceiver = new NotificationBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("pl.dsamsel.mp2.broadcast_intent");
        String permission = "pl.dsamsel.mp2.broadcast_intent.permission";
        registerReceiver(notificationBroadcastReceiver, intentFilter, permission, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (notificationBroadcastReceiver != null) {
            unregisterReceiver(notificationBroadcastReceiver);
        }
    }
}

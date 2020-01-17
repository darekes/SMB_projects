package pl.dsamsel.mp1.BroadcastReceivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

import pl.dsamsel.mp1.Activities.MainActivity;
import pl.dsamsel.mp1.R;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    private static final String GEOFENCE_TRANSITION_ENTER_TEXT = "Welcome in";
    private static final String GEOFENCE_TRANSITION_EXIT_TEXT = "Good bye from";
    private static final String UNKNOWN_GEOFENCE_TRANSITION = "Unknown geofence transition";
    private static final String CHANNEL_ID = "channel_01";
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            String geofenceTransitionDetails = getGeofenceTransitionDetails(geofenceTransition, triggeringGeofences);
            sendNotification(geofenceTransitionDetails);
        } else {
            Log.e(TAG, UNKNOWN_GEOFENCE_TRANSITION);
        }
    }

    private String getGeofenceTransitionDetails(int geofenceTransition, List<Geofence> triggeringGeofences) {
        String geofenceTransitionName = getTransitionString(geofenceTransition);
        StringBuilder triggeringGeofencesDetails = new StringBuilder(geofenceTransitionName);
        triggeringGeofences.forEach(geofence -> triggeringGeofencesDetails.append(geofence.getRequestId()));

        return triggeringGeofencesDetails.toString();
    }

    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return GEOFENCE_TRANSITION_ENTER_TEXT;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return GEOFENCE_TRANSITION_EXIT_TEXT;
            default:
                return UNKNOWN_GEOFENCE_TRANSITION;
        }
    }

    private void sendNotification(String notificationDetails) {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(MainActivity.class, CHANNEL_ID)
                .setContentTitle("Geofence works!")
                .setContentText(notificationDetails)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                "notification", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(id++, notification.build());
    }
}

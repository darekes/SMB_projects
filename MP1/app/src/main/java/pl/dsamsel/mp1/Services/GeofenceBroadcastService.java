package pl.dsamsel.mp1.Services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.util.Consumer;

import java.util.ArrayList;
import java.util.List;

import pl.dsamsel.mp1.BroadcastReceivers.GeofenceBroadcastReceiver;
import pl.dsamsel.mp1.Models.Shop;

public class GeofenceBroadcastService extends Service {

    private GeofencingClient geofencingClient;
    private ArrayList<Geofence> geofenceList;
    private PendingIntent geofencePendingIntent;

    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
    static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;
    static final float GEOFENCE_RADIUS_IN_METERS = 500;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!areLocationPermissionsGranted()) {
            requestPermissions();
        } else {
            addGeofences();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        geofenceList = new ArrayList<>();
        geofencingClient = LocationServices.getGeofencingClient(this);
        populateGeofenceList();
    }

    private void populateGeofenceList() {
        Consumer<List<Shop>> consumer = shops -> {
            shops.forEach(shop -> geofenceList.add(new Geofence.Builder()
                    .setRequestId(shop.getName())
                    .setCircularRegion(
                            shop.getLatitude(),
                            shop.getLongitude(),
                            GEOFENCE_RADIUS_IN_METERS
                    )
                    .setExpirationDuration(GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build()));
        };

        getShopsList(consumer);
    }

    private List<Shop> getShopsList(Consumer<List<Shop>> shopsList) {
        FirestoreDatabaseService databaseService = new FirestoreDatabaseService();
        return databaseService.getAllShops(shopsList);
    }

    private void addGeofences() {
        if (!areLocationPermissionsGranted()) {
            return;
        }

        if (!geofenceList.isEmpty()) {
            geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent());
        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);

        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        geofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    private boolean areLocationPermissionsGranted() {
        int permissionState = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        //;
    }
}

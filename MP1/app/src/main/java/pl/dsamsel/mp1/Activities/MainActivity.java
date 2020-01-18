package pl.dsamsel.mp1.Activities;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.util.Consumer;

import java.util.ArrayList;
import java.util.List;

import pl.dsamsel.mp1.BroadcastReceivers.GeofenceBroadcastReceiver;
import pl.dsamsel.mp1.Models.Shop;
import pl.dsamsel.mp1.R;
import pl.dsamsel.mp1.Services.FirestoreDatabaseService;
import pl.dsamsel.mp1.Services.PreferredGuiOptionsService;
import pl.dsamsel.mp1.Services.SharedPreferencesService;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private Button productListButton;
    private Button optionsListButton;
    private Button logoutButton;
    private Button googleMapsButton;
    private Button shopsListButton;

    //

    private GeofencingClient geofencingClient;
    private ArrayList<Geofence> geofenceList;
    private PendingIntent geofencePendingIntent;
    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
    static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;
    static final float GEOFENCE_RADIUS_IN_METERS = 5000;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        handlePreferredColorOptions();
        registerButtonListeners();

        //

        geofenceList = new ArrayList<>();
        geofencingClient = LocationServices.getGeofencingClient(this);
        populateGeofenceList();
    }

    private void handlePreferredColorOptions() {
        productListButton = findViewById(R.id.product_list_button);
        optionsListButton = findViewById(R.id.options_button);
        logoutButton = findViewById(R.id.logout_button);
        googleMapsButton = findViewById(R.id.google_map_button);
        shopsListButton = findViewById(R.id.shops_button);

        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesService
                .COLOR_PREFERENCES, Context.MODE_PRIVATE);
        PreferredGuiOptionsService preferredGuiOptionsService = new PreferredGuiOptionsService(sharedPreferences);
        preferredGuiOptionsService.setPreferredColorForButton(productListButton);
        preferredGuiOptionsService.setPreferredColorForButton(optionsListButton);
        preferredGuiOptionsService.setPreferredColorForButton(logoutButton);
        preferredGuiOptionsService.setPreferredColorForButton(googleMapsButton);
        preferredGuiOptionsService.setPreferredColorForButton(shopsListButton);
    }

    private void registerButtonListeners() {
        productListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToProductListActivity(v);
            }
        });

        optionsListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToOptionsActivity(v);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(v);
            }
        });

        googleMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToGoogleMapsActivity(v);
            }
        });

        shopsListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToShopsListActivity(v);
            }
        });
    }

    public void navigateToProductListActivity(View view) {
        Intent intent = new Intent(this, ProductListActivity.class);
        startActivity(intent);
    }

    public void navigateToOptionsActivity(View view) {
        Intent intent = new Intent(this, ColorOptionsActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {
        Intent intent = new Intent(this, LoginRegisterActivity.class);
        startActivity(intent);
    }

    public void navigateToGoogleMapsActivity(View view) {
        Intent intent = new Intent(this, GoogleMapsActivity.class);
        startActivity(intent);
    }

    public void navigateToShopsListActivity(View view) {
        Intent intent = new Intent(this, ShopsListActivity.class);
        startActivity(intent);
    }

    //

    private void populateGeofenceList() {
//        Consumer<List<Shop>> consumer = shops -> {
//            shops.forEach(shop -> geofenceList.add(new Geofence.Builder()
//                    .setRequestId(shop.getName())
//                    .setCircularRegion(
//                            shop.getLatitude(),
//                            shop.getLongitude(),
//                            GEOFENCE_RADIUS_IN_METERS
//                    )
//                    .setExpirationDuration(GEOFENCE_EXPIRATION_IN_MILLISECONDS)
//                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
//                            Geofence.GEOFENCE_TRANSITION_EXIT)
//                    .build()));
//        };
//
//        getShopsList(consumer);
        Shop shop = new Shop("sadasd", "name1", "desc2", 100000.0, 52.22307701547054, 20.99426049739122);
        geofenceList.add(new Geofence.Builder()
                .setRequestId(shop.getName())
                .setCircularRegion(
                        shop.getLatitude(),
                        shop.getLongitude(),
                        GEOFENCE_RADIUS_IN_METERS
                )
                .setExpirationDuration(GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());

        addGeofences();
    }

    private List<Shop> getShopsList(Consumer<List<Shop>> shopsList) {
        FirestoreDatabaseService databaseService = new FirestoreDatabaseService();
        return databaseService.getAllShops(shopsList);
    }

    private void addGeofences() {
        if (!geofenceList.isEmpty()) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 44);
            } else {
//                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//                boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//                if (!enabled) {
//                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    startActivity(intent);
//                } else {
//                    startGeofencing();
//                }
                startGeofencing();
            }
        }
    }

    private void startGeofencing() {
        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "GOOD GEO", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission denied 44", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                startGeofencing();
            }
        }
    }
}

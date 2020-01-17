package pl.dsamsel.mp1.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

import pl.dsamsel.mp1.R;
import pl.dsamsel.mp1.Services.GeolocationPermissionService;

public class AddShopGeoPointFromGoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String RANGE = "range";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    private String shopName;
    private String shopDescription;
    private String shopRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeolocationPermissionService.requestPermissionForLocation(this);
        setContentView(R.layout.add_shop_from_google_maps_activity);
        saveShopDataFromIntent();
        initMap();
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.add_shop_map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
    }

    public void saveShopDataFromIntent() {
        Intent intent = getIntent();
        shopName = intent.getStringExtra(NAME);
        shopDescription = intent.getStringExtra(DESCRIPTION);
        shopRange = intent.getStringExtra(RANGE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        moveCameraToCurrentGeolocation(googleMap);
        registerOnMapClickListener(googleMap);
    }

    public void moveCameraToCurrentGeolocation(GoogleMap googleMap) {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        googleMap.setMyLocationEnabled(true);
                        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                    }
                });
    }

    private void registerOnMapClickListener(GoogleMap googleMap) {
        googleMap.setOnMapClickListener(this::navigateToAddShopActivityWithExtras);
    }

    private void navigateToAddShopActivityWithExtras(LatLng latLng) {
        Intent intent = new Intent(this, AddShopActivity.class);
        intent.putExtra(NAME, shopName);
        intent.putExtra(DESCRIPTION, shopDescription);
        intent.putExtra(RANGE, shopRange);
        intent.putExtra(LATITUDE, String.valueOf(latLng.latitude));
        intent.putExtra(LONGITUDE, String.valueOf(latLng.longitude));

        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GeolocationPermissionService.PERMISSION_REQUEST_LOCATION) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                initMap();
            }
        }
    }
}

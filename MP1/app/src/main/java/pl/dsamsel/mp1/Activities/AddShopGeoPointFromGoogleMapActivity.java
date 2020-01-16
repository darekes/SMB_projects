package pl.dsamsel.mp1.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

import pl.dsamsel.mp1.R;
import pl.dsamsel.mp1.Services.GeolocationService;

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
        setContentView(R.layout.add_shop_from_google_maps_activity);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.add_shop_map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
        saveShopDataFromIntent();
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

    private void moveCameraToCurrentGeolocation(GoogleMap googleMap) {
        GeolocationService geolocationService = new GeolocationService(this);
        geolocationService.moveCameraToCurrentGeolocation(googleMap);
    }

    private void registerOnMapClickListener(GoogleMap googleMap) {
        googleMap.setOnMapClickListener(this::navigateToAddShopActivityWithExtras);
    }

    private void navigateToAddShopActivityWithExtras(LatLng latLng) {
        Intent intent = new Intent(this, AddShopActivity.class);
        intent.putExtra(NAME, shopName);
        intent.putExtra(DESCRIPTION, shopDescription);
        intent.putExtra(RANGE, shopRange);
        intent.putExtra(LATITUDE, latLng.latitude);
        intent.putExtra(LONGITUDE, latLng.longitude);

        startActivity(intent);
    }
}

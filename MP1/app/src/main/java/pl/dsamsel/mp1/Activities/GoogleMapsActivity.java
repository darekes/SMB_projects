package pl.dsamsel.mp1.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.util.Consumer;

import java.util.List;
import java.util.Objects;

import pl.dsamsel.mp1.Models.Shop;
import pl.dsamsel.mp1.R;
import pl.dsamsel.mp1.Services.FirestoreDatabaseService;
import pl.dsamsel.mp1.Services.GeolocationPermissionService;

public class GoogleMapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String CIRCLE_RANGE_COLOR = "#2271cce7";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String RANGE = "range";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_maps_activity);
        GeolocationPermissionService.requestPermissionForLocation(this);
        initMap();
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        attachShopMarkers(googleMap);
    }

    private void attachShopMarkers(GoogleMap googleMap) {
        Consumer<List<Shop>> consumer = shops -> shops.forEach(shop -> attachShopMarker(googleMap, shop));
        getShopsList(consumer);
        googleMap.setOnMarkerClickListener(this);
        moveCameraToCurrentGeolocation(googleMap);
    }

    private void attachShopMarker(GoogleMap googleMap, Shop shop) {
        LatLng shopGeoPoint = new LatLng(shop.getLatitude(), shop.getLongitude());
        googleMap.addMarker(new MarkerOptions()
                .position(shopGeoPoint)
                .title(shop.getName()))
                .setTag(shop);

        attachShopCircle(googleMap, shopGeoPoint, shop);
    }

    private void attachShopCircle(GoogleMap googleMap, LatLng shopGeoPoint, Shop shop) {
        googleMap.addCircle(new CircleOptions()
                .center(shopGeoPoint)
                .radius(shop.getRange())
                .strokeColor(Color.BLUE)
                .fillColor(Color.parseColor(CIRCLE_RANGE_COLOR))
                .strokeWidth(2f)
        );
    }

    private List<Shop> getShopsList(Consumer<List<Shop>> shopsList) {
        FirestoreDatabaseService databaseService = new FirestoreDatabaseService();
        return databaseService.getAllShops(shopsList);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return navigateToShopInfoActivityWithExtras(marker);
    }

    private boolean navigateToShopInfoActivityWithExtras(Marker marker) {
        Intent intent = new Intent(this, ShopInfoActivity.class);
        Shop clickedShop = (Shop) marker.getTag();
        intent.putExtra(NAME, Objects.requireNonNull(clickedShop).getName());
        intent.putExtra(DESCRIPTION, Objects.requireNonNull(clickedShop).getDescription());
        intent.putExtra(RANGE, String.valueOf(Objects.requireNonNull(clickedShop).getRange()));
        intent.putExtra(LATITUDE, String.valueOf(Objects.requireNonNull(clickedShop).getLatitude()));
        intent.putExtra(LONGITUDE, String.valueOf(Objects.requireNonNull(clickedShop).getLongitude()));

        startActivity(intent);
        return false;
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

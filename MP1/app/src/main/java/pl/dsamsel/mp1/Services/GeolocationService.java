package pl.dsamsel.mp1.Services;

import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class GeolocationService {

    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100;
    private LatLng geoPoint;

    private AppCompatActivity context;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public GeolocationService(AppCompatActivity context) {
        this.context = context;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        setCurrentGeolocationGeoPoint();
    }

    private void setCurrentGeolocationGeoPoint() {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        geoPoint = new LatLng(location.getLatitude(), location.getLongitude());
                    }
                });
    }

    public void setCurrentGeolocationForFields(final TextView shopLatitudeField, final TextView shopLongitudeField) {
        shopLatitudeField.setText(String.valueOf(geoPoint.latitude));
        shopLongitudeField.setText(String.valueOf(geoPoint.longitude));
    }

    private LatLng getCurrentGeolocationGeoPoint() {
        return this.geoPoint;
    }

    public void moveCameraToCurrentGeolocation(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(getCurrentGeolocationGeoPoint()));
    }
}

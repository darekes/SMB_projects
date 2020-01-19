package pl.dsamsel.mp1.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import pl.dsamsel.mp1.R;
import pl.dsamsel.mp1.Services.GeofenceBroadcastService;
import pl.dsamsel.mp1.Services.PreferredGuiOptionsService;
import pl.dsamsel.mp1.Services.SharedPreferencesService;

public class MainActivity extends AppCompatActivity {

    private Button productListButton;
    private Button optionsListButton;
    private Button logoutButton;
    private Button googleMapsButton;
    private Button shopsListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        handlePreferredColorOptions();
        registerButtonListeners();

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 44);
        } else {
            startGeofenceBroadcastService();
        }

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

    private void startGeofenceBroadcastService() {
        startService(new Intent(MainActivity.this, GeofenceBroadcastService.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission denied 44", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                startGeofenceBroadcastService();
            }
        }
    }
}

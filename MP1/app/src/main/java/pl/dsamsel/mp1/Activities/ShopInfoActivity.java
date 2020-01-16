package pl.dsamsel.mp1.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import pl.dsamsel.mp1.R;
import pl.dsamsel.mp1.Services.PreferredGuiOptionsService;
import pl.dsamsel.mp1.Services.SharedPreferencesService;

public class ShopInfoActivity extends AppCompatActivity {

    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String RANGE = "range";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    private Button goToHomepageButton;
    private Button goToGoogleMapsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_info_activity);

        handlePreferredColorOptions();
        registerButtonsListeners();
        fillClickedShopData();
    }

    private void fillClickedShopData() {
        Intent intent = getIntent();
        TextView nameField = findViewById(R.id.shop_info_name_value);
        TextView descriptionField = findViewById(R.id.shop_info_description_value);
        TextView rangeField = findViewById(R.id.shop_info_range_value);
        TextView latitudeField = findViewById(R.id.shop_info_latitude_value);
        TextView longitudeField = findViewById(R.id.shop_info_longitude_value);

        nameField.setText(intent.getStringExtra(NAME));
        descriptionField.setText(intent.getStringExtra(DESCRIPTION));
        rangeField.setText(intent.getStringExtra(RANGE));
        latitudeField.setText(intent.getStringExtra(LATITUDE));
        longitudeField.setText(intent.getStringExtra(LONGITUDE));
    }

    private void handlePreferredColorOptions() {
        goToHomepageButton = findViewById(R.id.go_to_homepage_button);
        goToGoogleMapsButton = findViewById(R.id.go_to_map_button);
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesService
                .COLOR_PREFERENCES, Context.MODE_PRIVATE);
        PreferredGuiOptionsService preferredGuiOptionsService = new PreferredGuiOptionsService(sharedPreferences);
        preferredGuiOptionsService.setPreferredColorForButton(goToHomepageButton);
        preferredGuiOptionsService.setPreferredColorForButton(goToGoogleMapsButton);
    }

    private void registerButtonsListeners() {
        registerGoToHomepageButtonListener();
        registerGoToGoogleMapsButtonListener();
    }

    private void registerGoToHomepageButtonListener() {
        goToHomepageButton.setOnClickListener(view -> navigateToMainActivity());
    }

    private void registerGoToGoogleMapsButtonListener() {
        goToGoogleMapsButton.setOnClickListener(view -> navigateToGoogleMapsActivity());
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void navigateToGoogleMapsActivity() {
        Intent intent = new Intent(this, GoogleMapsActivity.class);
        startActivity(intent);
    }
}

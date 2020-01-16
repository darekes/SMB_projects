package pl.dsamsel.mp1.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.common.base.Strings;

import java.util.UUID;

import pl.dsamsel.mp1.Models.Shop;
import pl.dsamsel.mp1.R;
import pl.dsamsel.mp1.Services.FirestoreDatabaseService;
import pl.dsamsel.mp1.Services.GeolocationService;
import pl.dsamsel.mp1.Services.PreferredGuiOptionsService;
import pl.dsamsel.mp1.Services.SharedPreferencesService;

public class AddShopActivity extends AppCompatActivity {

    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String RANGE = "range";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    private Button addShopButton;
    private Button getCurrentGeolocationButton;
    private Button getGeolocationFromMapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_shop_activity);

        handlePreferredColorOptions();
        registerButtonsListeners();
        saveShopDataFromGoogleMapIntent();
    }

    private void handlePreferredColorOptions() {
        addShopButton = findViewById(R.id.submit_add_product);
        getCurrentGeolocationButton = findViewById(R.id.current_location_button);
        getGeolocationFromMapButton = findViewById(R.id.map_location_button);
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesService
                .COLOR_PREFERENCES, Context.MODE_PRIVATE);
        PreferredGuiOptionsService preferredGuiOptionsService = new PreferredGuiOptionsService(sharedPreferences);
        preferredGuiOptionsService.setPreferredColorForButton(addShopButton);
        preferredGuiOptionsService.setPreferredColorForButton(getCurrentGeolocationButton);
        preferredGuiOptionsService.setPreferredColorForButton(getGeolocationFromMapButton);
    }

    private void registerButtonsListeners() {
        registerSubmitAddShopButtonListener();
        registerGetCurrentGeolocationButtonListener();
        registerGetGeolocationFromMapButtonListener();
    }

    private void registerSubmitAddShopButtonListener() {
        addShopButton.setOnClickListener(view -> {
            Shop shop = retrieveShopFromForm();
            addNewShop(shop);
            navigateToShopListActivity();
        });
    }

    private void registerGetCurrentGeolocationButtonListener() {
        getCurrentGeolocationButton.setOnClickListener(view -> {
            TextView shopLatitudeField = findViewById(R.id.shop_latitude);
            TextView shopLongitudeField = findViewById(R.id.shop_longitude);
            GeolocationService geolocationService = new GeolocationService(this);
            geolocationService.setCurrentGeolocationForFields(shopLatitudeField, shopLongitudeField);
        });
    }

    private void registerGetGeolocationFromMapButtonListener() {
        getGeolocationFromMapButton.setOnClickListener(view -> navigateToAddShopGeoPointFromGoogleMapActivity());
    }

    private void navigateToAddShopGeoPointFromGoogleMapActivity() {
        Intent intent = new Intent(this, AddShopGeoPointFromGoogleMapActivity.class);
        intent.putExtra(NAME, getNameFieldValue());
        intent.putExtra(DESCRIPTION, getDescriptionFieldValue());
        intent.putExtra(RANGE, getRangeFieldValue());

        startActivity(intent);
    }

    private Shop retrieveShopFromForm() {
        String id = UUID.randomUUID().toString();
        String name = getNameFieldValue();
        String description = getDescriptionFieldValue();
        double range = Double.parseDouble(getRangeFieldValue());
        double latitude = Double.parseDouble(getLatitudeFieldValue());
        double longitude = Double.parseDouble(getLongitudeFieldValue());

        return new Shop(id, name, description, range, latitude, longitude);
    }

    private void addNewShop(Shop shop) {
        FirestoreDatabaseService databaseService = new FirestoreDatabaseService();
        databaseService.insertShop(shop);
    }

    private void saveShopDataFromGoogleMapIntent() {
        Intent intent = getIntent();

        TextView nameField = findViewById(R.id.shop_name_add_text);
        nameField.setText(Strings.nullToEmpty(intent.getStringExtra(NAME)));
        TextView descriptionField = findViewById(R.id.shop_description_add_text);
        descriptionField.setText(Strings.nullToEmpty(intent.getStringExtra(DESCRIPTION)));
        TextView rangeField = findViewById(R.id.shop_range_add_text);
        rangeField.setText(Strings.nullToEmpty(intent.getStringExtra(RANGE)));
        TextView latitudeField = findViewById(R.id.shop_latitude_add_text);
        latitudeField.setText(Strings.nullToEmpty(intent.getStringExtra(LATITUDE)));
        TextView longitudeField = findViewById(R.id.shop_longitude_add_text);
        longitudeField.setText(Strings.nullToEmpty(intent.getStringExtra(LONGITUDE)));
    }

    private void navigateToShopListActivity() {
        Intent intent = new Intent(this, ShopsListActivity.class);
        startActivity(intent);
    }

    private String getNameFieldValue() {
        TextView nameField = findViewById(R.id.shop_name_add_text);
        return Strings.nullToEmpty(String.valueOf(nameField.getText()));
    }

    private String getDescriptionFieldValue() {
        TextView descriptionField = findViewById(R.id.shop_description_add_text);
        return Strings.nullToEmpty(String.valueOf(descriptionField.getText()));
    }

    private String getRangeFieldValue() {
        TextView rangeField = findViewById(R.id.shop_range_add_text);
        return Strings.nullToEmpty(String.valueOf(rangeField.getText()));
    }

    private String getLatitudeFieldValue() {
        TextView latitudeField = findViewById(R.id.shop_latitude_add_text);
        return Strings.nullToEmpty(String.valueOf(latitudeField.getText()));
    }

    private String getLongitudeFieldValue() {
        TextView longitudeField = findViewById(R.id.shop_longitude_add_text);
        return Strings.nullToEmpty(String.valueOf(longitudeField.getText()));
    }
}

package pl.dsamsel.mp2.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pl.dsamsel.mp2.R;
import pl.dsamsel.mp2.Services.PreferredGuiOptionsService;
import pl.dsamsel.mp2.Services.SharedPreferencesService;

public class ColorOptionsActivity extends AppCompatActivity {

    private SharedPreferencesService sharedPreferencesService;
    private Spinner backgroundColorSpinner;
    private Spinner fontColorSpinner;
    private Button submitButton;

    private static final Map<String, Integer> FONT_COLOR_MAP = new HashMap<String, Integer>(){
        {
            put("Red", Color.RED);
            put("Green", Color.GREEN);
            put("Blue", Color.BLUE);
            put("Black", Color.BLACK);
            put("Gray", Color.GRAY);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_options_activity);
        initSharedPreferences();
        fulfillSpinners();
        handlePreferredColorOptions();
        registerSubmitButtonListener();
    }

    private void handlePreferredColorOptions() {
        submitButton = findViewById(R.id.submit_color_preferences_button);
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesService
                .COLOR_PREFERENCES, Context.MODE_PRIVATE);
        PreferredGuiOptionsService preferredGuiOptionsService = new PreferredGuiOptionsService(sharedPreferences);
        preferredGuiOptionsService.setPreferredColorForButton(submitButton);
    }

    private void fulfillSpinners() {
        backgroundColorSpinner = findViewById(R.id.background_color_spinner);
        ArrayAdapter<String> backgroundColorAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, new ArrayList<String>(FONT_COLOR_MAP.keySet()));
        backgroundColorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        backgroundColorSpinner.setAdapter(backgroundColorAdapter);

        fontColorSpinner = findViewById(R.id.font_color_spinner);
        ArrayAdapter<String> fontColorAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, new ArrayList<String>(FONT_COLOR_MAP.keySet()));
        fontColorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fontColorSpinner.setAdapter(fontColorAdapter);
    }

    private void registerSubmitButtonListener() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveColorPreferencesToSharedPreferences(view);
                navigateToMainActivity(view);
            }
        });
    }

    private void saveColorPreferencesToSharedPreferences(View view) {
        String selectedFontColor = fontColorSpinner.getSelectedItem().toString();
        String selectedBackgroundColor = backgroundColorSpinner.getSelectedItem().toString();

        sharedPreferencesService.saveInt(SharedPreferencesService.BUTTON_FONT_COLOR_KEY,
                FONT_COLOR_MAP.get(selectedFontColor));
        sharedPreferencesService.saveInt(SharedPreferencesService.BUTTON_BACKGROUND_COLOR_KEY,
                FONT_COLOR_MAP.get(selectedBackgroundColor));
    }

    private void initSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesService
                        .COLOR_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesService = new SharedPreferencesService(sharedPreferences);
    }

    private void navigateToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

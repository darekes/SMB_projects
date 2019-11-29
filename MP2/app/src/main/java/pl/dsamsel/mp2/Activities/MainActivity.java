package pl.dsamsel.mp2.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import pl.dsamsel.mp2.R;
import pl.dsamsel.mp2.Services.BroadcastNotificationService;
import pl.dsamsel.mp2.Services.PreferredGuiOptionsService;
import pl.dsamsel.mp2.Services.SharedPreferencesService;

public class MainActivity extends AppCompatActivity {

    private Button productListButton;
    private Button optionsListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        handlePreferredColorOptions();
        registerButtonListeners();
        startNotificationService();
    }

    private void startNotificationService() {
        startService(new Intent(MainActivity.this, BroadcastNotificationService.class));
        Toast.makeText(this, "Broadcast notification service started!", Toast.LENGTH_SHORT).show();
    }

    private void handlePreferredColorOptions() {
        productListButton = findViewById(R.id.product_list_button);
        optionsListButton = findViewById(R.id.options_button);

        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesService
                .COLOR_PREFERENCES, Context.MODE_PRIVATE);
        PreferredGuiOptionsService preferredGuiOptionsService = new PreferredGuiOptionsService(sharedPreferences);
        preferredGuiOptionsService.setPreferredColorForButton(productListButton);
        preferredGuiOptionsService.setPreferredColorForButton(optionsListButton);
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
    }

    public void navigateToProductListActivity(View view){
        Intent intent = new Intent(this, ProductListActivity.class);
        startActivity(intent);
    }

    public void navigateToOptionsActivity(View view) {
        Intent intent = new Intent(this, ColorOptionsActivity.class);
        startActivity(intent);
    }
}

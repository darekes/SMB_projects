package pl.dsamsel.mp2.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import pl.dsamsel.mp2.R;
import pl.dsamsel.mp2.Services.DatabaseService;
import pl.dsamsel.mp2.Services.PreferredGuiOptionsService;
import pl.dsamsel.mp2.Services.SharedPreferencesService;

public class AddProductActivity extends AppCompatActivity {

    private Button submitAddProductButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product_activity);
        handlePreferredColorOptions();
        registerSubmitAddProductButtonListener();
    }

    private void handlePreferredColorOptions() {
        submitAddProductButton = findViewById(R.id.submit_add_product);
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesService
                .COLOR_PREFERENCES, Context.MODE_PRIVATE);
        PreferredGuiOptionsService preferredGuiOptionsService = new PreferredGuiOptionsService(sharedPreferences);
        preferredGuiOptionsService.setPreferredColorForButton(submitAddProductButton);
    }

    private void registerSubmitAddProductButtonListener() {
        submitAddProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewProductToDatabaseAndSendBroadcastIntent(view);
                navigateToProductListActivity(view);
            }
        });
    }

    private void addNewProductToDatabaseAndSendBroadcastIntent(View view) {
        DatabaseService databaseService = new DatabaseService(this);
        databaseService.init();

        TextView name = findViewById(R.id.name_add_text);
        TextView price = findViewById(R.id.price_add_text);
        TextView quantity = findViewById(R.id.quantity_add_text);
        CheckBox isBought = findViewById(R.id.is_bought_add_value);

        databaseService.insertProductAndReturnId(name.getText().toString(),
                Integer.parseInt(price.getText().toString()),
                Integer.parseInt(quantity.getText().toString()), isBought.isChecked());
    }

    private void navigateToProductListActivity(View view) {
        Intent intent = new Intent(this, ProductListActivity.class);
        startActivity(intent);
    }
}

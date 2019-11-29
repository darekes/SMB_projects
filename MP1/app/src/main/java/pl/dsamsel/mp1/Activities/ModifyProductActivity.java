package pl.dsamsel.mp1.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import pl.dsamsel.mp1.R;
import pl.dsamsel.mp1.Services.DatabaseService;
import pl.dsamsel.mp1.Services.PreferredGuiOptionsService;
import pl.dsamsel.mp1.Services.SharedPreferencesService;

public class ModifyProductActivity extends AppCompatActivity {

    private Button updateProductButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_product_activity);
        Intent intent = getIntent();
        fillFormWithCurrentValues(intent);
        handlePreferredColorOptions();
        registerUpdateProductButtonListener(intent.getIntExtra("id", 0));
    }

    private void handlePreferredColorOptions() {
        updateProductButton = findViewById(R.id.update_product_button);
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesService
                .COLOR_PREFERENCES, Context.MODE_PRIVATE);
        PreferredGuiOptionsService preferredGuiOptionsService = new PreferredGuiOptionsService(sharedPreferences);
        preferredGuiOptionsService.setPreferredColorForButton(updateProductButton);
    }

    private void fillFormWithCurrentValues(Intent intent) {
        TextView name = findViewById(R.id.name_edit_text);
        TextView price = findViewById(R.id.price_edit_text);
        TextView quantity = findViewById(R.id.quantity_edit_text);
        CheckBox isBought = findViewById(R.id.is_bought_edit_value);

        name.setText(intent.getStringExtra("name"));
        price.setText(String.valueOf(intent.getIntExtra("price", 0)));
        quantity.setText(String.valueOf(intent.getIntExtra("quantity", 0)));
        isBought.setChecked(intent.getBooleanExtra("isBought", false));
    }

    private void registerUpdateProductButtonListener(final long editableProductId) {
        updateProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateExistingProductInDatabase(view, editableProductId);
                navigateToProductListActivity(view);
            }
        });
    }

    private void updateExistingProductInDatabase(View view, long editableProductId) {
        DatabaseService databaseService = new DatabaseService(this);
        databaseService.init();

        TextView name = findViewById(R.id.name_edit_text);
        TextView price = findViewById(R.id.price_edit_text);
        TextView quantity = findViewById(R.id.quantity_edit_text);
        CheckBox isBought = findViewById(R.id.is_bought_edit_value);

        databaseService.updateProduct(editableProductId, name.getText().toString(),
                Integer.parseInt(price.getText().toString()),
                Integer.parseInt(quantity.getText().toString()), isBought.isChecked());
    }

    private void navigateToProductListActivity(View view) {
        Intent intent = new Intent(this, ProductListActivity.class);
        startActivity(intent);
    }
}
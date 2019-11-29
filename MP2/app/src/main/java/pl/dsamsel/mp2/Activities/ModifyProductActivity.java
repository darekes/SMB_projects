package pl.dsamsel.mp2.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import pl.dsamsel.mp2.Models.Product;
import pl.dsamsel.mp2.R;
import pl.dsamsel.mp2.Services.DatabaseService;
import pl.dsamsel.mp2.Services.PreferredGuiOptionsService;
import pl.dsamsel.mp2.Services.SharedPreferencesService;

public class ModifyProductActivity extends AppCompatActivity {

    private Button updateProductButton;
    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_product_activity);
        Intent intent = getIntent();
        initDatabase();
        fillFormWithReceivedValues(intent);
        handlePreferredColorOptions();
        registerUpdateProductButtonListener(intent.getIntExtra("productId", 0));
    }

    private void initDatabase() {
        databaseService = new DatabaseService(this);
        databaseService.init();
    }

    private void handlePreferredColorOptions() {
        updateProductButton = findViewById(R.id.update_product_button);
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesService
                .COLOR_PREFERENCES, Context.MODE_PRIVATE);
        PreferredGuiOptionsService preferredGuiOptionsService = new PreferredGuiOptionsService(sharedPreferences);
        preferredGuiOptionsService.setPreferredColorForButton(updateProductButton);
    }

    private void fillFormWithReceivedValues(Intent intent) {
        TextView name = findViewById(R.id.name_edit_text);
        TextView price = findViewById(R.id.price_edit_text);
        TextView quantity = findViewById(R.id.quantity_edit_text);
        CheckBox isBought = findViewById(R.id.is_bought_edit_value);

        Product product = databaseService.getProductById(intent.getIntExtra("productId", 0));

        name.setText(product.getName());
        price.setText(String.valueOf(product.getPrice()));
        quantity.setText(String.valueOf(product.getQuantity()));
        isBought.setChecked(product.isBought());
    }

    private void registerUpdateProductButtonListener(final int editableProductId) {
        updateProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateExistingProductInDatabase(view, editableProductId);
                navigateToProductListActivity(view);
            }
        });
    }

    private void updateExistingProductInDatabase(View view, int editableProductId) {
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

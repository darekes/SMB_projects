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

import java.util.UUID;

import pl.dsamsel.mp1.Models.Product;
import pl.dsamsel.mp1.R;
import pl.dsamsel.mp1.Services.FirestoreDatabaseService;
import pl.dsamsel.mp1.Services.PreferredGuiOptionsService;
import pl.dsamsel.mp1.Services.SharedPreferencesService;

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
                Product product = retrieveProduct();
                addNewProduct(product);
                sendBroadcastIntentWithExtras(product);
                navigateToProductListActivity(view);
            }
        });
    }

    private Product retrieveProduct() {
        TextView nameField = findViewById(R.id.name_add_text);
        TextView priceField = findViewById(R.id.price_add_text);
        TextView quantityField = findViewById(R.id.quantity_add_text);
        CheckBox isBoughtField = findViewById(R.id.is_bought_add_value);

        String id = UUID.randomUUID().toString();
        String name = nameField.getText().toString();
        int price = Integer.parseInt(priceField.getText().toString());
        int quantity = Integer.parseInt(quantityField.getText().toString());
        boolean isBought = isBoughtField.isChecked();

        return new Product(id, name, price, quantity, isBought);
    }

    private void addNewProduct(Product product) {
        FirestoreDatabaseService databaseService = new FirestoreDatabaseService();

        databaseService.insertProduct(product);
    }

    private void sendBroadcastIntentWithExtras(Product product) {
        Intent broadcastIntent = new Intent("pl.dsamsel.mp2.broadcast_intent");
        String permission = "pl.dsamsel.mp2.broadcast_intent.permission";
        broadcastIntent.putExtra("productName", product.getName());
        broadcastIntent.putExtra("productPrice", product.getPrice());
        broadcastIntent.putExtra("productQuantity", product.getQuantity());
        broadcastIntent.putExtra("isProductBought", product.isBought());
        sendBroadcast(broadcastIntent, permission);
    }

    private void navigateToProductListActivity(View view) {
        Intent intent = new Intent(this, ProductListActivity.class);
        startActivity(intent);
    }
}

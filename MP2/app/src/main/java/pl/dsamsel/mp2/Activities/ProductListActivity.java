package pl.dsamsel.mp2.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.dsamsel.mp2.Adapters.ProductAdapter;
import pl.dsamsel.mp2.Adapters.RecyclerViewClickListener;
import pl.dsamsel.mp2.Models.Product;
import pl.dsamsel.mp2.R;
import pl.dsamsel.mp2.Services.DatabaseService;
import pl.dsamsel.mp2.Services.PreferredGuiOptionsService;
import pl.dsamsel.mp2.Services.SharedPreferencesService;

public class ProductListActivity extends AppCompatActivity {

    private Button addProductButton;
    private Button goHomepageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list_activity);

        RecyclerView productList = findViewById(R.id.product_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        productList.setLayoutManager(linearLayoutManager);

        handlePreferredColorOptions();
        registerButtonsListeners();

        DividerItemDecoration decoration = new DividerItemDecoration(productList.getContext(),
                linearLayoutManager.getOrientation());
        productList.addItemDecoration(decoration);

        RecyclerViewClickListener recyclerViewClickListener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                handleDeleteModifyProductButtonsListeners(view, position);
            }
        };

        ProductAdapter productAdapter = new ProductAdapter(getProductList(), this,
                recyclerViewClickListener);
        productAdapter.notifyDataSetChanged();

        productList.setAdapter(productAdapter);
    }

    private void handlePreferredColorOptions() {
        addProductButton = findViewById(R.id.add_product_button);
        goHomepageButton = findViewById(R.id.go_homepage_button);

        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesService
                .COLOR_PREFERENCES, Context.MODE_PRIVATE);
        PreferredGuiOptionsService preferredGuiOptionsService = new PreferredGuiOptionsService(sharedPreferences);
        preferredGuiOptionsService.setPreferredColorForButton(addProductButton);
        preferredGuiOptionsService.setPreferredColorForButton(goHomepageButton);
    }

    private void registerButtonsListeners() {
        registerAddProductButtonListener();
        registerGoHomepageButtonListener();
    }

    private void handleDeleteModifyProductButtonsListeners(View view, int position) {
        DatabaseService databaseService = new DatabaseService(this);
        databaseService.init();
        Product productId = getProductList().get(position);

        switch (view.getId()) {
            case R.id.delete_product_button:
                databaseService.deleteProduct(productId.getId());
                navigateToProductListActivity(view);
                break;
            case R.id.modify_product_button:
                navigateToModifyProductActivityWithExtras(view, productId.getId());
                break;
        }
    }

    private void registerAddProductButtonListener() {
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToAddProductActivity(view);
            }
        });
    }

    private void registerGoHomepageButtonListener() {
        goHomepageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToMainActivity(view);
            }
        });
    }

    private List<Product> getProductList() {
        DatabaseService databaseService = new DatabaseService(this);
        databaseService.init();

        return databaseService.getAllProducts();
    }

    private void navigateToAddProductActivity(View view) {
        Intent intent = new Intent(this, AddProductActivity.class);
        startActivity(intent);
    }

    public void navigateToProductListActivity(View view){
        Intent intent = new Intent(this, ProductListActivity.class);
        startActivity(intent);
    }

    private void navigateToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void navigateToModifyProductActivityWithExtras(View view, int productId) {
        Intent intent = new Intent(this, ModifyProductActivity.class);
        intent.putExtra("productId", productId);

        startActivity(intent);
    }
}

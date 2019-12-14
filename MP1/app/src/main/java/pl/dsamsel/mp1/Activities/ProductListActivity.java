package pl.dsamsel.mp1.Activities;

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

import com.google.firebase.firestore.util.Consumer;

import java.util.List;

import pl.dsamsel.mp1.Adapters.ProductAdapter;
import pl.dsamsel.mp1.Adapters.RecyclerViewClickListener;
import pl.dsamsel.mp1.Models.Product;
import pl.dsamsel.mp1.R;
import pl.dsamsel.mp1.Services.FirestoreDatabaseService;
import pl.dsamsel.mp1.Services.PreferredGuiOptionsService;
import pl.dsamsel.mp1.Services.SharedPreferencesService;

public class ProductListActivity extends AppCompatActivity {

    private Button addProductButton;
    private Button goHomepageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list_activity);

        final RecyclerView productList = findViewById(R.id.product_list);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        productList.setLayoutManager(linearLayoutManager);

        final Context context = this;

        Consumer<List<Product>> consumer = new Consumer<List<Product>>() {
            @Override
            public void accept(List<Product> products) {

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
                ProductAdapter productAdapter = new ProductAdapter(products, context,
                        recyclerViewClickListener);
                productAdapter.notifyDataSetChanged();
                productList.setAdapter(productAdapter);
            }
        };

        getProductList(consumer);

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
        final int positionFinal = position;
        final View viewFinal = view;
        Consumer<List<Product>> consumer = new Consumer<List<Product>>() {
            @Override
            public void accept(List<Product> products) {
                Product touchedProduct = products.get(positionFinal);

                switch (viewFinal.getId()) {
                    case R.id.delete_product_button:
                        deleteProduct(touchedProduct.getId());
                        navigateToProductListActivity(viewFinal);
                        break;
                    case R.id.modify_product_button:
                        navigateToModifyProductActivityWithExtras(viewFinal, touchedProduct);
                        break;
                }
            }
        };
        getProductList(consumer);
    }

    private void deleteProduct(String productId) {
        FirestoreDatabaseService databaseService = new FirestoreDatabaseService();
        databaseService.deleteProduct(productId);
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

    private List<Product> getProductList(Consumer<List<Product>> productsList) {
        FirestoreDatabaseService databaseService = new FirestoreDatabaseService();
        return databaseService.getAllProducts(productsList);
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

    private void navigateToModifyProductActivityWithExtras(View view, Product product) {
        Intent intent = new Intent(this, ModifyProductActivity.class);
        intent.putExtra("id", product.getId());
        intent.putExtra("name", product.getName());
        intent.putExtra("price", product.getPrice());
        intent.putExtra("quantity", product.getQuantity());
        intent.putExtra("isBought", product.isBought());

        startActivity(intent);
    }
}

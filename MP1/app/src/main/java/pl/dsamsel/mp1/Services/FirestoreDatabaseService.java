package pl.dsamsel.mp1.Services;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.util.Consumer;

import java.util.ArrayList;
import java.util.List;

import pl.dsamsel.mp1.Models.Product;
import pl.dsamsel.mp1.Models.Shop;
import pl.dsamsel.mp1.Models.User;

public class FirestoreDatabaseService {

    private static final String DB_SERVICE_TAG = "DB_SERVICE";
    private static final String USERS_COLLECTION = "users";
    private static final String PRODUCTS_COLLECTION = "products";
    private static final String USER_SPECIFIC_PRODUCTS_COLLECTION = "user_specific_products";
    private static final String SHOPS_COLLECTION = "shops";
    private static final String USER_SPECIFIC_SHOPS_COLLECTION = "user_specific_shops";
    private List<Shop> myAllShops = new ArrayList<>();

    private String loggedInUserUid;
    private FirebaseFirestore firestoreDb;

    public FirestoreDatabaseService() {
        loggedInUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestoreDb = FirebaseFirestore.getInstance();
        initShops();
    }

    void insertUser(User user) {
        firestoreDb.collection(USERS_COLLECTION)
                .document(loggedInUserUid)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(DB_SERVICE_TAG, "User added to database!");
                    }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(DB_SERVICE_TAG, "Error when adding user to database", e);
                    }
                });
    }

    private void initShops() {
        myAllShops.add(new Shop("sadasd", "name1", "desc1", 100000.0, 52.22307701547054, 20.99426049739122));
        myAllShops.add(new Shop("sadasdadsd", "name2", "desc2", 100000.0, 37.3188, 122.1768));
    }

    public void insertShop(Shop shop) {
        firestoreDb.collection(SHOPS_COLLECTION)
                .document(loggedInUserUid)
                .collection(USER_SPECIFIC_SHOPS_COLLECTION)
                .document(shop.getId())
                .set(shop)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(DB_SERVICE_TAG, "Shop added to database!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(DB_SERVICE_TAG, "Error when adding shop to database.", e);
                    }
                });
    }

    public void updateShop(Shop shop) {
        firestoreDb.collection(SHOPS_COLLECTION)
                .document(loggedInUserUid)
                .collection(USER_SPECIFIC_SHOPS_COLLECTION)
                .document(shop.getId())
                .set(shop)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(DB_SERVICE_TAG, "Shop updated in database!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(DB_SERVICE_TAG, "Error when updating shop in database.", e);
                    }
                });
    }

    public void deleteShop(String shopId) {
        firestoreDb.collection(SHOPS_COLLECTION)
                .document(loggedInUserUid)
                .collection(USER_SPECIFIC_SHOPS_COLLECTION)
                .document(shopId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(DB_SERVICE_TAG, "Shop removed from database!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(DB_SERVICE_TAG, "Error when removing shop from database.", e);
                    }
                });
    }

    public List<Shop> getAllShops() {
        return myAllShops;
    }

    public List<Shop> getAllShops(final Consumer<List<Shop>> shopsList) {
        final List<Shop> allShops = new ArrayList<>();
        firestoreDb.collection(SHOPS_COLLECTION)
                .document(loggedInUserUid)
                .collection(USER_SPECIFIC_SHOPS_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                allShops.add(document.toObject(Shop.class));
                            }
                            shopsList.accept(allShops);
                        } else {
                            Log.w(DB_SERVICE_TAG, "Error when getting all shops.", task.getException());
                        }
                    }
                });

        return allShops;
    }

    public void insertProduct(Product product) {
        firestoreDb.collection(PRODUCTS_COLLECTION)
                .document(loggedInUserUid)
                .collection(USER_SPECIFIC_PRODUCTS_COLLECTION)
                .document(product.getId())
                .set(product)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(DB_SERVICE_TAG, "Product added to database!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(DB_SERVICE_TAG, "Error when adding product to database.", e);
                    }
                });
    }

    public void updateProduct(Product product) {
        firestoreDb.collection(PRODUCTS_COLLECTION)
                .document(loggedInUserUid)
                .collection(USER_SPECIFIC_PRODUCTS_COLLECTION)
                .document(product.getId())
                .set(product)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(DB_SERVICE_TAG, "Product updated in database!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(DB_SERVICE_TAG, "Error when updating product in database.", e);
                    }
                });
    }

    public void deleteProduct(String productId) {
        firestoreDb.collection(PRODUCTS_COLLECTION)
                .document(loggedInUserUid)
                .collection(USER_SPECIFIC_PRODUCTS_COLLECTION)
                .document(productId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(DB_SERVICE_TAG, "Product removed from database!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(DB_SERVICE_TAG, "Error when removing product from database.", e);
                    }
                });
    }

    public List<Product> getAllProducts(final Consumer<List<Product>> productsList) {
        final List<Product> allProducts = new ArrayList<>();
        firestoreDb.collection(PRODUCTS_COLLECTION)
                .document(loggedInUserUid)
                .collection(USER_SPECIFIC_PRODUCTS_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                allProducts.add(document.toObject(Product.class));
                            }
                            productsList.accept(allProducts);
                        } else {
                            Log.w(DB_SERVICE_TAG, "Error when getting all products.", task.getException());
                        }
                    }
                });

        return allProducts;
    }
}

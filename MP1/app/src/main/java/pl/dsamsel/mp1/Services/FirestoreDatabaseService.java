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
import pl.dsamsel.mp1.Models.User;

public class FirestoreDatabaseService {

    private static final String DB_SERVICE_TAG = "DB_SERVICE";
    private static final String USERS_COLLECTION = "users";
    private static final String PRODUCTS_COLLECTION = "products";
    private static final String USER_SPECIFIC_PRODUCTS_COLLECTION = "user_specific_products";

    private String loggedInUserUid;
    private FirebaseFirestore firestoreDb;

    public FirestoreDatabaseService() {
        loggedInUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestoreDb = FirebaseFirestore.getInstance();
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

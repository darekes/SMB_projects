package pl.dsamsel.mp1.Services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import pl.dsamsel.mp1.Database.DatabaseHelper;
import pl.dsamsel.mp1.Models.Product;

public class DatabaseService {

    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DatabaseService(Context context) {
        this.context = context;
    }

    public DatabaseService init() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insertProduct(String name, int price, int quantity, boolean isBought) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.COLUMN_NAME, name);
        contentValue.put(DatabaseHelper.COLUMN_PRICE, price);
        contentValue.put(DatabaseHelper.COLUMN_QUANTITY, quantity);
        contentValue.put(DatabaseHelper.COLUMN_IS_BOUGHT, isBought);

        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public int updateProduct(long id, String name, int price, int quantity, boolean isBought) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_NAME, name);
        contentValues.put(DatabaseHelper.COLUMN_PRICE, price);
        contentValues.put(DatabaseHelper.COLUMN_QUANTITY, quantity);
        contentValues.put(DatabaseHelper.COLUMN_IS_BOUGHT, isBought);

        return database.update(DatabaseHelper.TABLE_NAME, contentValues,
                DatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    public void deleteProduct(long id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COLUMN_ID + "=" + id, null);
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<Product>();
        Cursor cursor = fetchProducts();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            int price = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRICE));
            int quantity = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_QUANTITY));
            boolean isBought = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_IS_BOUGHT)) == 1;
            productList.add(new Product(id, name, price, quantity, isBought));
        }

        cursor.close();
        return productList;
    }

    private Cursor fetchProducts() {
        String[] columns = new String[] { DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME,
                DatabaseHelper.COLUMN_PRICE, DatabaseHelper.COLUMN_QUANTITY,
                DatabaseHelper.COLUMN_IS_BOUGHT};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null,
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public void dropTable(String tableName) {
        database.execSQL("DROP TABLE IF EXISTS " + tableName);
    }
}

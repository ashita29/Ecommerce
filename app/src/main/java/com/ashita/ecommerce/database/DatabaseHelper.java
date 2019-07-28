package com.ashita.ecommerce.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import com.ashita.ecommerce.model.Product;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteAssetHelper {

    private static final  String DB_NAME = "ecommerce.db";
    private static final int DB_VER=1;

    public DatabaseHelper(Context context)
    {
        super(context, DB_NAME,null,DB_VER);
    }

    public void addToCart(Product product)
    {
       SQLiteDatabase db = getReadableDatabase();
       String query = String.format("INSERT OR REPLACE INTO OrderDetail(UserEmail, ProductId, ProductName, ProductPrice, ProductShippingPrice, ProductImage,ProductQuantity) VALUES('%s','%s','%s','%s','%s','%s','%s');",
               product.getUserEmail(),
               product.getId(),
               product.getName(),
               product.getPrice(),
               product.getShippingPrice(),
               product.getImageUrl(),
               product.getQuantity());

       db.execSQL(query);
    }

    public List<Product> getCartDetails(String userEmail)
    {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"UserEmail","ProductId","ProductName","ProductPrice","ProductShippingPrice","ProductImage","ProductQuantity"};
        String sqlTable = "OrderDetail";

        qb.setTables(sqlTable);
        Cursor cursor = qb.query(db,sqlSelect,"UserEmail=?",new String[]{userEmail},null,null,null);
        final List<Product> result = new ArrayList<>();
        if(cursor.moveToFirst())
        {
            do {
                result.add(new Product(
                        cursor.getString(cursor.getColumnIndex("UserEmail")),
                        cursor.getString(cursor.getColumnIndex("ProductId")),
                        cursor.getString(cursor.getColumnIndex("ProductName")),
                        cursor.getString(cursor.getColumnIndex("ProductPrice")),
                        cursor.getString(cursor.getColumnIndex("ProductShippingPrice")),
                        cursor.getString(cursor.getColumnIndex("ProductImage")),
                        cursor.getString(cursor.getColumnIndex("ProductQuantity"))));
            }while (cursor.moveToNext());
        }
        return result;
    }

    public  void deleteFromCart(String userEmail)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail WHERE UserEmail='%s'",userEmail);
        db.execSQL(query);
    }

    public int getCountofItems(String userEmail)
    {
        int count=0;
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT COUNT(*) FROM OrderDetail WHERE UserEmail='%s'",userEmail);
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            do {
                count= cursor.getInt(0);
            }while (cursor.moveToNext());
        }
        return count;
    }

    public void upgradeCart(Product product)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("UPDATE OrderDetail SET ProductQuantity = '%s' WHERE UserEmail = '%s' AND ProductId = '%s'",product.getQuantity(),product.getUserEmail(),product.getId());
        db.execSQL(query);
    }
}

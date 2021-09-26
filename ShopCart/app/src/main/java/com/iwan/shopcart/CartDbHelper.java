package com.iwan.shopcart;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CartDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "shopcart.db";
    public static final String TABLE_NAME = "carts";

    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_ITEM = "item";
    public static final String COLUMN_NAME_NUMBER_OF_ITEMS = "number_of_items";
    public static final String COLUMN_NAME_PRICE = "price";

    public CartDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COLUMN_NAME_ID + " INTEGER PRIMARY KEY, " + COLUMN_NAME_NAME + " TEXT, " + COLUMN_NAME_ITEM + " TEXT, " + COLUMN_NAME_NUMBER_OF_ITEMS + " INTEGER, " + COLUMN_NAME_PRICE + " INTEGER)";

        db.execSQL(CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long addCart(Cart cart) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME_NAME, cart.name);
        values.put(COLUMN_NAME_ITEM, cart.item);
        values.put(COLUMN_NAME_NUMBER_OF_ITEMS, cart.numberOfItems);
        values.put(COLUMN_NAME_PRICE, cart.price);

        long id = db.insert(TABLE_NAME, null, values);

        return id;
    }

    @SuppressLint("Range")
    public ArrayList<Cart> getAllCarts() {
        ArrayList<Cart> carts = new ArrayList<>();

        String SELECT_QUERY = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Cart cart = new Cart();
                cart.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID)));
                cart.name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NAME));
                cart.item = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ITEM));
                cart.numberOfItems = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NUMBER_OF_ITEMS)));
                cart.price = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PRICE)));

                carts.add(cart);
            } while (cursor.moveToNext());
        }

        return carts;
    }

    public void deleteCart(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_NAME_ID + "=" + id, null);
    }
}

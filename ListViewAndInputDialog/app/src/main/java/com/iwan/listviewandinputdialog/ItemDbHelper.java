package com.iwan.listviewandinputdialog;

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

public class ItemDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "listview.db";
    public static final String TABLE_NAME = "items";

    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_PHONE = "phone";

    public ItemDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COLUMN_NAME_ID + " INTEGER PRIMARY KEY, " + COLUMN_NAME_NAME + " TEXT, " + COLUMN_NAME_PHONE + " TEXT)";

        db.execSQL(CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long addItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME_NAME, item.getName());
        values.put(COLUMN_NAME_PHONE, item.getPhone());

        long id = db.insert(TABLE_NAME, null, values);

        return id;
    }

    @SuppressLint("Range")
    public ArrayList<Item> getAllItems() {
        ArrayList<Item> items = new ArrayList<>();

        String SELECT_QUERY = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID))));
                item.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NAME)));
                item.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PHONE)));

                items.add(item);
            } while (cursor.moveToNext());
        }

        return items;
    }

    public void deleteItem(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_NAME_ID + "=" + id, null);
    }
}

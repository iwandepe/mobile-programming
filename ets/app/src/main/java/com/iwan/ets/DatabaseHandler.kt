package com.iwan.ets

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "ETS"
        private val TABLE_CONTACTS = "Contacts"
        private val KEY_NAME = "name"
        private val KEY_PHONE = "phone"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //creating table with fields
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_NAME + " TEXT,"
                + KEY_PHONE + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS)
        onCreate(db)
    }


    //method to insert data
    fun addContact(contact: ContactModel):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, contact.name) // EmpModelClass Name
        contentValues.put(KEY_PHONE,contact.phone ) // EmpModelClass Phone
        // Inserting Row
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
    //method to read data
    @SuppressLint("Range")
    fun viewContacts():List<ContactModel>{
        val contactList:ArrayList<ContactModel> = ArrayList<ContactModel>()
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var name: String
        var phone: String
        if (cursor.moveToFirst()) {
            do {
                name = cursor.getString(cursor.getColumnIndex("name"))
                phone = cursor.getString(cursor.getColumnIndex("phone"))
                val contact = ContactModel(name = name, phone = phone)
                contactList.add(contact)
            } while (cursor.moveToNext())
        }
        db.close()
        return contactList
    }
    //method to update data
    fun updateContact(oldName: String, contact: ContactModel):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, contact.name) // EmpModelClass Name
        contentValues.put(KEY_PHONE,contact.phone ) // EmpModelClass Email

        // Updating Row
        val success = db.update(TABLE_CONTACTS, contentValues,"name='" + oldName + "'",null)
//        val query = "UPDATE " + TABLE_CONTACTS + " SET name='" + contact.name + "', phone='" + contact.phone + "' WHERE name='"+ contact.name+"'"
//        db.execSQL("UPDATE Contacts SET name='cobaganti', phone='081881' WHERE name='erik'")
//        val success = 1
                //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
    //method to delete data
    fun deleteContact(contact: ContactModel):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, contact.name) // EmpModelClass UserId
        // Deleting Row
        val success = db.delete(TABLE_CONTACTS,"name='" + contact.name + "'",null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
}
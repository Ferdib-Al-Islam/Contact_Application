package com.dreamsoft.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import com.dreamsoft.contacts.Contact;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contacts_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Contact.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Contact.TABLE_NAME);

        onCreate(db);
    }

    public long insertContact(String name,String number, String email, String favourite) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Contact.COLUMN_NAME,name);
        values.put(Contact.COLUMN_NUMBER,number);
        values.put(Contact.COLUMN_EMAIL,email);
        values.put(Contact.COLUMN_FAVOURITE,favourite);

        long id = db.insert(Contact.TABLE_NAME, null, values);

        db.close();

        return id;
    }

    public Contact getContact(long id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Contact.TABLE_NAME,
                new String[]{Contact.COLUMN_ID, Contact.COLUMN_NAME, Contact.COLUMN_NUMBER,Contact.COLUMN_EMAIL,Contact.COLUMN_FAVOURITE},
                Contact.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare contact object
        Contact contact = new Contact(
                cursor.getInt(cursor.getColumnIndex(Contact.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Contact.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(Contact.COLUMN_NUMBER)),
                cursor.getString(cursor.getColumnIndex(Contact.COLUMN_EMAIL)),
                cursor.getString(cursor.getColumnIndex(Contact.COLUMN_FAVOURITE)));

        cursor.close();

        return contact;
    }

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + Contact.TABLE_NAME + " ORDER BY " +
                Contact.COLUMN_FAVOURITE + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(cursor.getInt(cursor.getColumnIndex(Contact.COLUMN_ID)));
                contact.setName(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_NAME)));
                contact.setNumber(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_NUMBER)));
                contact.setEmail(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_EMAIL)));
                contact.setFavourite(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_FAVOURITE)));

                contacts.add(contact);
            } while (cursor.moveToNext());
        }

        db.close();

        return contacts;
    }

    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + Contact.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Contact.COLUMN_NAME,contact.getName());
        values.put(Contact.COLUMN_NUMBER,contact.getNumber());
        values.put(Contact.COLUMN_EMAIL,contact.getEmail());
        values.put(Contact.COLUMN_FAVOURITE,contact.getFavourite());

        // updating row
        return db.update(Contact.TABLE_NAME, values, Contact.COLUMN_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});
    }

    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Contact.TABLE_NAME, Contact.COLUMN_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});
        db.close();
    }
}
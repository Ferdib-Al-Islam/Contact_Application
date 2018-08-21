package com.dreamsoft.contacts;

/**
 * Created by Mizan on 5/20/2018.
 */
public class Contact {
    public static final String TABLE_NAME = "contacts";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME= "name";
    public static final String COLUMN_NUMBER= "number";
    public static final String COLUMN_EMAIL= "email";
    public static final String COLUMN_FAVOURITE= "favourite";


    private int id;
    private String name,number,email,favourite;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_NUMBER + " TEXT,"
                    + COLUMN_EMAIL + " TEXT,"
                    + COLUMN_FAVOURITE + " TEXT"
                    + ")";

    public Contact() {
    }

    public Contact(int id, String name, String number, String email,String favourite) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.email = email;
        this.favourite = favourite;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getEmail() {
        return email;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }
}
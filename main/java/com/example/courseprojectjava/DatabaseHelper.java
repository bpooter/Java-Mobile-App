package com.example.courseprojectjava;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    //database name
    public static final String dbName = "CryptidCo.db";

    //customer table attributes
    public static final String customerTableName = "customerAccounts";
    public static final String customerColID = "customerID";
    public static final String customerColFName = "firstName";
    public static final String customerColLName = "lastName";
    public static final String customerColUserName = "userName";
    public static final String customerColPassword = "password";


    //------items table attributes

    //------was going to use a item table as well but was getting dropped frames messages

    //------tried looking into multithreading to use for database calls but settled for smaller database

    /*public static final String itemTableName = "item";
    public static final String itemColID = "itemID";
    public static final String itemColCategory = "category";
    public static final String itemColDesc = "description";
    public static final String itemColPrice = "price";
    */

    //cart table attributes
    public static final String cartTableName = "cart";
    public static final String cartColID = "cartID";
    public static final String cartColItemID = "itemID";
    public static final String cartColPrice = "price";
    public static final String cartColSize = "size";
    public static final String cartCustomerID = "customerID";
    public static final String cartQuantity = "quantity";


    public DatabaseHelper(Context context){
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("Create Table "  + customerTableName + "(customerID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, firstName TEXT, lastName TEXT, userName TEXT, password TEXT);");
        //sqLiteDatabase.execSQL("CREATE TABLE " + itemTableName + "(itemID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, category TEXT, description TEXT, price REAL);");
        sqLiteDatabase.execSQL("CREATE TABLE " + cartTableName + "(cartID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, itemID INTEGER, category TEXT, description TEXT, size TEXT, price REAL, quantity INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("Drop Table IF Exists " + customerTableName);
        //sqLiteDatabase.execSQL("Drop Table IF Exists " + itemTableName);
        sqLiteDatabase.execSQL("Drop Table IF Exists " + cartTableName);
        onCreate(sqLiteDatabase);
    }

    public void resetTables(){
        SQLiteDatabase database = this.getWritableDatabase();

        database.execSQL("Drop Table IF Exists " + customerTableName);
        //database.execSQL("Drop Table IF Exists " + itemTableName);
        database.execSQL("Drop Table IF Exists " + cartTableName);

        database.execSQL("Create Table "  + customerTableName + "(customerID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, firstName TEXT, lastName TEXT, userName TEXT, password TEXT);");
        //database.execSQL("CREATE TABLE " + itemTableName + "(itemID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, category TEXT, description TEXT, price REAL);");
        database.execSQL("CREATE TABLE " + cartTableName + "(cartID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, itemID INTEGER, customerID INTEGER REFERENCES customer(customerID) , description TEXT, size TEXT, price REAL, quantity INTEGER);");

    }

    public void resetCartTable(){
        SQLiteDatabase database = this.getWritableDatabase();

        //database.execSQL("Drop Table IF Exists " + customerTableName);
        //database.execSQL("Drop Table IF Exists " + itemTableName);
        database.execSQL("Drop Table IF Exists " + cartTableName);

        //database.execSQL("Create Table "  + customerTableName + "(customerID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, firstName TEXT, lastName TEXT, userName TEXT, password TEXT);");
        //database.execSQL("CREATE TABLE " + itemTableName + "(itemID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, category TEXT, description TEXT, price REAL);");
        database.execSQL("CREATE TABLE " + cartTableName + "(cartID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, itemID INTEGER, customerID INTEGER REFERENCES customer(customerID) , description TEXT, size TEXT, price REAL, quantity INTEGER);");

    }



    public Long insertCustomerData(String fName, String lName, String userName, String password){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contents = new ContentValues();
        contents.put("firstName", fName);
        contents.put("lastName", lName);
        contents.put("userName", userName);
        contents.put("password", password);

        Long pos = database.insert(customerTableName, null, contents);

        return pos;
    }


    public long insertCartData(int itemID, double price, String size, int customerID, int quantity, String desc){

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contents = new ContentValues();

        contents.put("itemID", itemID);
        contents.put("customerID", customerID);
        contents.put("description", desc);
        contents.put("size", size);
        contents.put("quantity", quantity);
        contents.put("price", price);

        Long pos = database.insert(cartTableName, null, contents);

        return pos;
    }

    public Cursor totalCart(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor table = database.rawQuery("SELECT (quantity*price) AS subtotal FROM cart", null);
        return table;
    }

    public Cursor getCart(Integer customerId){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor table = database.rawQuery("Select description, size, quantity, price  from cart", null);

        return table;
    }

    public Cursor getCustomerID(String userName){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor table = database.rawQuery("SELECT " + customerColID + " FROM " + customerTableName + " WHERE userName = ?", new String[]{userName});
        return table;
    }


    //method for checking if customer account exists for login validation
    public int accountExists(String userName, String password){

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = null;

        try{

            String[] selectionArgs = {userName, password};
            cursor = database.rawQuery("SELECT * FROM " + customerTableName +" WHERE userName = ? AND password = ?", selectionArgs);

            if (cursor!=null && cursor.getCount()>0){
                int count = cursor.getCount();
                return count;
            }
            else{
                return -1;
            }

        }
        catch(NullPointerException e)
        {
            //using as a log to see error and cursor count
            System.out.println("There was an error in the account exists method");
            System.out.println( "Caught null pointer -Cursor count is " + cursor.getCount());
            return -1;
        }
    }


}

/*
    public void resetTable(){
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DROP TABLE IF Exists customerAccounts");
        database.execSQL("DROP TABLE IF Exists ticketData");
        database.execSQL("Create Table "  + customerTableName + "(customerID INTEGER PRIMARY KEY, custName TEXT NOT NULL, address TEXT, city TEXT, state TEXT, zip TEXT) ");
        database.execSQL("CREATE TABLE " + ticketTableName + "(ticketID INTEGER PRIMARY KEY, destination TEXT, departure TEXT, customerIDOnTicket INTEGER)");
    }

    public Integer deleteCustomerByID(Integer id){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete("customerAccounts", "customerID = ?", new String[]{id.toString()});
    }

    public Integer deleteTicketByID(Integer id){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete("ticketData", "ticketID = ?", new String[]{id.toString()});
    }

    public boolean updateCustomerById(Integer id, String name, String address, String city, String state, String zip){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contents = new ContentValues();
        contents.put("customerID", id);
        contents.put("custName", name);
        contents.put("address", address);
        contents.put("city", city);
        contents.put("state", state);
        contents.put("zip", zip);

        database.update("customerAccounts", contents, "customerID = ?", new String[]{id.toString()});

        return true;
    }

    public boolean updateTicketById(Integer id, String destination, String departure, Integer customerID){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contents = new ContentValues();
        contents.put("ticketID", id);
        contents.put("destination", destination);
        contents.put("departure", departure);
        contents.put("customerIDOnTicket", customerID);

        database.update("ticketData", contents, "ticketID = ?", new String[]{id.toString()});

        return true;
    }

    public Cursor selectCustomerById(Integer id){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor table = database.rawQuery("Select * from customerAccounts Where customerID = ?", new String[]{id.toString()});

        return table;
    }
    public Cursor selectTicketById(Integer id){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor table = database.rawQuery("Select * from ticketData Where ticketID = ?", new String[]{id.toString()});

        return table;
    }

    public Cursor selectAllTickets(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor allInfo = database.rawQuery("SELECT customerAccounts.custName, customerAccounts.customerID, ticketData.ticketID, ticketData.destination, ticketData.departure FROM customerAccounts, ticketData WHERE customerAccounts.customerID = ticketData.customerIDOnTIcket", null);

        return allInfo;
    }

    public Long insertItemData(String category, String desc, double price){

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contents = new ContentValues();

        contents.put("category", category);
        contents.put("description", desc);
        contents.put("price", price);

        Long pos = database.insert(itemTableName, null, contents);

        return pos;

    }

    public Cursor selectAllItems(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor table = database.rawQuery("Select * from " + itemTableName, null);

        return table;
    }


 */
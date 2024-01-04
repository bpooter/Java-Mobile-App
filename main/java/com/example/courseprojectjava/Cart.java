package com.example.courseprojectjava;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Cart extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    TextView textViewCartPrompt;
    TextView textViewCartContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getSupportActionBar().setTitle("Cart");

        Button buttonPlaceOrder = (Button) findViewById(R.id.buttonPlaceOrder);
        textViewCartPrompt = (TextView) findViewById(R.id.textViewCartPrompt);
        textViewCartContents = (TextView) findViewById(R.id.textViewCartContents);
        databaseHelper = new DatabaseHelper(this);

        showItems();

        buttonPlaceOrder.setOnClickListener(v -> {
            showCartTotal();

        });

    }

    public void showItems() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userName = sharedPreferences.getString("userName", null);

        textViewCartPrompt.setText(userName + " Cart:");
        int customerID = sharedPreferences.getInt("customerID", 0);
        Cursor table = databaseHelper.getCart(customerID);

        if (table.getCount() == 0) {
            System.out.println("No records in the table to show");
        } else {
            System.out.println("Displaying " + table.getCount() + " record(s) from table");
            StringBuffer buffer = new StringBuffer();

            if (table.moveToFirst()) {
                while (!table.isAfterLast()) {
                    //String data = table.getString(table.getColumnIndex("price"));
                    buffer.append(String.format("Item Description: %s\nSize: %s\nQuantity: %d\nPrice: $%.2f\n\n", table.getString(0), table.getString(1), table.getInt(2), table.getDouble(3)));
                    table.moveToNext();
                }
                String tableContents = buffer.toString();
                textViewCartContents.setText(tableContents);
                System.out.println(tableContents);
            }
        }

        table.close();

    }

    public void showCartTotal(){

        Cursor table = databaseHelper.totalCart();
        double subTotal = 0;
        double tax = 0;
        double total = 0;

        if (table.moveToFirst()) {
            while (!table.isAfterLast()) {
                subTotal += table.getDouble(0);
                table.moveToNext();
            }

            tax = subTotal * 0.07;
            total = subTotal + tax;

            AlertDialog.Builder alertBox = new AlertDialog.Builder(Cart.this);
            alertBox.setCancelable(true);
            alertBox.setTitle("Total For Order");
            alertBox.setMessage(String.format("Subtotal Due: $%.2f\nTax Due: $%.2f\nTotal Due: $%.2f", subTotal, tax, total));
            alertBox.show();
        }
    }
    //method for inflating menu when chosen
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    //method for handling menu options chosen
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.item1) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            boolean isLoggedIn = sharedPreferences.getBoolean("loggedIn", false);

            if (isLoggedIn) {
                editor.putBoolean("loggedIn", false);
                Toast toast = Toast.makeText(Cart.this, "Logged Out Successfully", Toast.LENGTH_LONG);
                toast.show();
                editor.commit();
                databaseHelper.resetCartTable();

                startActivity(new Intent(Cart.this, LoginPage.class));
            } else {
                Toast toast = Toast.makeText(Cart.this, "You aren't logged in currently", Toast.LENGTH_LONG);
                toast.show();
            }
            return true;
        } else if (id == R.id.item2) {
            Toast.makeText(this, "Thank you for a great semester Mr. Dorsett!", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.item3) {
            startActivity(new Intent(Cart.this, CryptidStorePage.class));
            return true;
        } else if (id == R.id.item4) {
            startActivity(new Intent(Cart.this, MainActivity.class));
            return true;
        } else if (id == R.id.item5) {
            startActivity(new Intent(Cart.this, Cart.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
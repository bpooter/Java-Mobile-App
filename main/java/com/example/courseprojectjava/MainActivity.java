package com.example.courseprojectjava;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.BlockingDeque;
import java.util.jar.JarOutputStream;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    Handler handler = null;

    //this app needs multithreading because it drops a lot of frames database calls need to be off UI thread
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean loggedIn = false;
        boolean internalLogIn = false;
        getSupportActionBar().setTitle("Home");

        handler = new Handler();

        //populating table with username and password in alert box
        databaseHelper = new DatabaseHelper(this);


        databaseHelper.resetTables();
        databaseHelper.close();


        //reference to button and textView objects on screen
        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        Button buttonGuest = (Button) findViewById(R.id.buttonGuest);
        TextView textViewClickHere = (TextView) findViewById(R.id.textViewClickHere);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();


            populateCustomerTable();
            //populateCart();

        //getting value of stored login status to show the stored login on first launch
        loggedIn =sharedPreferences.getBoolean("loggedIn", false);
        if (loggedIn){
            internalLogIn = true;
            startActivity(new Intent(MainActivity.this, CryptidStorePage.class));
        }

        //getting userName to show in toast
        String userName = sharedPreferences.getString("userName", null);

        //if internal login is false then show stored login for first launch
        if(!internalLogIn){
            showLogin();
        }
        //show toast with user name if loggedin
        if(loggedIn){
            Toast toast = Toast.makeText(MainActivity.this, "Welcome to Cryptid Co. , " + userName + "!", Toast.LENGTH_LONG);
            toast.show();
        }


        //click listener for guest button
        buttonGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //boolean value assigned to ensure that guest cannot attempt to place an order and can only browse
                boolean loggedIn = false;

                //storing values in shared preferences to ensure that user is not logged in through guest button
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userName", null);
                editor.commit();
                editor.putString("passWord", null);
                editor.commit();
                editor.putBoolean("loggedIn", loggedIn);
                editor.commit();

                startActivity(new Intent(MainActivity.this, CryptidStorePage.class));

            }
        });

        //click listener for login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //launching login page activity
                startActivity(new Intent(MainActivity.this, LoginPage.class));
            }
        });

        //click listener for textView for create an account
        textViewClickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launching create an account activity
                startActivity(new Intent(MainActivity.this, CreateAccount.class));
            }
        });
    }
    //method to populate an initial test value of a login and password
    public void populateCustomerTable(){
        databaseHelper.insertCustomerData("John", "Doe", "root", "password");
        databaseHelper.close();
    }

    public void populateCart(){
        databaseHelper.insertCartData(1, 15.00, null, 1, 2, "Moth Man Coffee Mug");
        databaseHelper.close();
    }


    //method for displaying stored login information in an alert dialog
    public void showLogin(){
        AlertDialog.Builder alertBox = new AlertDialog.Builder(MainActivity.this);
        alertBox.setCancelable(true);
        alertBox.setTitle("Login for user already in system");
        alertBox.setMessage("username: root\npassword: password\n\ncannot place an order without being signed in to account");
        alertBox.show();
    }
    //method for inflating menu when chosen
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

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
                Toast toast = Toast.makeText(MainActivity.this, "Logged Out Successfully", Toast.LENGTH_LONG);
                toast.show();
                editor.commit();
                databaseHelper.resetCartTable();
                databaseHelper.close();

                startActivity(new Intent(MainActivity.this, LoginPage.class));
            }
            else{
                Toast toast = Toast.makeText(MainActivity.this, "You aren't logged in currently", Toast.LENGTH_LONG);
                toast.show();
            }
            return true;
        }
        else if (id == R.id.item2) {
            Toast.makeText(this, "Thank you for a great semester Mr. Dorsett!", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.item3) {
            startActivity(new Intent(MainActivity.this, CryptidStorePage.class));
            return true;
        } else if (id == R.id.item4) {
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            return true;
        } else if (id == R.id.item5) {
            startActivity(new Intent(MainActivity.this, Cart.class));
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

}

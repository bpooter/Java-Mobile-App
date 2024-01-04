package com.example.courseprojectjava;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPage extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        //object references
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        EditText editTextUserName = (EditText) findViewById(R.id.editTextLoginUser);
        EditText editTextPassword = (EditText) findViewById(R.id.editTextLoginPass);
        Button loginButton = (Button) findViewById(R.id.buttonAttemptLogin);
        getSupportActionBar().setTitle("Login");


        //login button listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //userName and password reference
                String userName;
                String password;

                //initializing userName and password with values from editText and checking values
                try {
                    userName = editTextUserName.getText().toString();
                    password = editTextPassword.getText().toString();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userName", userName);
                    editor.commit();
                    editor.putString("password", password);
                    editor.commit();
                } catch (Exception e) {

                }

                String storedUserName = sharedPreferences.getString("userName", null);
                String storedPassWord = sharedPreferences.getString("password", null);

                //passing stored userName and password to database helper for verifying if valid login
                int loggedIn = databaseHelper.accountExists(storedUserName, storedPassWord);
                databaseHelper.close();

                //editor to store if login was successful
                SharedPreferences.Editor editor = sharedPreferences.edit();

                //testing if loggedIn from database helper was successful, updating loggedIn sharedPreference and launching Store page activity
                if (loggedIn != -1) {
                    startActivity(new Intent(LoginPage.this, CryptidStorePage.class));
                    editor.putBoolean("loggedIn", true);
                    editor.commit();

                    //if loggedIn is error value show a dialog that says failed to login
                } else if (loggedIn == -1) {
                    AlertDialog.Builder alertBox = new AlertDialog.Builder(LoginPage.this);
                    alertBox.setCancelable(true);
                    alertBox.setTitle("Failed to log in");
                    alertBox.setMessage("You couldn't be logged in" + loggedIn);
                    alertBox.show();
                    editor.putBoolean("loggedIn", false);
                    editor.commit();
                }
            }
        });
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
                Toast toast = Toast.makeText(LoginPage.this, "Logged Out Successfully", Toast.LENGTH_LONG);
                toast.show();
                editor.commit();
                databaseHelper.resetCartTable();
                databaseHelper.close();

                startActivity(new Intent(LoginPage.this, LoginPage.class));
            }
            else{
                Toast toast = Toast.makeText(LoginPage.this, "You aren't logged in currently", Toast.LENGTH_LONG);
                toast.show();
            }
            return true;
        }
        else if (id == R.id.item2) {
            Toast.makeText(this, "Thank you for a great semester Mr. Dorsett!", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.item3) {
            startActivity(new Intent(LoginPage.this, CryptidStorePage.class));
            return true;
        } else if (id == R.id.item4) {
            startActivity(new Intent(LoginPage.this, MainActivity.class));
            return true;
        } else if (id == R.id.item5) {
            startActivity(new Intent(LoginPage.this, Cart.class));
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }
}
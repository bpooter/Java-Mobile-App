package com.example.courseprojectjava;

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

public class CreateAccount extends AppCompatActivity {

    //database helper to insert new account into customer table
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        databaseHelper = new DatabaseHelper(this);

        //object references of UI
        EditText editTextFName = (EditText) findViewById(R.id.editTextFirstName);
        EditText editTextLName = (EditText) findViewById(R.id.editTextLastName);
        EditText editTextUserName = (EditText) findViewById(R.id.editTextUserName);
        EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        Button buttonNewAccount = (Button) findViewById(R.id.buttonNewAccount);
        getSupportActionBar().setTitle("Create Account");

        //submit button listener on create account activity
        buttonNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //attributes for customer declaration
                String firstName;
                String lastName;
                String userName;
                String password;

                //try catch handling getting values from UI and initializing attributes
                try {
                    firstName = editTextFName.getText().toString();
                    lastName = editTextLName.getText().toString();
                    userName = editTextUserName.getText().toString();
                    password = editTextPassword.getText().toString();

                    if (firstName.isEmpty()){
                        throw new Exception();
                    }
                    if (lastName.isEmpty()){
                        throw new Exception();
                    }
                    if (userName.isEmpty()){
                        throw new Exception();
                    }
                    if (password.isEmpty()){
                        throw new Exception();
                    }

                    //inserting values into databaseHelper to get a value to test against
                    Long position = databaseHelper.insertCustomerData(firstName, lastName, userName, password);
                    databaseHelper.close();
                    //testing value for success and launching login page for newly created account to be used
                    if (position != -1){
                        Toast.makeText(CreateAccount.this, "Successfully Created Account, Returning to login page", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(CreateAccount.this, LoginPage.class));
                    }
                    else{
                        //if position is error value then show toast stating account not created
                        Toast.makeText(CreateAccount.this, "Was not able to create account!", Toast.LENGTH_LONG).show();
                    }
                }
                //catching exception that is thrown if any of the editText are blank in UI
                catch (Exception e){
                    Toast.makeText(CreateAccount.this, "Unable to create account, all fields are required", Toast.LENGTH_LONG).show();
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
                Toast toast = Toast.makeText(CreateAccount.this, "Logged Out Successfully", Toast.LENGTH_LONG);
                toast.show();
                editor.commit();
                databaseHelper.resetCartTable();

                startActivity(new Intent(CreateAccount.this, LoginPage.class));
            }
            else{
                Toast toast = Toast.makeText(CreateAccount.this, "You aren't logged in currently", Toast.LENGTH_LONG);
                toast.show();
            }
            return true;
        }
        else if (id == R.id.item2) {
            Toast.makeText(this, "Thank you for a great semester Mr. Dorsett!", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.item3) {
            startActivity(new Intent(CreateAccount.this, CryptidStorePage.class));
            return true;
        } else if (id == R.id.item4) {
            startActivity(new Intent(CreateAccount.this, MainActivity.class));
            return true;
        } else if (id == R.id.item5) {
            startActivity(new Intent(CreateAccount.this, Cart.class));
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }
}
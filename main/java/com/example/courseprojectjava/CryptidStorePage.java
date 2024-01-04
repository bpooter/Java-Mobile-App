package com.example.courseprojectjava;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class CryptidStorePage extends AppCompatActivity /*implements AdapterView.OnItemSelectedListener*/{

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cryptid_store_page);

        getSupportActionBar().setTitle("Cryptid Store");

        databaseHelper = new DatabaseHelper(this);
        Button buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        Button buttonReturn = (Button) findViewById(R.id.buttonReturn);
        RadioGroup group = (RadioGroup) findViewById(R.id.buttonGroup);
        RadioButton buttonBigFoot = (RadioButton) findViewById(R.id.bigfootRad);
        RadioButton buttonMothMan = (RadioButton) findViewById(R.id.mothmanRad);
        RadioButton buttonChupacabra = (RadioButton) findViewById(R.id.chupacabraRad);
        RadioButton buttonLochNess = (RadioButton) findViewById(R.id.lochNessMonsterRad);

        //listener on radio group for checked button
        group.setOnCheckedChangeListener((group1, checkedId) -> {

            RadioButton radioButton = (RadioButton) group1.findViewById(checkedId);
        });

        //listener on submit button
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getting selected id value from radiogroup
                int selectedId = group.getCheckedRadioButtonId();

               //control structure to direct what activity to launch if selectedId matches
                if (selectedId == -1){
                    Toast.makeText(CryptidStorePage.this, "No answer selected", Toast.LENGTH_LONG).show();
                } else if (selectedId == R.id.bigfootRad) {
                    startActivity(new Intent(CryptidStorePage.this, BigFoot.class));
                } else if (selectedId == R.id.mothmanRad) {
                    startActivity(new Intent(CryptidStorePage.this, MothMan.class));
                } else if (selectedId == R.id.chupacabraRad) {
                    startActivity(new Intent(CryptidStorePage.this, Chupacabra.class));
                } else if (selectedId == R.id.lochNessMonsterRad) {
                    startActivity(new Intent(CryptidStorePage.this, LochNess.class));
                }
            }
        });

        //listener to return to main activity
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CryptidStorePage.this, MainActivity.class));
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
                Toast toast = Toast.makeText(CryptidStorePage.this, "Logged Out Successfully", Toast.LENGTH_LONG);
                toast.show();
                editor.commit();
                databaseHelper.resetCartTable();

                startActivity(new Intent(CryptidStorePage.this, LoginPage.class));
            }
            else{
                Toast toast = Toast.makeText(CryptidStorePage.this, "You aren't logged in currently", Toast.LENGTH_LONG);
                toast.show();
            }
            return true;
        }
        else if (id == R.id.item2) {
            Toast.makeText(this, "Thank you for a great semester Mr. Dorsett!", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.item3) {
            startActivity(new Intent(CryptidStorePage.this, CryptidStorePage.class));
            return true;
        } else if (id == R.id.item4) {
            startActivity(new Intent(CryptidStorePage.this, MainActivity.class));
            return true;
        } else if (id == R.id.item5) {
            startActivity(new Intent(CryptidStorePage.this, Cart.class));
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }
}
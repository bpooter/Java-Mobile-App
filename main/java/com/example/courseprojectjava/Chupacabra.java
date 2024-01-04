package com.example.courseprojectjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Chupacabra extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chupacabra);

        databaseHelper = new DatabaseHelper(this);

        //object references to UI and mediaplayer for sound of monster
        ImageView chupaImage = (ImageView) findViewById(R.id.imageViewChupacabra);
        Button chupaStore = (Button) findViewById(R.id.buttonChupaStore);
        Button buttonPrevious = (Button) findViewById(R.id.buttonPrevious);
        Button buttonNext = (Button) findViewById(R.id.buttonNext);
        Button buttonGrowl = (Button) findViewById(R.id.buttonChupaSound);
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.chupacabra);

        //setting title bar name
        getSupportActionBar().setTitle("Chupacabra Info");

        //lambda for listening for button click on monster sound
        buttonGrowl.setOnClickListener(v -> mediaPlayer.start());

        //lambda for listening for click on next image button
        buttonNext.setOnClickListener(v -> chupaImage.setImageResource(R.drawable.chupacabra2));

        //lambda for listening for click on previous image button
        buttonPrevious.setOnClickListener(v -> chupaImage.setImageResource(R.drawable.chupacabra1));

        //lambda for store button
        chupaStore.setOnClickListener(v -> {
            startActivity(new Intent(Chupacabra.this, ChupacabraStore.class));

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
                Toast toast = Toast.makeText(Chupacabra.this, "Logged Out Successfully", Toast.LENGTH_LONG);
                toast.show();
                editor.commit();
                databaseHelper.resetCartTable();

                startActivity(new Intent(Chupacabra.this, LoginPage.class));
            }
            else{
                Toast toast = Toast.makeText(Chupacabra.this, "You aren't logged in currently", Toast.LENGTH_LONG);
                toast.show();
            }
            return true;
        }
        else if (id == R.id.item2) {
            Toast.makeText(this, "Thank you for a great semester Mr. Dorsett!", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.item3) {
            startActivity(new Intent(Chupacabra.this, CryptidStorePage.class));
            return true;
        } else if (id == R.id.item4) {
            startActivity(new Intent(Chupacabra.this, MainActivity.class));
            return true;
        } else if (id == R.id.item5) {
            startActivity(new Intent(Chupacabra.this, Cart.class));
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }
}
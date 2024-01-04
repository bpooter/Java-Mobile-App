package com.example.courseprojectjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class LochNess extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loch_ness);

        databaseHelper = new DatabaseHelper(this);

        //object references to UI and mediaplayer for sound of monster
        ImageView lochNessImage = (ImageView) findViewById(R.id.imageViewLochNess);
        Button lochNessStore = (Button) findViewById(R.id.buttonNessieStore);
        Button buttonPrevious = (Button) findViewById(R.id.buttonPrevious);
        Button buttonNext = (Button) findViewById(R.id.buttonNext);
        Button buttonGrowl = (Button) findViewById(R.id.buttonNessieSound);
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.lochness);

        //setting title bar name
        getSupportActionBar().setTitle("Loch Ness Monster Info");

        //lambda for listening for button click on monster sound
        buttonGrowl.setOnClickListener(v -> mediaPlayer.start());

        //lambda for listening for click on next image button
        buttonNext.setOnClickListener(v -> lochNessImage.setImageResource(R.drawable.lochness2));

        //lambda for listening for click on previous image button
        buttonPrevious.setOnClickListener(v -> lochNessImage.setImageResource(R.drawable.lochness1));

        //lambda for store button
        lochNessStore.setOnClickListener(v -> {
            startActivity(new Intent(LochNess.this, LochNessStore.class));

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
                Toast toast = Toast.makeText(LochNess.this, "Logged Out Successfully", Toast.LENGTH_LONG);
                toast.show();
                editor.commit();
                databaseHelper.resetCartTable();

                startActivity(new Intent(LochNess.this, LoginPage.class));
            }
            else{
                Toast toast = Toast.makeText(LochNess.this, "You aren't logged in currently", Toast.LENGTH_LONG);
                toast.show();
            }
            return true;
        }
        else if (id == R.id.item2) {
            Toast.makeText(this, "Thank you for a great semester Mr. Dorsett!", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.item3) {
            startActivity(new Intent(LochNess.this, CryptidStorePage.class));
            return true;
        } else if (id == R.id.item4) {
            startActivity(new Intent(LochNess.this, MainActivity.class));
            return true;
        } else if (id == R.id.item5) {
            startActivity(new Intent(LochNess.this, Cart.class));
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }
}
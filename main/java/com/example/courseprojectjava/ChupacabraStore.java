package com.example.courseprojectjava;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChupacabraStore extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //reference to itemPic imageView and itemDesc textView to access in other methods to update in onSelectedItem listener
    ImageView itemPic = null;
    TextView itemDesc = null;
    TextView sizeTextView = null;
    Spinner sizeSpinner = null;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chupacabra_store);

        //setting title bar name
        getSupportActionBar().setTitle("Chupacabra Store");
        databaseHelper = new DatabaseHelper(this);


        //setting values in resources sizes array to sizeSpinner
        sizeSpinner = findViewById(R.id.sizeSpinner);
        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(this, R.array.sizes, android.R.layout.simple_spinner_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);


        //setting values in resources quantity array to quantitySpinner
        Spinner quantitySpinner = findViewById(R.id.quantitySpinner);
        ArrayAdapter<CharSequence> quantityAdapter = ArrayAdapter.createFromResource(this, R.array.quantities, android.R.layout.simple_spinner_item);
        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantitySpinner.setAdapter(quantityAdapter);


        //setting values in resource items array to itemSpinner
        Spinner itemSpinner = findViewById(R.id.itemSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemSpinner.setAdapter(adapter);

        //setting context of listener for spinner
        itemSpinner.setOnItemSelectedListener(this);


        //object reference to items on store page UI
        Button addToCart = (Button) findViewById(R.id.buttonAddToCart);
        Button goToCart = (Button) findViewById(R.id.buttonSeeCart);
        itemDesc = (TextView) findViewById(R.id.textViewItemDesc);
        itemPic = (ImageView) findViewById(R.id.imageViewItemHolder);
        sizeTextView = (TextView) findViewById(R.id.sizeTV);

        //setting sizeSpinner and sizeTextView to be invisible
        sizeSpinner.setVisibility(View.INVISIBLE);
        sizeTextView.setVisibility(View.INVISIBLE);


        //click listener for goToCart button
        goToCart.setOnClickListener(v -> {
            startActivity(new Intent(ChupacabraStore.this, Cart.class));
        });

        //click listener for place order button
        addToCart.setOnClickListener(v -> {

            //price variable used for inserting in database for cart
            double price;
            String desc;
            long addedToCart;

            //getting boolean value from sharedPreferences if logged in to not let guests place order
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            boolean loggedIn =  sharedPreferences.getBoolean("loggedIn", false);

            //show message blocking order and return user to login page
            if (!loggedIn){
                Toast.makeText(this, "You aren't allowed to place an order without logging in", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ChupacabraStore.this, LoginPage.class));
            }
            else {

                //logging what userName is stored in terminal for debugging
                System.out.println(sharedPreferences.contains("userName"));
                System.out.println(sharedPreferences.getString("userName", null));
                //getting value of userName from sharedPreferences for current user and querying database to get ID stored for them to insert in cart
                String userName = sharedPreferences.getString("userName", null);

                //getting customerID from database with value stored in userName sharedPreference
                Cursor table = databaseHelper.getCustomerID(userName);
                table.moveToFirst();
                int customerID = table.getInt(0);
                int cursorSize = table.getCount();

                //logging amount in cursor and value of userName and id
                System.out.println("Amount in cursor " + cursorSize);
                System.out.println(userName + " " + " customerID: " + customerID);

                //getting values from the spinners on ui to use in insert
                String itemChoice = itemSpinner.getSelectedItem().toString();
                String sizeChoice = sizeSpinner.getSelectedItem().toString();
                int quantity = Integer.parseInt(quantitySpinner.getSelectedItem().toString());

                //logic for updating the image view and textView will go here
                switch (itemChoice) {
                    case "T-shirt":

                        price = 15.00;
                        desc = "Chupacabra Shirt";

                        addedToCart = databaseHelper.insertCartData(111, price, sizeChoice, customerID, quantity, desc);

                        if (addedToCart != -1){
                            Toast.makeText(this, "Added " + quantity + " " + desc + " size " + sizeChoice + " to Cart", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(this, "Error adding to cart", Toast.LENGTH_LONG).show();
                        }

                        break;

                    case "Coffee Mug":

                        price = 11.00;
                        desc = "Chupacabra Mug";

                        addedToCart = databaseHelper.insertCartData(117, price, null, customerID, quantity, desc);

                        if (addedToCart != -1){
                            Toast.makeText(this, "Added " + quantity + " " + desc + " to Cart", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(this, "Error adding to cart", Toast.LENGTH_LONG).show();
                        }
                        break;

                    case "Baseball Cap":

                        price = 18.00;
                        desc = "Chupacabra Cap";
                        addedToCart = databaseHelper.insertCartData(113, price, null, customerID, quantity, desc);

                        if (addedToCart != -1){
                            Toast.makeText(this, "Added " + quantity + " " + desc + " to Cart", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(this, "Error adding to cart", Toast.LENGTH_LONG).show();
                        }
                        break;

                }
            }
        });
        databaseHelper.close();
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
                Toast toast = Toast.makeText(ChupacabraStore.this, "Logged Out Successfully", Toast.LENGTH_LONG);
                toast.show();
                editor.commit();
                databaseHelper.resetCartTable();

                startActivity(new Intent(ChupacabraStore.this, LoginPage.class));
            }
            else{
                Toast toast = Toast.makeText(ChupacabraStore.this, "You aren't logged in currently", Toast.LENGTH_LONG);
                toast.show();
            }
            return true;
        }
        else if (id == R.id.item2) {
            Toast.makeText(this, "Thank you for a great semester Mr. Dorsett!", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.item3) {
            startActivity(new Intent(ChupacabraStore.this, CryptidStorePage.class));
            return true;
        } else if (id == R.id.item4) {
            startActivity(new Intent(ChupacabraStore.this, MainActivity.class));
            return true;
        } else if (id == R.id.item5) {
            startActivity(new Intent(ChupacabraStore.this, Cart.class));
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        String choice = parent.getItemAtPosition(position).toString();


            //logic for updating the image view and textView will go here
            switch (choice){
                case "T-shirt":
                    itemDesc.setText("A wonderful shirt that shows the chupacabra and his friends");
                    itemPic.setImageResource(R.drawable.chupacabrashirt);
                    sizeSpinner.setVisibility(View.VISIBLE);
                    sizeTextView.setVisibility(View.VISIBLE);
                    break;

                case "Coffee Mug":
                    itemDesc.setText("A 64 oz. Coffee mug of the Chupacabra before his morning coffee");
                    itemPic.setImageResource(R.drawable.chupacabramug);
                    sizeSpinner.setVisibility(View.INVISIBLE);
                    sizeTextView.setVisibility(View.INVISIBLE);
                    break;

                case "Baseball Cap":
                    itemDesc.setText("Friendly looking chupacabra hat");
                    itemPic.setImageResource(R.drawable.chupacabrahat);
                    sizeSpinner.setVisibility(View.INVISIBLE);
                    sizeTextView.setVisibility(View.INVISIBLE);
                    break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
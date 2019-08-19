package com.ashita.ecommerce;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ashita.ecommerce.adapter.CheckoutAdapter;
import com.ashita.ecommerce.database.DatabaseHelper;
import com.ashita.ecommerce.model.Product;
import com.ashita.ecommerce.model.Request;
import com.ashita.ecommerce.model.Users;
import com.ashita.ecommerce.utilities.Common;
import com.ashita.ecommerce.utilities.CustomDialog;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity implements CustomDialog.OnInputListener {

    private static final String TAG = "checkoutActivity";
    ActionBar actionBar;
    private RecyclerView checkoutRecyclerView;
    CheckoutAdapter checkoutAdapter;
    TextView totalPrice, totalShipping, totalSum, firstName, lastName, address, city, state, country, zipcode, phoneNumber;
    String currentTime;
    List<Product> itemSelectedList = new ArrayList<>();
    DatabaseReference databaseReference;
    MaterialRippleLayout place_order_button;
    GoogleSignInAccount acct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        Date date = new Date();
        Locale locale = new Locale("en","US");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm",locale);
        currentTime = simpleDateFormat.format(date);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Checkout");

        totalPrice = findViewById(R.id.item_totalPrice_OH);
        totalShipping = findViewById(R.id.price_shipping_OH);
        totalSum = findViewById(R.id.totalFinal_OH);
        place_order_button = findViewById(R.id.place_order_button);

        checkoutRecyclerView = findViewById(R.id.recyclerView_checkout);
        checkoutRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        checkoutRecyclerView.setLayoutManager(layoutManager);

        checkAddress();

        loadRecylerView(acct);

        place_order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("Request");
                Request request = new Request(
                        firstName.getText().toString(),
                        lastName.getText().toString(),
                        address.getText().toString(),
                        city.getText().toString(),
                        state.getText().toString(),
                        country.getText().toString(),
                        zipcode.getText().toString(),
                        phoneNumber.getText().toString(),
                        totalSum.getText().toString(),
                        currentTime,
                        acct.getEmail(),
                        totalPrice.getText().toString(),
                        totalShipping.getText().toString(),
                        itemSelectedList
                );
                //Sending request to firebase database
                String order_number = String.valueOf(System.currentTimeMillis());
                databaseReference.child(order_number).setValue(request);

                //Showing snackbar, after order is placed
                Common common;
                ConstraintLayout constraintLayout = findViewById(R.id.checkout_activity_main);
                common = new Common(CheckoutActivity.this);
                common.placeOrderSnackBack(constraintLayout);

                //Cleaning the cart
                new DatabaseHelper(getBaseContext()).deleteFromCart(acct.getEmail());

                //Delay for Snack bar, as we click on order placed, snack bar will appear for sometime, therefore we are making
                //use of Handler and under its run
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(CheckoutActivity.this,MainActivity.class));
                    }
                },2000L);

            }
        });
    }

    private void checkAddress() {
        final String userID = acct.getId();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(userID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null)
                {
                    CustomDialog customDialog = new CustomDialog();
                    customDialog.show(getSupportFragmentManager(),"Enter Deliver Address");
                }
                else
                {
                    //Fetching data from Firebase and display here
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                    databaseReference.child(userID);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Users users = new Users();
                            for(DataSnapshot snapshot : dataSnapshot.getChildren())
                            {
                                users = snapshot.getValue(Users.class);
                            }
                            populateAddress(users);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e(TAG,"Unable to load data from database: "+databaseError.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG,"Unable to load data from database: "+databaseError.getMessage());
            }
        });
    }

    public void loadRecylerView(GoogleSignInAccount account)
    {
        itemSelectedList = new DatabaseHelper(this).getCartDetailsWOEmail(account.getEmail());
        checkoutAdapter = new CheckoutAdapter(itemSelectedList, this,getApplicationContext());
        checkoutAdapter.notifyDataSetChanged();

        checkoutRecyclerView.setAdapter(checkoutAdapter);
        double subTotal=0, totalShippingPrice =0;
        for(Product item : itemSelectedList)
        {
            subTotal += (Double.parseDouble(item.getPrice()))*(Double.parseDouble(item.getQuantity()));
            totalShippingPrice += (Double.parseDouble(item.getShippingPrice()));
        }

        totalPrice.setText(Double.toString(subTotal));
        totalShipping.setText(Double.toString(totalShippingPrice));
        double totalSumPrice = subTotal + totalShippingPrice;
        Locale locale = new Locale("en","us");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        totalSum.setText(numberFormat.format(totalSumPrice));
    }


    @Override
    public void sendInput(Users users) {
        populateAddress(users);
    }

    private void populateAddress(Users users) {
        //firstName, lastName, address, city, state, country, zipcode, phoneNumber;
        firstName = findViewById(R.id.Fname_OH);
        lastName  = findViewById(R.id.LName_OH);
        address  = findViewById(R.id.address_OH);
        city  = findViewById(R.id.City_OH);
        state  = findViewById(R.id.state_OH);
        country  = findViewById(R.id.country_OH);
        zipcode  = findViewById(R.id.zipcode_OH);
        phoneNumber  = findViewById(R.id.phoneNum_OH);

        firstName.setText(users.getFirstName());
        lastName.setText(users.getLastName());
        address.setText(users.getAddress());
        city.setText(users.getCity());
        state.setText(users.getState());
        country.setText(users.getCountry());
        zipcode.setText(users.getZipCode());
        phoneNumber.setText(users.getPhoneNum());
    }

    //the below both codes are for above back pressed
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,ShoppingCartActivity.class));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}

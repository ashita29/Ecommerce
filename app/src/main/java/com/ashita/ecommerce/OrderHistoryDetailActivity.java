package com.ashita.ecommerce;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.ashita.ecommerce.adapter.CheckoutAdapter;
import com.ashita.ecommerce.adapter.OrderHistoryDetailAdapter;
import com.ashita.ecommerce.model.Product;
import com.ashita.ecommerce.model.Request;
import com.ashita.ecommerce.utilities.Common;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryDetailActivity extends AppCompatActivity {

    private static final String TAG = "orderHistoryDetailActivity";
    private RecyclerView orderHistoryDetailRecyclerView;
    OrderHistoryDetailAdapter orderHistoryDetailAdapter;
    TextView totalPrice, totalShipping, totalSum, firstName, lastName, address, city, state, country, zipcode, phoneNumber;
    List<Product> orderHistoryList = new ArrayList<>();
    DatabaseReference databaseReference;
    GoogleSignInAccount acct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_detail);
        acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        totalPrice = findViewById(R.id.item_totalPrice_OH);
        totalShipping = findViewById(R.id.price_shipping_OH);
        totalSum = findViewById(R.id.totalFinal_OH);
        firstName = findViewById(R.id.Fname_OH);
        lastName = findViewById(R.id.LName_OH);
        address = findViewById(R.id.address_OH);
        city = findViewById(R.id.City_OH);
        state = findViewById(R.id.state_OH);
        country = findViewById(R.id.country_OH);
        zipcode = findViewById(R.id.zipcode_OH);
        phoneNumber = findViewById(R.id.phoneNum_OH);

        orderHistoryDetailRecyclerView = findViewById(R.id.recyclerView_OH);
        orderHistoryDetailRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        orderHistoryDetailRecyclerView.setLayoutManager(layoutManager);

        loadOrderHistoryDetailRecyclerView(acct);
    }

    public void loadOrderHistoryDetailRecyclerView(GoogleSignInAccount account)
    {
        totalPrice.setText(Common.orderRequest.getSubTotal());
        totalShipping.setText(Common.orderRequest.getShippingTotal());
        totalSum.setText(Common.orderRequest.getTotalSum());

        firstName.setText(Common.orderRequest.getFirstName());
        lastName.setText(Common.orderRequest.getLastName());
        address.setText(Common.orderRequest.getAddress());
        city.setText(Common.orderRequest.getCity());
        state.setText(Common.orderRequest.getState());
        country.setText(Common.orderRequest.getCountry());
        zipcode.setText(Common.orderRequest.getZipCode());
        phoneNumber.setText(Common.orderRequest.getPhoneNum());

        orderHistoryDetailAdapter = new OrderHistoryDetailAdapter(Common.orderRequest.getOrderList(),this);
        orderHistoryDetailAdapter.notifyDataSetChanged();
        orderHistoryDetailRecyclerView.setAdapter(orderHistoryDetailAdapter);
    }

    public void onBackPressed() {
        startActivity(new Intent(this,OrderHistoryActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}

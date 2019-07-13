package com.ashita.ecommerce;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity {

    ActionBar actionBar;
    String id;
    TextView productName,productDesc, productPrice, productShipPrice;
    ImageView productUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Product Detail");

        Bundle bundle = getIntent().getExtras();
        id= bundle.getString("id");
        String image = bundle.getString("productImage");
        String name = bundle.getString("productName");
        String des = bundle.getString("productDescription");
        String price = bundle.getString("productPrice");
        String shipPrice = bundle.getString("productShipPrice");

        productName = findViewById(R.id.product_name);
        productDesc= findViewById(R.id.product_description);
        productPrice = findViewById(R.id.product_price);
        productShipPrice = findViewById(R.id.ship_price);
        productUrl = findViewById(R.id.product_image);

        //Size spinner button
        Spinner spinner = (Spinner)findViewById(R.id.size_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.size_spinner,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        productName.setText(name);
        productDesc.setText(des);
        productPrice.setText(price);
        productShipPrice.setText(shipPrice);
        Picasso.get().load(image).into(productUrl);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra("prevId",id);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}

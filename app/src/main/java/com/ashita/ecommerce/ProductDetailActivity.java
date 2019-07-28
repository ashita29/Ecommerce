package com.ashita.ecommerce;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.andremion.counterfab.CounterFab;
import com.ashita.ecommerce.database.DatabaseHelper;
import com.ashita.ecommerce.model.Product;
import com.ashita.ecommerce.utilities.Common;
import com.balysv.materialripple.MaterialRippleLayout;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity {

    ActionBar actionBar;
    String id;
    String image,name,des,price,shipPrice;
    TextView productName,productDesc, productPrice, productShipPrice;
    ImageView productUrl;
    MaterialRippleLayout addtocart_button;
    ElegantNumberButton elegantNumberButton;
    CounterFab counterFab_cartButton;
    String productID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Product Detail");

        Bundle bundle = getIntent().getExtras();
        id= bundle.getString("id");
        image = bundle.getString("productImage");
        name = bundle.getString("productName");
        des = bundle.getString("productDescription");
        price = bundle.getString("productPrice");
        shipPrice = bundle.getString("productShipPrice");
        productID = bundle.getString("productId");

        productName = findViewById(R.id.product_name);
        productDesc= findViewById(R.id.product_description);
        productPrice = findViewById(R.id.product_price);
        productShipPrice = findViewById(R.id.ship_price);
        productUrl = findViewById(R.id.product_image);
        addtocart_button = findViewById(R.id.pd_rippleLo_addtocart);
        elegantNumberButton = findViewById(R.id.count_button);
        counterFab_cartButton = findViewById(R.id.floating_cartButton);

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

        addtocart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Common.checkInternetConnection(ProductDetailActivity.this))
                {
                    new DatabaseHelper(getBaseContext()).
                            addToCart(new Product
                                    (       acct.getEmail(),
                                            productID,
                                            name,
                                            price,
                                            shipPrice,
                                            image,
                                            elegantNumberButton.getNumber()));
                    counterFab_cartButton.setCount(new DatabaseHelper(ProductDetailActivity.this).getCountofItems(acct.getEmail()));
                    Common common;
                    ConstraintLayout constraintLayout = findViewById(R.id.pd_constraintLO);
                    common= new Common(ProductDetailActivity.this);
                    common.addToCartSnackBack(constraintLayout);
                }
                else
                {
                    Common.showInternetAlertMsg(ProductDetailActivity.this);
                }
            }
        });

        counterFab_cartButton.setCount(new DatabaseHelper(ProductDetailActivity.this).getCountofItems(acct.getEmail()));

        counterFab_cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductDetailActivity.this, ShoppingCartActivity.class));
            }
        });
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

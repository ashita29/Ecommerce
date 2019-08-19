package com.ashita.ecommerce;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ashita.ecommerce.adapter.ShoppingCartAdapter;
import com.ashita.ecommerce.database.DatabaseHelper;
import com.ashita.ecommerce.model.Product;
import com.ashita.ecommerce.utilities.Common;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity {

    ActionBar actionBar;
    RecyclerView shoppingcartRecyclerView;
    ShoppingCartAdapter shoppingCartAdapter;
    TextView cartItemCount, priceTotal, itemText;
    List<Product> cartList = new ArrayList<>();
    MaterialRippleLayout proceed_to_checkout_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Shopping Cart");

        cartItemCount = findViewById(R.id.itemNum);
        priceTotal = findViewById(R.id.price_sc);
        itemText = findViewById(R.id.items_sc);
        proceed_to_checkout_button = findViewById(R.id.proceed_ripple_button);

        shoppingcartRecyclerView = findViewById(R.id.cart_recycler_view_sc);
        shoppingcartRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        shoppingcartRecyclerView.setLayoutManager(layoutManager);

        if(Common.checkInternetConnection(ShoppingCartActivity.this))
        {
            loadProductList(acct);
        }
        else
        {
            Common.showInternetAlertMsg(ShoppingCartActivity.this);
        }

        proceed_to_checkout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShoppingCartActivity.this, CheckoutActivity.class));
            }
        });
    }

    public void loadProductList(GoogleSignInAccount account)
    {
        cartList = new DatabaseHelper(this).getCartDetails(account.getEmail());
        if(cartList.size() == 0)
        {
                Common.checkForEmptyCart(this);
        }
        else
        {
            shoppingCartAdapter = new ShoppingCartAdapter(cartList,this,getApplicationContext());
            shoppingCartAdapter.notifyDataSetChanged();

            shoppingcartRecyclerView.setAdapter(shoppingCartAdapter);

            double totalPrice =0;
            for(Product item:cartList)
            {
                totalPrice += (Double.parseDouble(item.getPrice()))*(Double.parseDouble(item.getQuantity()));
            }
            priceTotal.setText(Double.toString(totalPrice));
            cartItemCount.setText(Integer.toString(cartList.size()));
            if(cartList.size() > 1)
            {
                itemText.setText(getString(R.string.items));
            }
            else
                itemText.setText(getString(R.string.item));
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}

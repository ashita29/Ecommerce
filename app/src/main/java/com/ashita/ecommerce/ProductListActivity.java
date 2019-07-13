package com.ashita.ecommerce;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ashita.ecommerce.adapter.CategoryViewHolder;
import com.ashita.ecommerce.adapter.ProductViewHolder;
import com.ashita.ecommerce.model.Category;
import com.ashita.ecommerce.model.Product;
import com.ashita.ecommerce.utilities.Common;
import com.ashita.ecommerce.utilities.ItemClickListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductListActivity extends AppCompatActivity {

    ActionBar actionBar;
    DatabaseReference cRef;
    String productID;
    FirebaseRecyclerAdapter<Product, ProductViewHolder> firebaseRecyclerAdapter;
    private RecyclerView productRecyclerView;
    private static final String TAG = "MainActivity";
    private static final int NUM_COLUMNS =2;
    FirebaseDatabase cFirebaseDatabase;

    @Override
    protected void onStart() {
        super.onStart();
        if(Common.checkInternetConnection(ProductListActivity.this))
        {
            productRecyclerView();

        }
        else
        {
            Common.showInternetAlertMsg(ProductListActivity.this);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Product List");

        productID = getIntent().getStringExtra("id");

        if(Common.checkInternetConnection(ProductListActivity.this))
        {

            initProductRecyclerView();
        }
        else
        {
            Common.showInternetAlertMsg(ProductListActivity.this);
        }

    }

    public void productRecyclerView()
    {
        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(cRef.orderByChild("id").equalTo(productID),Product.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Product model) {

                holder.setProductView(getApplicationContext(),model.getImageUrl(),model.getName(),model.getDescription(),model.getPrice());
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemClick(View v, int pos) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id",productID);
                        bundle.putString("productImage",model.getImageUrl());
                        bundle.putString("productName",model.getName());
                        bundle.putString("productDescription",model.getDescription());
                        bundle.putString("productPrice",model.getPrice());
                        bundle.putString("productShipPrice",model.getShippingPrice());
                        Intent productDetail = new Intent(ProductListActivity.this,ProductDetailActivity.class);
                        productDetail.putExtras(bundle);
                        startActivityForResult(productDetail,1);
                    }
                });

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.card_view_product,viewGroup,false);
                return new ProductViewHolder(view);
            }
        };
        firebaseRecyclerAdapter.startListening();
        productRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void initProductRecyclerView() {

        Log.d(TAG, "initCategoryRecyclerView: called");
        productRecyclerView = findViewById(R.id.product_view_category);
        productRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        productRecyclerView.setLayoutManager(layoutManager);

        cFirebaseDatabase = FirebaseDatabase.getInstance();
        cRef = cFirebaseDatabase.getReference("ProductList");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            if(requestCode==(RESULT_OK))
            {
                productID= data.getStringExtra("prevId");
            }
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

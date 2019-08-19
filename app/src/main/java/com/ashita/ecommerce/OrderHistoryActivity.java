package com.ashita.ecommerce;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ashita.ecommerce.adapter.OrderHistoryViewHolder;
import com.ashita.ecommerce.adapter.ProductViewHolder;
import com.ashita.ecommerce.model.Product;
import com.ashita.ecommerce.model.Request;
import com.ashita.ecommerce.utilities.Common;
import com.ashita.ecommerce.utilities.ItemClickListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class OrderHistoryActivity extends AppCompatActivity {

    ActionBar actionBar;
    DatabaseReference cRef;
    FirebaseRecyclerAdapter<Request, OrderHistoryViewHolder> firebaseRecyclerAdapter;
    private RecyclerView orderHistoryRecyclerView;
    FirebaseDatabase cFirebaseDatabase;
    private static final String TAG = "OrderHistoryActivity";
    GoogleSignInAccount account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Order History");
        account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(Common.checkInternetConnection(OrderHistoryActivity.this))
        {
            initOrderHistoryRecyclerView();
            checkOrders(this);
        }
        else
        {
            Common.showInternetAlertMsg(OrderHistoryActivity.this);
        }
    }

    private void initOrderHistoryRecyclerView() {
        Log.d(TAG, "initOrderHistoryRecyclerView: called");
        orderHistoryRecyclerView = findViewById(R.id.order_history_recyclerView);
        orderHistoryRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        orderHistoryRecyclerView.setLayoutManager(layoutManager);

        cFirebaseDatabase = FirebaseDatabase.getInstance();
        cRef = cFirebaseDatabase.getReference("Request");
    }

    public void checkOrders(final Context context)
    {
        Query query = cRef.orderByChild("userEmail").equalTo(account.getEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    loadOrderHistory();
                }
                else
                {
                    Common.checkForOrderHistory(context);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void loadOrderHistory()
    {
        FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(cRef.orderByChild("userEmail").equalTo(account.getEmail()),Request.class)
                .build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Request, OrderHistoryViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position, @NonNull final Request model) {
                    holder.setOrderView(model.getOrderDate(),firebaseRecyclerAdapter.getRef(position).getKey(),model.getTotalSum());
                    holder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onItemClick(View v, int pos) {
                            Intent intent = new Intent(OrderHistoryActivity.this,OrderHistoryDetailActivity.class);
                            Common.orderRequest = model;
                            startActivity(intent);
                        }
                    });
            }

            @NonNull
            @Override
            public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.card_view_orderhistory,viewGroup,false);
                return new OrderHistoryViewHolder(view);
            }
        };

        firebaseRecyclerAdapter.startListening();
        orderHistoryRecyclerView.setAdapter(firebaseRecyclerAdapter);
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

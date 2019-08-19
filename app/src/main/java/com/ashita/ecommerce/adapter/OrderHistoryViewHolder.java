package com.ashita.ecommerce.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashita.ecommerce.OrderHistoryActivity;
import com.ashita.ecommerce.R;
import com.ashita.ecommerce.model.Product;
import com.ashita.ecommerce.model.Request;
import com.ashita.ecommerce.utilities.ItemClickListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView orderDate, orderID, orderTotal;
    View mView;
    ItemClickListener itemClickListener;

    public OrderHistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        itemView.setOnClickListener(this);
    }

    public void setOrderView(String date, String id, String total)
    {
        orderDate = itemView.findViewById(R.id.date_tag);
        orderID = itemView.findViewById(R.id.id_tag);
        orderTotal = itemView.findViewById(R.id.price_tag);

        orderDate.setText(date);
        orderID.setText(id);
        orderTotal.setText(total);
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(v,getLayoutPosition());

    }
    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener=itemClickListener;
    }
}

//    public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryViewHolder> {
//        private List<Request> orderList = new ArrayList<>();
//        private OrderHistoryActivity orderHistoryActivity;
//        private Context applicationContext;
//
//        public OrderHistoryAdapter(List<Request> orderList, OrderHistoryActivity orderHistoryActivity, Context applicationContext) {
//            this.orderList = orderList;
//            this.orderHistoryActivity = orderHistoryActivity;
//            this.applicationContext = applicationContext;
//        }
//
//        @NonNull
//        @Override
//        public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//            LayoutInflater layoutInflater = LayoutInflater.from(orderHistoryActivity);
//            View itemView = layoutInflater.inflate(R.layout.card_view_orderhistory,viewGroup,false);
//            return new OrderHistoryViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull OrderHistoryViewHolder orderHistoryViewHolder, int position) {
//            final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(applicationContext);
//
//            //orderHistoryViewHolder.orderID.setText(orderList.get(position).);
//            orderHistoryViewHolder.orderDate.setText(orderList.get(position).getOrderDate());
//            orderHistoryViewHolder.orderTotal.setText(orderList.get(position).getTotalSum());
//        }
//
//        @Override
//        public int getItemCount() {
//            return 0;
//        }
//    }

package com.ashita.ecommerce.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashita.ecommerce.OrderHistoryDetailActivity;
import com.ashita.ecommerce.R;
import com.ashita.ecommerce.model.Product;
import com.ashita.ecommerce.model.Request;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class OrderHistoryDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public ImageView orderImage;
    public TextView orderName, orderPrice,orderShip,orderQty;

    public OrderHistoryDetailViewHolder(@NonNull View itemView) {
        super(itemView);
        orderImage = itemView.findViewById(R.id.orderImage);
        orderName = itemView.findViewById(R.id.product_name);
        orderPrice = itemView.findViewById(R.id.price);
        orderShip = itemView.findViewById(R.id.shiping_price);
        orderQty = itemView.findViewById(R.id.qty);
    }

    @Override
    public void onClick(View v) {

    }
}

public class OrderHistoryDetailAdapter extends RecyclerView.Adapter<OrderHistoryDetailViewHolder> {
    List<Product> orderHistoryList = new ArrayList<>();
    private OrderHistoryDetailActivity orderHistoryDetailActivity;

    public OrderHistoryDetailAdapter(List<Product> orderHistoryList, OrderHistoryDetailActivity orderHistoryDetailActivity) {
        this.orderHistoryList = orderHistoryList;
        this.orderHistoryDetailActivity = orderHistoryDetailActivity;
    }

    @NonNull
    @Override
    public OrderHistoryDetailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(orderHistoryDetailActivity);
        View itemView = layoutInflater.inflate(R.layout.card_view_orderhistorydetail,viewGroup,false);
        return new OrderHistoryDetailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryDetailViewHolder orderHistoryDetailViewHolder, int position) {

        //fetched the complete list of orders selected
        Product request = orderHistoryList.get(position);
        orderHistoryDetailViewHolder.orderName.setText(request.getName());
        orderHistoryDetailViewHolder.orderQty.setText(request.getQuantity());
        orderHistoryDetailViewHolder.orderPrice.setText(request.getPrice());
        orderHistoryDetailViewHolder.orderShip.setText(request.getShippingPrice());
        Picasso.get()
                .load(request.getImageUrl())
                .into(orderHistoryDetailViewHolder.orderImage);}

    @Override
    public int getItemCount() {
        return orderHistoryList.size();
    }
}

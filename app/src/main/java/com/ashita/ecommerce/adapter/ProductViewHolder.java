package com.ashita.ecommerce.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ashita.ecommerce.R;
import com.ashita.ecommerce.utilities.ItemClickListener;
import com.squareup.picasso.Picasso;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;
    ItemClickListener itemClickListener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        mView.setOnClickListener(this);
    }

    public void setProductView(Context context, String imageUrl, String name, String Description, String Price)
    {
        ImageView imageView = mView.findViewById(R.id.product_image);
        TextView productName = mView.findViewById(R.id.product_name);
        TextView productDes = mView.findViewById(R.id.product_des);
        TextView productPrice = mView.findViewById(R.id.price_number);

        productName.setText(name);
        productDes.setText(Description);
        productPrice.setText(Price);
        Picasso.get()
                .load(imageUrl)
                .into(imageView);
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

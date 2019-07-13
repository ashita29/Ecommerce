package com.ashita.ecommerce.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ashita.ecommerce.R;
import com.ashita.ecommerce.utilities.ItemClickListener;
import com.squareup.picasso.Picasso;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    View mView;
    ItemClickListener itemClickListener;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        itemView.setOnClickListener(this);
    }

    public void setCategory(Context context, String title, String imageUrl, ProgressBar progressBar, RelativeLayout sliderLayout, RelativeLayout recyclerLayout)
    {
        TextView categoryName = mView.findViewById(R.id.category_name);
        ImageView categoryImage = mView.findViewById(R.id.category_image);

        categoryName.setText(title);
        Picasso.get()
                .load(imageUrl).
                into(categoryImage);

        progressBar.setVisibility(View.INVISIBLE);
        sliderLayout.setVisibility(View.VISIBLE);
        recyclerLayout.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(v,getLayoutPosition());
    }

    public void  setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }
}

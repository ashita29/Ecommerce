package com.ashita.ecommerce.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageSliderAdapter extends PagerAdapter {
    private Context context;
    private String[] imageUrl;
    private int current_position =0;

    public ImageSliderAdapter(Context context, String[] imageUrl)
    {
        this.context = context;
        this.imageUrl = imageUrl;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE; //we removed 'imageUrl.length' so as to make the count infinite and smooth flow to first image;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object; //The object passed here the one which is returned from 'instantiateItem' method as well as view is my imageView.
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        if(current_position == imageUrl.length)
        {
            current_position =0;
        }
        ImageView imageView = new ImageView(context);
        Picasso.get()
                .load(imageUrl[current_position])
                .fit()
                .centerCrop()
                .into(imageView);
        container.addView(imageView);
        current_position++;
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       container.removeView((View) object);
    }
}

package com.ashita.ecommerce.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashita.ecommerce.CheckoutActivity;
import com.ashita.ecommerce.R;
import com.ashita.ecommerce.model.Product;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class CheckViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public ImageView productImage;
    public TextView prodName, prodQty, prodPrice, prodShipPrice, prodTotal;
    public CheckViewHolder(@NonNull View itemView) {
        super(itemView);
        productImage= itemView.findViewById(R.id.productImage);
        prodName = itemView.findViewById(R.id.productName);
        prodQty = itemView.findViewById(R.id.qty);
        prodPrice = itemView.findViewById(R.id.price);
        prodShipPrice = itemView.findViewById(R.id.shipPrice);;
        prodTotal = itemView.findViewById(R.id.total);
    }

    @Override
    public void onClick(View v) {
    }
}

public class CheckoutAdapter extends RecyclerView.Adapter<CheckViewHolder> {
    private List<Product> productList = new ArrayList<>();
    private CheckoutActivity checkoutActivity;
    private Context applicationContext;

    public CheckoutAdapter(List<Product> productList, CheckoutActivity checkoutActivity, Context applicationContext) {
        this.productList = productList;
        this.checkoutActivity = checkoutActivity;
        this.applicationContext = applicationContext;
    }

    @NonNull
    @Override
    public CheckViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(checkoutActivity);
        View itemView = layoutInflater.inflate(R.layout.card_view_checkoutproduct,viewGroup,false);
        return new CheckViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckViewHolder checkViewHolder, int position) {
        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(applicationContext);
        Picasso.get()
                .load(productList.get(position).getImageUrl())
                .into(checkViewHolder.productImage);

        checkViewHolder.prodName.setText(productList.get(position).getName());
        checkViewHolder.prodQty.setText(productList.get(position).getQuantity());
        checkViewHolder.prodPrice.setText(productList.get(position).getPrice());
        checkViewHolder.prodShipPrice.setText(productList.get(position).getShippingPrice());
        int totalPrice = (Integer.parseInt(productList.get(position).getQuantity())) * (Integer.parseInt(productList.get(position).getPrice()));
        checkViewHolder.prodTotal.setText(Integer.toString(totalPrice));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}

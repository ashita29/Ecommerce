package com.ashita.ecommerce.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashita.ecommerce.R;
import com.ashita.ecommerce.ShoppingCartActivity;
import com.ashita.ecommerce.database.DatabaseHelper;
import com.ashita.ecommerce.model.Product;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public ImageView deleteButton, productImage;
    public TextView prodName, prodPrice;
    public ElegantNumberButton quantityButton;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        deleteButton = itemView.findViewById(R.id.delete_button_sc);
        productImage = itemView.findViewById(R.id.product_image_sc);
        prodName = itemView.findViewById(R.id.product_name_sc);
        prodPrice = itemView.findViewById(R.id.product_price_sc);
        quantityButton = itemView.findViewById(R.id.elegantNumberButton);
    }

    @Override
    public void onClick(View v) {

    }
}
public class ShoppingCartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Product> productList = new ArrayList<>();
    private ShoppingCartActivity shoppingCartActivity;
    private Context applicationContext;

    public ShoppingCartAdapter(List<Product> productList, ShoppingCartActivity shoppingCartActivity, Context applicationContext) {
        this.productList = productList;
        this.shoppingCartActivity = shoppingCartActivity;
        this.applicationContext = applicationContext;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(shoppingCartActivity);
        View itemView = layoutInflater.inflate(R.layout.card_view_shopping_cart,viewGroup,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder cartViewHolder, final int position) {
        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(applicationContext);
        Picasso.get()
                .load(productList.get(position).getImageUrl())
                .into(cartViewHolder.productImage);

        cartViewHolder.quantityButton.setNumber(productList.get(position).getQuantity());
        cartViewHolder.prodName.setText(productList.get(position).getName());
        cartViewHolder.quantityButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Product product = productList.get(position);
                product.setQuantity(String.valueOf(newValue));
                new DatabaseHelper(shoppingCartActivity).upgradeCart(product);

                //After updating the quantity, we will update the price of the product.
                double totalSum =0;
                List<Product> products = new DatabaseHelper(shoppingCartActivity).getCartDetails(acct.getEmail());
                for(Product item:products)
                {
                    totalSum += (Double.parseDouble(item.getPrice()))*(Double.parseDouble(item.getQuantity()));
                }

                TextView price = shoppingCartActivity.findViewById(R.id.price_sc);
                price.setText(Double.toString(totalSum));

                //The below code will increse the count of product at the individual card view.
                double pricePerItem = (Double.parseDouble(productList.get(position).getPrice()))*(Double.parseDouble(productList.get(position).getQuantity()));
                cartViewHolder.prodPrice.setText(Double.toString(pricePerItem));
            }
        });

        double pricePerItem = (Double.parseDouble(productList.get(position).getPrice()))*(Double.parseDouble(productList.get(position).getQuantity()));
        cartViewHolder.prodPrice.setText(Double.toString(pricePerItem));

        cartViewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Product> products = new DatabaseHelper(shoppingCartActivity).getCartDetails(acct.getEmail());

                //Delete item from list -> clear all items from database-> replace new list in database
                //removing the item from the selected product list
                products.remove(position);

                //deleting tall item from the database
                new DatabaseHelper(shoppingCartActivity).deleteFromCart(acct.getEmail());

                //replacing the new items in database
                for(Product item:products)
                {
                    new DatabaseHelper(shoppingCartActivity).addToCart(item);
                }

                //refreshing the content on shopping cart page
                shoppingCartActivity.loadProductList(acct);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}

package com.ashita.ecommerce.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ashita.ecommerce.MainActivity;
import com.ashita.ecommerce.model.Request;

public class Common extends AppCompatActivity {

    Context context;
    public static Request orderRequest;

    public Common(Context context) {
        this.context = context;
    }

    public static boolean checkInternetConnection(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager != null)
        {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if(networkInfo != null)
                {
                    if(networkInfo.getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
        }
        return false;
    }

    public static void showInternetAlertMsg(final Context context)
    {
        new AlertDialog.Builder(context)
                .setTitle("No Internet Connectection")
                .setMessage("Please Check the Internet Connection and Try Again!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity)context).finish();
                    }
                }).setNegativeButton("Cancel",null).show();
    }

    public static void checkForEmptyCart(final Context context)
    {
        new AlertDialog.Builder(context)
                .setTitle("Empty Cart")
                .setMessage("Please add products to cart.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity)context).finish();
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                    }
                }).setCancelable(false).show();
    }

    public static void checkForOrderHistory(final Context context)
    {
        new AlertDialog.Builder(context)
                .setTitle("Empty Order History")
                .setMessage("No orders in history")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity)context).finish();
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                    }
                }).setCancelable(false).show();
    }



    public void addToCartSnackBack(View view)
    {
        Snackbar snackbar = Snackbar.make(view,"Product is added to the cart",Snackbar.LENGTH_LONG)
                .setAction("",null);
        snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.parseColor("#E91E63"));
        TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public void placeOrderSnackBack(View view)
    {
        Snackbar snackbar = Snackbar.make(view,"Order is placed",Snackbar.LENGTH_LONG)
                .setAction("",null);
        snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.parseColor("#E91E63"));
        TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

}

package com.ashita.ecommerce.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class Common extends AppCompatActivity {

    Context context;

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
}

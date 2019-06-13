package com.ashita.ecommerce.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Common {

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
}

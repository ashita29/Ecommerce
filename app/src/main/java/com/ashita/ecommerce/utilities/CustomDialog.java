package com.ashita.ecommerce.utilities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ashita.ecommerce.R;
import com.ashita.ecommerce.ShoppingCartActivity;
import com.ashita.ecommerce.model.Users;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomDialog extends DialogFragment {

    public interface OnInputListener
    {
        void sendInput(Users users);
    }

    public OnInputListener onInputListener;

    TextView dialogHeader;
    EditText firstName, lastName, phoneNumber, addressInfo, city,state,postalCode, country;
    Button cancelButton, saveButton;
    DatabaseReference databaseReference;
    Users users;
    ShoppingCartActivity shoppingCartActivity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.customer_info_dialog,container,false);
        dialogHeader = view.findViewById(R.id.dialog_heading);
        firstName = view.findViewById(R.id.first_name);
        lastName = view.findViewById(R.id.last_name);
        phoneNumber = view.findViewById(R.id.phone_number);
        addressInfo = view.findViewById(R.id.address_OH);
        city = view.findViewById(R.id.city);
        state = view.findViewById(R.id.state_OH);
        postalCode = view.findViewById(R.id.postal_code);
        country =  view.findViewById(R.id.country_OH);
        cancelButton= view.findViewById(R.id.Cancel_button);
        saveButton = view.findViewById(R.id.save_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        users = new Users();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String first_name,last_name,address_info, phone_info,state_info,city_info,postal_info,country_info;
                first_name = firstName.getText().toString();
                last_name=lastName.getText().toString();
                phone_info = phoneNumber.getText().toString();
                address_info = addressInfo.getText().toString();
                state_info = state.getText().toString();
                city_info=city.getText().toString();
                postal_info=postalCode.getText().toString();
                country_info=country.getText().toString();

                users.setFirstName(first_name);
                users.setLastName(last_name);
                users.setPhoneNum(phone_info);
                users.setAddress(address_info);
                users.setCity(city_info);
                users.setState(state_info);
                users.setZipCode(postal_info);
                users.setCountry(country_info);


                //setting value on current activity
                onInputListener.sendInput(users);

                //sending data to firbase table 'Users'
                databaseReference.child(account.getId()).setValue(users);

                getDialog().dismiss();
            }
        });
        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            onInputListener = (OnInputListener) getActivity();
        }
        catch (ClassCastException e){
            e.getMessage();
        }
    }
}

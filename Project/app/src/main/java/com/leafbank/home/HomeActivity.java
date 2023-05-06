package com.leafbank.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.leafbank.R;
import com.leafbank.bankaccount.BankaccountActivity;
import com.leafbank.profile.ProfileActivity;

public class HomeActivity extends AppCompatActivity implements HomeButtons {
    public static FirebaseUser user;
    public static DocumentReference usersRef;
    public static CollectionReference bankaccountsRef;
    TextView titleTextView;
    private FirebaseAuth firebase;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        titleTextView = findViewById(R.id.home_title);
        firebase = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user = firebase.getCurrentUser();
        usersRef = firestore.collection("Users").document(user.getUid());
        bankaccountsRef = firestore.collection("Bankaccounts");

        if(user == null) finish();

        Log.d(this.getClass().getName(), "onCreate: "+firebase.getCurrentUser().getEmail());
        setUserNameTextView(this);


    }

    public static void pageController(Context context, Class<?> dest){
        if (context.getClass() == dest) return;
        Intent intent = new Intent(context, dest);
        context.startActivity(intent);
    }

    public static void setUserNameTextView(AppCompatActivity view){
        TextView titleTextView = view.findViewById(R.id.home_title);
        usersRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()){
                        String username = documentSnapshot.getString("USERNAME");
                        titleTextView.setText("Szia "+username);
                    }
                });
    }

    @Override
    public void hombebtn_click(View view) {
        pageController(this, HomeActivity.class);
    }

    @Override
    public void bankaccountbtn_click(View view) {
        pageController(this, BankaccountActivity.class);
    }

    @Override
    public void profilebtn_click(View view) {
        pageController(this, ProfileActivity.class);
    }
}
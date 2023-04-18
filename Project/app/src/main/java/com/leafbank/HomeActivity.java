package com.leafbank;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class HomeActivity extends AppCompatActivity {
    TextView titleTextView;
    private FirebaseAuth firebase;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        titleTextView = findViewById(R.id.home_title);
        firebase = FirebaseAuth.getInstance();
        user = firebase.getCurrentUser();

        if(user == null) finish();

        titleTextView.setText("Szia "+user.getEmail());
        Log.d(this.getClass().getName(), "onCreate: "+firebase.getCurrentUser().getEmail());
    }
}
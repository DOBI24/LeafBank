package com.leafbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {
    TextView titleTextView;
    private FirebaseAuth firebase;
    private FirebaseUser user;
    private FirebaseFirestore firestore;
    private DocumentReference usersRef;

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

        usersRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()){
                        String username = documentSnapshot.getString("USERNAME");
                        titleTextView.setText("Szia "+username);
                    }
                    else{
                        Toast.makeText(this, "Network ERROR", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Network ERROR", Toast.LENGTH_SHORT).show();
                    finish();
                });

        if(user == null) finish();

        Log.d(this.getClass().getName(), "onCreate: "+firebase.getCurrentUser().getEmail());
    }
}
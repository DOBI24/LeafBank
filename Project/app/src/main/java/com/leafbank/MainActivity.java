package com.leafbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

//    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


//        firestore = FirebaseFirestore.getInstance();
//
//        Map<String, Object> user = new HashMap<>();
//        user.put("firstname", "asd");
//        user.put("Name", "cica");
//
//        firestore.collection("users").add(user)
//                .addOnSuccessListener(documentReference -> Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show())
//                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show());

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
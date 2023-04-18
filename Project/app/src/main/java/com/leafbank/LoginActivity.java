package com.leafbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText emailEditText;
    EditText passwordEditText;

    private FirebaseAuth firebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        firebase = FirebaseAuth.getInstance();
    }

    public void registerClick(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void loginClick(View view) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

//        Log.i(this.getClass().getName(), "loginClick: "+username+" "+password);
        firebase.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(this, authResult -> {startHome();})
                .addOnFailureListener(this, authResult -> {
                    Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
                });
    }

    public void startHome(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
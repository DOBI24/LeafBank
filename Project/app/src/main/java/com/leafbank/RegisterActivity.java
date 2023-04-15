package com.leafbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;
    EditText passwordRepEditText;
    EditText emailEditText;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordRepEditText = findViewById(R.id.passwordRepEditText);
        emailEditText = findViewById(R.id.emailEditText);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void registerClick(View view) {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordRep = passwordRepEditText.getText().toString();
        String email = emailEditText.getText().toString();

        //TODO: Ellenőrzés

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(this, authResult -> {Log.d(this.getClass().getName(), "Sikeres létrehozás");})
                .addOnFailureListener(this, authResult -> {Log.d(this.getClass().getName(), "Sikertelen");});
    }
}
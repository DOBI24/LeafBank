package com.leafbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import org.w3c.dom.Text;

import java.util.concurrent.BlockingDeque;

public class LoginActivity extends AppCompatActivity {
    EditText emailEditText;
    EditText passwordEditText;
    TextView emailErrorText;
    TextView passwordErrorText;
    private FirebaseAuth firebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        emailEditText = findViewById(R.id.login_emailEditText);
        passwordEditText = findViewById(R.id.login_passwordEditText);
        emailErrorText = findViewById(R.id.login_emailErrorTextView);
        passwordErrorText = findViewById(R.id.login_passwordErrorTextView);
        firebase = FirebaseAuth.getInstance();

    }

    public void registerClick(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void loginClick(View view) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // isEmpty CHECK
        if(!authDatas(new String[]{email, password}, new TextView[]{emailErrorText, passwordErrorText})) return;

//        Log.i(this.getClass().getName(), "loginClick: "+username+" "+password);
        firebase.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                startHome();
                return;
            }
            try {
                throw task.getException();
            } catch (FirebaseAuthInvalidCredentialsException e) {
                emailErrorText.setText("Nem létezik ilyen felhasználó!");
            } catch (FirebaseAuthInvalidUserException e){
                emailErrorText.setText("Nem létezik ilyen felhasználó!");
            } catch (Exception e) {
                Log.e(this.getClass().getName(), e.getClass() + e.getMessage());
            }
        });
    }

    public static boolean authDatas(String[] fields, TextView[] textViews) {
        boolean accept = true;

        // isEmpty
        for (int i = 0; i < fields.length; i++) {
            textViews[i].setText("");
            if (fields[i].isEmpty()){
                textViews[i].setText("Minden mezőt kötelező kitölteni!");
                accept = false;
            }
        }
        return accept;
    }

    public void startHome(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // USER Auth
        firebase = FirebaseAuth.getInstance();
        if (firebase.getCurrentUser() != null){
            startHome();
        }
    }
}
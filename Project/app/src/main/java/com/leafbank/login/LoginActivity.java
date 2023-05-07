package com.leafbank.login;

import static com.leafbank.home.HomeActivity.pageController;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.leafbank.R;
import com.leafbank.home.HomeActivity;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView emailErrorText;
    private TextView passwordErrorText;
    private FirebaseAuth firebase;

    @SuppressLint("SetTextI18n")
    public static boolean authDatas(String[] fields, TextView[] textViews) {
        boolean accept = true;

        // isEmpty
        for (int i = 0; i < fields.length; i++) {
            textViews[i].setText("");
            if (fields[i].isEmpty()) {
                textViews[i].setText("Minden mezőt kötelező kitölteni!");
                accept = false;
            }
        }
        return !accept;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();

        firebase = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.login_emailEditText);
        passwordEditText = findViewById(R.id.login_passwordEditText);
        emailErrorText = findViewById(R.id.login_emailErrorTextView);
        passwordErrorText = findViewById(R.id.login_passwordErrorTextView);
        ConstraintLayout login_container = findViewById(R.id.login_container);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.login_anim);
        login_container.startAnimation(animation);
    }

    public void registerClick(View view) {
        pageController(this, RegisterActivity.class);
    }

    @SuppressLint("SetTextI18n")
    public void loginClick(View view) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // isEmpty CHECK
        if (authDatas(new String[]{email, password}, new TextView[]{emailErrorText, passwordErrorText}))
            return;

        firebase.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                pageController(this, HomeActivity.class);
                return;
            }
            try {
                throw Objects.requireNonNull(task.getException());
            } catch (FirebaseAuthInvalidCredentialsException | FirebaseAuthInvalidUserException e) {
                emailErrorText.setText("Nem létezik ilyen felhasználó!");
            } catch (Exception e) {
                Toast.makeText(this, "Ismeretlen hiba!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // USER Auth
        firebase = FirebaseAuth.getInstance();
        if (firebase.getCurrentUser() == null) return;

        pageController(this, HomeActivity.class);
    }
}
package com.leafbank.login;

import static com.leafbank.home.HomeActivity.pageController;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.leafbank.R;
import com.leafbank.home.HomeActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText passwordRepEditText;
    private EditText fullnameEditText;
    private TextView emailErrorText;
    private TextView usernameErrorText;
    private TextView passwordErrorText;
    private TextView passwordRepErrorText;
    private TextView fullnameErrorText;
    private FirebaseAuth firebase;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Objects.requireNonNull(getSupportActionBar()).hide();

        firebase = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        emailEditText = findViewById(R.id.register_emailEditText);
        usernameEditText = findViewById(R.id.register_usernameEditText);
        passwordEditText = findViewById(R.id.register_passwordEditText);
        passwordRepEditText = findViewById(R.id.register_passwordRepEditText);
        fullnameEditText = findViewById(R.id.register_fullnameEditText);

        emailErrorText = findViewById(R.id.register_emailErrorTextView);
        usernameErrorText = findViewById(R.id.register_usernameErrorTextView);
        passwordErrorText = findViewById(R.id.register_passwordErrorTextView);
        passwordRepErrorText = findViewById(R.id.register_passwordRepErrorTextView);
        fullnameErrorText = findViewById(R.id.register_fullnameErrorTextView);
        ConstraintLayout register_container = findViewById(R.id.register_container);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.login_anim);
        register_container.startAnimation(animation);
    }

    @SuppressLint("SetTextI18n")
    public void registerClick(View view) {
        String email = emailEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordRep = passwordRepEditText.getText().toString();
        String fullname = fullnameEditText.getText().toString();

        // isEmpty CHECK
        if (LoginActivity.authDatas(new String[]{email, username, password, passwordRep, fullname}, new TextView[]{emailErrorText, usernameErrorText, passwordErrorText, passwordRepErrorText, fullnameErrorText}))
            return;

        // PASSWORD EQUAl CHECK
        if (!password.equals(passwordRep)) {
            passwordRepErrorText.setText("Nem egyezik a két jelszó!");
            return;
        }

        firebase.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = task.getResult().getUser();
                Map<String, String> userDatas = new HashMap<>();
                userDatas.put("EMAIL", email);
                userDatas.put("USERNAME", username);
                userDatas.put("NAME", fullname);
                assert user != null;
                firestore.collection("Users").document(user.getUid()).set(userDatas)
                        .addOnSuccessListener(unused -> pageController(this, HomeActivity.class))
                        .addOnFailureListener(e -> Toast.makeText(this, "Network ERROR", Toast.LENGTH_SHORT).show());
                return;
            }

            try {
                throw Objects.requireNonNull(task.getException());
            } catch (FirebaseAuthWeakPasswordException e) {
                passwordErrorText.setText("Jelszónak minimum 6 karakternek kell lennie!");
            } catch (FirebaseAuthUserCollisionException e) {
                emailErrorText.setText("Az email cím már foglalt!");
            } catch (Exception e) {
                Log.e(this.getClass().getName(), e.getClass() + " " + e.getMessage());
            }
        });
    }

    public void loginClick(View view) {
        pageController(this, LoginActivity.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // USER Auth
        if (firebase.getCurrentUser() == null) return;

        pageController(this, HomeActivity.class);
    }

}
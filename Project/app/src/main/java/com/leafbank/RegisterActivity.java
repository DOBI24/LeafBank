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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

    EditText emailEditText;
    EditText usernameEditText;
    EditText passwordEditText;
    EditText passwordRepEditText;
    TextView emailErrorText;
    TextView usernameErrorText;
    TextView passwordErrorText;
    TextView passwordRepErrorText;

    private FirebaseAuth firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        emailEditText = findViewById(R.id.register_emailEditText);
        usernameEditText = findViewById(R.id.register_usernameEditText);
        passwordEditText = findViewById(R.id.register_passwordEditText);
        passwordRepEditText = findViewById(R.id.register_passwordRepEditText);

        emailErrorText = findViewById(R.id.register_emailErrorTextView);
        usernameErrorText = findViewById(R.id.register_usernameErrorTextView);
        passwordErrorText = findViewById(R.id.register_passwordErrorTextView);
        passwordRepErrorText = findViewById(R.id.register_passwordRepErrorTextView);

        firebase = FirebaseAuth.getInstance();
    }

    public void registerClick(View view) {
        String email = emailEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordRep = passwordRepEditText.getText().toString();

        // isEmpty CHECK
        if(!LoginActivity.authDatas(new String[]{email, username, password, passwordRep}, new TextView[]{emailErrorText, usernameErrorText, passwordErrorText, passwordRepErrorText})) return;

        // PASSWORD EQUAl CHECK
        if (!password.equals(passwordRep)){
            passwordRepErrorText.setText("Nem egyezik a két jelszó!");
            return;
        }

        firebase.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()){
                startHome();
                return;
            }

            try {
                throw task.getException();
            } catch (FirebaseAuthWeakPasswordException e){
                passwordErrorText.setText("Jelszónak minimum 6 karakternek kell lennie!");
            } catch (FirebaseAuthUserCollisionException e){
                emailErrorText.setText("Az email cím már foglalt!");
            } catch (Exception e) {
                Log.e(this.getClass().getName(), e.getClass() + " " + e.getMessage());
            }
        });
    }

    public void loginClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();
        // USER Auth
        if (firebase.getCurrentUser() != null){
            startHome();
        }
    }

    public void startHome(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }


}
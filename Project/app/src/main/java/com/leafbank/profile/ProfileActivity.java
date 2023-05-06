package com.leafbank.profile;

import static com.leafbank.home.HomeActivity.pageController;
import static com.leafbank.home.HomeActivity.user;
import static com.leafbank.home.HomeActivity.usersRef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.leafbank.LoginActivity;
import com.leafbank.R;
import com.leafbank.bankaccount.BankaccountActivity;
import com.leafbank.home.HomeActivity;
import com.leafbank.home.HomeButtons;

public class ProfileActivity extends AppCompatActivity implements HomeButtons {
    private EditText emailEditText;
    private EditText usernameEditText;
    private EditText fullnameEditText;
    private FirebaseAuth firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        firebase = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.profile_emailEditText);
        usernameEditText = findViewById(R.id.profile_usernameEditText);
        fullnameEditText = findViewById(R.id.profile_fullnameEditText);

        emailEditText.setText(user.getEmail());
        usersRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()){
                usernameEditText.setText(documentSnapshot.getString("USERNAME"));
                fullnameEditText.setText(documentSnapshot.getString("NAME"));
            }
        });

        HomeActivity.setUserNameTextView(this);
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

    public void resetpasswordbtn_click(View view) {
        firebase.sendPasswordResetEmail(emailEditText.getText().toString()).addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               Toast.makeText(this, "Elküldve", Toast.LENGTH_SHORT).show();
           }
           else{
               Toast.makeText(this, "Nem küldve", Toast.LENGTH_SHORT).show();
           }
        });
    }

    public void logoutbtn_click(View view) {
        firebase.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void deleteuserbtn_click(View view) {
        user.delete();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
package com.leafbank.profile;

import static com.leafbank.home.HomeActivity.pageController;
import static com.leafbank.home.HomeActivity.user;
import static com.leafbank.home.HomeActivity.usersRef;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.leafbank.NotificationHandler;
import com.leafbank.R;
import com.leafbank.bankaccount.BankaccountActivity;
import com.leafbank.home.HomeActivity;
import com.leafbank.home.HomeButtons;
import com.leafbank.login.LoginActivity;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity implements HomeButtons {
    private EditText emailEditText;
    private EditText usernameEditText;
    private EditText fullnameEditText;
    private FirebaseAuth firebase;
    private FirebaseFirestore firestore;
    private NotificationHandler notificationHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Objects.requireNonNull(getSupportActionBar()).hide();
        HomeActivity.setUserNameTextView(this);

        firebase = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        notificationHandler = new NotificationHandler(this);

        emailEditText = findViewById(R.id.profile_emailEditText);
        usernameEditText = findViewById(R.id.profile_usernameEditText);
        fullnameEditText = findViewById(R.id.profile_fullnameEditText);

        emailEditText.setText(user.getEmail());
        usersRef.document(user.getUid()).get().addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists()) return;

            usernameEditText.setText(documentSnapshot.getString("USERNAME"));
            fullnameEditText.setText(documentSnapshot.getString("NAME"));
        });

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
            if (task.isSuccessful()) {
                notificationHandler.SendEmailNotification("Jelszó-visszaállító email elküldve");
            } else {
                Toast.makeText(this, "Email ERROR", Toast.LENGTH_SHORT).show();
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
        firebase.signOut();
        firestore.collection("Bankaccounts").whereEqualTo("ownerID", user.getUid()).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot x : queryDocumentSnapshots) {
                        x.getReference().delete();
                    }
                });
        user.delete();
        usersRef.document(user.getUid()).delete();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
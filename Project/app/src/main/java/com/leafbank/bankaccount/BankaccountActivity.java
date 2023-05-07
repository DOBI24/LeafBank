package com.leafbank.bankaccount;

import static com.leafbank.home.HomeActivity.bankaccountsRef;
import static com.leafbank.home.HomeActivity.pageController;
import static com.leafbank.home.HomeActivity.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.leafbank.R;
import com.leafbank.home.HomeActivity;
import com.leafbank.home.HomeButtons;
import com.leafbank.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class BankaccountActivity extends AppCompatActivity implements HomeButtons {
    private ArrayList<BankaccountItem> items;
    private BankaccountItemAdapter adapter;
    private AppCompatButton bankaccount_newbtn;
    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankaccount);
        Objects.requireNonNull(getSupportActionBar()).hide();
        HomeActivity.setUserNameTextView(this);

        firestore = FirebaseFirestore.getInstance();

        bankaccount_newbtn = findViewById(R.id.bankaccount_newbtn);
        bankaccount_newbtn.setVisibility(View.VISIBLE);

        RecyclerView recyclerView = findViewById(R.id.bankaccount_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));

        items = new ArrayList<>();
        adapter = new BankaccountItemAdapter(this, items);
        recyclerView.setAdapter(adapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        items.clear();
        firestore.collection("Bankaccounts")
                .whereEqualTo("ownerID", user.getUid())
                .orderBy("balance").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) return;

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        BankaccountItem item = document.toObject(BankaccountItem.class);
                        items.add(item);
                        if (items.size() == 2) bankaccount_newbtn.setVisibility(View.INVISIBLE);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void createNewBankAccount_click(View view) {
        BankaccountItem newBankAccount = createBankaccount();

        items.add(newBankAccount);
        if (items.size() == 2) bankaccount_newbtn.setVisibility(View.INVISIBLE);

        adapter.notifyDataSetChanged();
    }

    public BankaccountItem createBankaccount() {
        List<String> numbers = new ArrayList<>();

        bankaccountsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot document : queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) return;

                numbers.add(Objects.requireNonNull(document.get("number")).toString());
            }
        });

        String number = generateRandomNumber(numbers);

        BankaccountItem item = new BankaccountItem(number, 0, user.getUid());
        firestore.collection("Bankaccounts").add(item)
                .addOnFailureListener(e -> Toast.makeText(this, "Network ERROR", Toast.LENGTH_SHORT).show());

        return item;
    }

    private String generateRandomNumber(List<String> numbers) {
        Random rnd = new Random();
        StringBuilder newNumber = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            newNumber.append(rnd.nextInt(10));
        }

        if (numbers.contains(newNumber.toString())) return generateRandomNumber(numbers);
        return newNumber.toString();
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
}
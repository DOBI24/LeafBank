package com.leafbank.bankaccount;

import static com.leafbank.home.HomeActivity.bankaccountsRef;
import static com.leafbank.home.HomeActivity.pageController;
import static com.leafbank.home.HomeActivity.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.leafbank.home.HomeActivity;
import com.leafbank.home.HomeButtons;
import com.leafbank.R;
import com.leafbank.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BankaccountActivity extends AppCompatActivity implements HomeButtons {
    private RecyclerView recyclerView;
    private ArrayList<BankaccountItem> items;
    private BankaccountItemAdapter adapter;
    private AppCompatButton bankaccount_newbtn;
    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankaccount);
        getSupportActionBar().hide();

        HomeActivity.setUserNameTextView(this);

        firestore = FirebaseFirestore.getInstance();


        bankaccount_newbtn = findViewById(R.id.bankaccount_newbtn);
        bankaccount_newbtn.setVisibility(View.VISIBLE);
        recyclerView = findViewById(R.id.bankaccount_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));
        items = new ArrayList<>();
        adapter = new BankaccountItemAdapter(this, items);
        recyclerView.setAdapter(adapter);


        firestore.collection("Bankaccounts").whereEqualTo("ownerID", user.getUid()).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null){
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                            BankaccountItem item = document.toObject(BankaccountItem.class);
                            items.add(item);
                            if (items.size() == 2) bankaccount_newbtn.setVisibility(View.INVISIBLE);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

        adapter.notifyDataSetChanged();
    }

    public void createNewInvocie_Click(View view) {
        BankaccountItem newBankAccount = createBankaccount();

        items.add(newBankAccount);
        if (items.size() == 2) bankaccount_newbtn.setVisibility(View.INVISIBLE);

        adapter.notifyDataSetChanged();
    }

    public BankaccountItem createBankaccount(){
        List<String> numbers = new ArrayList<>();

        bankaccountsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot document : queryDocumentSnapshots){
                numbers.add(document.get("number").toString());
            }
        });

        String number = generateRandomNumber(numbers);
        int balance = 0;

        BankaccountItem item = new BankaccountItem(number, balance, user.getUid());
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

        if (numbers.contains(newNumber)) return generateRandomNumber(numbers);
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

    public void tobankbtn_click(View view) {
        pageController(this, BankaccountActivity.class);
    }

    public void tocardbtn_click(View view) {
        //TODO: Card view berakása
//        pageController(this, );
    }
}
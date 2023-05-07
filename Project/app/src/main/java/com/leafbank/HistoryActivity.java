package com.leafbank;

import static com.leafbank.home.HomeActivity.pageController;
import static com.leafbank.home.HomeActivity.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.leafbank.bankaccount.BankaccountActivity;
import com.leafbank.bankaccount.BankaccountItem;
import com.leafbank.bankaccount.BankaccountItemAdapter;
import com.leafbank.home.HomeActivity;
import com.leafbank.home.HomeButtons;
import com.leafbank.profile.ProfileActivity;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity implements HomeButtons {

    private RecyclerView recyclerView;
    private ArrayList<HistoryItem> items;
    private HistoryItemAdapter adapter;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().hide();
        HomeActivity.setUserNameTextView(this);

        firestore = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.history_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));

        items = new ArrayList<>();
        adapter = new HistoryItemAdapter(this, items);
        recyclerView.setAdapter(adapter);

        firestore.collection("Transfers")
                .whereEqualTo("fromUser", user.getUid())
                .whereEqualTo("toUser", user.getUid())
                .orderBy("date")
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) return;

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                        HistoryItem item = document.toObject(HistoryItem.class);

                        if (document.get("fromUser").toString().equals(user.getUid())) item.setDirection("out");
                        else item.setDirection("in");

                        items.add(item);
                        adapter.notifyDataSetChanged();

                        if (document.get("fromUser").toString().equals(user.getUid()) && document.get("toUser").toString().equals(user.getUid())){
                            item = document.toObject(HistoryItem.class);
                            item.setDirection("in");
                            items.add(item);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    recyclerView.scrollToPosition(items.size()-1);
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
}
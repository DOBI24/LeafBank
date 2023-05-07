package com.leafbank.bankaccount;

import static com.leafbank.home.HomeActivity.user;
import static com.leafbank.home.HomeActivity.usersRef;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leafbank.R;

import java.util.ArrayList;

public class BankaccountItemAdapter extends RecyclerView.Adapter<BankaccountItemAdapter.ViewHolder> {

    private ArrayList<BankaccountItem> items;
    private Context context;
    private int lastPosition = -1;

    public BankaccountItemAdapter(Context context, ArrayList<BankaccountItem> items){
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.bankaccount_cards, parent, false));
    }

    @Override
    public void onBindViewHolder(BankaccountItemAdapter.ViewHolder holder, int position) {
        BankaccountItem currentItem = items.get(position);

        holder.bindTo(currentItem);
    }
    @Override
    public int getItemCount() {
        return items.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView number;
        private TextView balance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.bankaccount_name);
            number = itemView.findViewById(R.id.bankaccount_number);
            balance = itemView.findViewById(R.id.bankaccount_balance);
        }

        public void bindTo(BankaccountItem currentItem) {
            usersRef.document(user.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()){
                            String fullname = documentSnapshot.getString("NAME");
                            name.setText(fullname);
                        }
                    });
            number.setText(BankaccountItem.numberFormat(currentItem.getNumber()));
            balance.setText("$ "+currentItem.getBalance());
        }
    }
}

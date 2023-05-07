package com.leafbank.bankaccount;

import static com.leafbank.home.HomeActivity.user;
import static com.leafbank.home.HomeActivity.usersRef;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leafbank.R;

import java.util.ArrayList;

public class BankaccountItemAdapter extends RecyclerView.Adapter<BankaccountItemAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<BankaccountItem> items;
    private int lastPosition = -1;

    public BankaccountItemAdapter(Context context, ArrayList<BankaccountItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.bankaccount_cards, parent, false));
    }

    @Override
    public void onBindViewHolder(BankaccountItemAdapter.ViewHolder holder, int position) {
        BankaccountItem currentItem = items.get(position);

        holder.bindTo(currentItem);

        if (holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.bankaccount_anim);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView number;
        private final TextView balance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.bankaccount_name);
            number = itemView.findViewById(R.id.bankaccount_number);
            balance = itemView.findViewById(R.id.bankaccount_balance);
        }

        @SuppressLint("SetTextI18n")
        public void bindTo(BankaccountItem currentItem) {
            usersRef.document(user.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String fullname = documentSnapshot.getString("NAME");
                            name.setText(fullname);
                        }
                    });
            number.setText(BankaccountItem.numberFormat(currentItem.getNumber()));
            balance.setText("$ " + currentItem.getBalance());
        }
    }
}

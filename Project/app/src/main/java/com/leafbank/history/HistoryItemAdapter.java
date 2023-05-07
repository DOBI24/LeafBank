package com.leafbank.history;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leafbank.R;
import com.leafbank.bankaccount.BankaccountItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class HistoryItemAdapter extends RecyclerView.Adapter<HistoryItemAdapter.ViewHolder> {
    private final ArrayList<HistoryItem> items;
    private final Context context;

    public HistoryItemAdapter(Context context, ArrayList<HistoryItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.history_card_in, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryItemAdapter.ViewHolder holder, int position) {
        HistoryItem currentItem = items.get(position);

        holder.bindTo(currentItem);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView fromaccountTextView;
        private final TextView detailsTextView;
        private final TextView toaccountTextView;
        private final ImageView history_arrow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fromaccountTextView = itemView.findViewById(R.id.history_fromaccountTextView);
            detailsTextView = itemView.findViewById(R.id.history_detailsTextView);
            toaccountTextView = itemView.findViewById(R.id.history_toaccountTextView);
            history_arrow = itemView.findViewById(R.id.history_arrow);
        }

        @SuppressLint("SetTextI18n")
        public void bindTo(HistoryItem currentItem) {
            if (Objects.equals(currentItem.getDirection(), "out"))
                history_arrow.setImageResource(R.drawable.outmoney2);
            else history_arrow.setImageResource(R.drawable.inmoney);
            fromaccountTextView.setText(BankaccountItem.numberFormat(currentItem.getFromNumber()));

            //TODO: Az idő direkt helyi időzóna
            detailsTextView.setText(
                    new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault()).format(currentItem.getDate().toDate()) + "\n"
                            + "$" + currentItem.getAmount()
            );

            toaccountTextView.setText(BankaccountItem.numberFormat(currentItem.getToNumber()));
        }
    }
}

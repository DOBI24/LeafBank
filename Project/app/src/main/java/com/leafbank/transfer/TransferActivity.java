package com.leafbank.transfer;

import static com.leafbank.home.HomeActivity.pageController;
import static com.leafbank.home.HomeActivity.user;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.leafbank.NotificationHandler;
import com.leafbank.R;
import com.leafbank.bankaccount.BankaccountActivity;
import com.leafbank.bankaccount.BankaccountItem;
import com.leafbank.history.HistoryItem;
import com.leafbank.home.HomeActivity;
import com.leafbank.home.HomeButtons;
import com.leafbank.login.LoginActivity;
import com.leafbank.profile.ProfileActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TransferActivity extends AppCompatActivity implements HomeButtons {
    private RadioGroup radioGroup;
    private EditText amountEditText;
    private EditText toaccountnumberEditText;
    private TextView toaccountnumberErrorTextView;
    private TextView amountErrorTextView;
    private FirebaseFirestore firestore;
    private ArrayList<BankaccountItem> items;
    private NotificationHandler notificationHandler;


    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        Objects.requireNonNull(getSupportActionBar()).hide();
        HomeActivity.setUserNameTextView(this);

        firestore = FirebaseFirestore.getInstance();

        notificationHandler = new NotificationHandler(this);

        items = new ArrayList<>();

        radioGroup = findViewById(R.id.transfer_radioGroup);
        amountEditText = findViewById(R.id.transfer_amountEditText);
        toaccountnumberEditText = findViewById(R.id.transfer_toaccountnumberEditText);
        toaccountnumberErrorTextView = findViewById(R.id.transfer_toaccountnumberErrorTextView);
        amountErrorTextView = findViewById(R.id.transfer_amountErrorTextView);

        int dpi = getResources().getDisplayMetrics().densityDpi;

        firestore.collection("Bankaccounts").whereEqualTo("ownerID", user.getUid()).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots == null) return;

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        BankaccountItem item = document.toObject(BankaccountItem.class);
                        items.add(item);
                    }

                    if (items.size() == 0) {
                        TextView errorText = new TextView(this);
                        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                                RadioGroup.LayoutParams.MATCH_PARENT,
                                RadioGroup.LayoutParams.WRAP_CONTENT
                        );
                        errorText.setText("Nincs számlád!");
                        errorText.setTextSize(25);
                        errorText.setTextColor(Color.argb(255, 219, 166, 149));
                        errorText.setGravity(Gravity.CENTER);
                        errorText.setHeight(100 * (dpi / 160));

                        radioGroup.addView(errorText, layoutParams);
                        return;
                    }

                    List<RadioButton> radioButtons = new ArrayList<>();
                    for (BankaccountItem x : items) {
                        RadioButton newRadiobtn = new RadioButton(this);

                        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                                RadioGroup.LayoutParams.WRAP_CONTENT,
                                RadioGroup.LayoutParams.WRAP_CONTENT
                        );
                        layoutParams.setMargins(5 * (dpi / 160), 5 * (dpi / 160), 5 * (dpi / 160), 10 * (dpi / 160));
                        layoutParams.gravity = Gravity.CENTER;

                        newRadiobtn.setText(BankaccountItem.numberFormat(x.getNumber()));
                        newRadiobtn.setTextColor(getColor(R.color.white1));
                        newRadiobtn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        newRadiobtn.setTextSize(16);
                        newRadiobtn.setBackgroundResource(R.drawable.radiobtn_selector);
                        newRadiobtn.setButtonDrawable(android.R.color.transparent);
                        newRadiobtn.setHeight(40 * (dpi / 160));

                        radioGroup.addView(newRadiobtn, layoutParams);
                        radioButtons.add(newRadiobtn);
                    }

                    radioButtons.get(0).setChecked(true);
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

    @SuppressLint("SetTextI18n")
    public void sendBtn_click(View view) {
        String fromAccountnumber = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString().replace("-", "");
        String toAccountnumber = toaccountnumberEditText.getText().toString();
        String amount = amountEditText.getText().toString();

        if (LoginActivity.authDatas(new String[]{toAccountnumber, amount}, new TextView[]{toaccountnumberErrorTextView, amountErrorTextView}))
            return;

        if (fromAccountnumber.equals(toAccountnumber)) {
            toaccountnumberErrorTextView.setText("Válassz másik számlát!");
            return;
        }

        firestore.collection("Bankaccounts").whereEqualTo("number", toAccountnumber).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.isEmpty()) {
                toaccountnumberErrorTextView.setText("Nincs ilyen célszámla!");
                return;
            }
            firestore.collection("Bankaccounts").whereEqualTo("number", fromAccountnumber).get().addOnSuccessListener(queryDocumentSnapshots2 -> {
                if (queryDocumentSnapshots2.isEmpty()) {
                    return;
                }
                String balance = Objects.requireNonNull(queryDocumentSnapshots2.getDocuments().get(0).get("balance")).toString();
                if (Double.parseDouble(balance) < Double.parseDouble(amount)) {
                    amountErrorTextView.setText("Nincs elég pénz a számládon!");
                    return;
                }

                sendMoney(fromAccountnumber, toAccountnumber, Double.parseDouble(amount));
            });
        });
    }

    public void sendMoney(String fromNumber, String toNumber, double a) {
        double amount = Double.parseDouble(new DecimalFormat("#.##").format(a));

        firestore.collection("Bankaccounts").whereEqualTo("number", fromNumber).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.isEmpty()) {
                return;
            }

            BankaccountItem bankaccountItem = queryDocumentSnapshots.getDocuments().get(0).toObject(BankaccountItem.class);
            Objects.requireNonNull(bankaccountItem).setBalance(bankaccountItem.getBalance() - amount);
            DocumentReference documentReference = queryDocumentSnapshots.getDocuments().get(0).getReference();
            documentReference.set(bankaccountItem);

            notificationHandler.SendTransferNotification("Sikeresen utaltál $" + amount + " összeget, neki: " + toNumber);
        });

        firestore.collection("Bankaccounts").whereEqualTo("number", toNumber).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.isEmpty()) {
                return;
            }

            BankaccountItem bankaccountItem = queryDocumentSnapshots.getDocuments().get(0).toObject(BankaccountItem.class);
            Objects.requireNonNull(bankaccountItem).setBalance(bankaccountItem.getBalance() + amount);
            DocumentReference documentReference = queryDocumentSnapshots.getDocuments().get(0).getReference();
            documentReference.set(bankaccountItem);
        });

        firestore.collection("Bankaccounts").whereEqualTo("number", toNumber).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.isEmpty()) return;

            firestore.collection("Transfers").add(new HistoryItem(user.getUid(), fromNumber, Objects.requireNonNull(queryDocumentSnapshots.getDocuments().get(0).get("ownerID")).toString(), toNumber, new Timestamp(new Date()), amount))
                    .addOnFailureListener(e -> Toast.makeText(this, "Network ERROR", Toast.LENGTH_SHORT).show());
        });
    }
}
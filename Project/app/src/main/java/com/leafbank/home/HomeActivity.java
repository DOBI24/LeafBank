package com.leafbank.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.leafbank.R;
import com.leafbank.bankaccount.BankaccountActivity;
import com.leafbank.history.HistoryInActivity;
import com.leafbank.profile.ProfileActivity;
import com.leafbank.transfer.TransferActivity;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements HomeButtons {
    public static FirebaseUser user;
    public static CollectionReference usersRef;
    public static CollectionReference bankaccountsRef;
    private TextView titleTextView;
    private LinearLayout titlelayout;

    public static void pageController(Context context, Class<?> dest) {
        if (context.getClass() == dest) return;
        Intent intent = new Intent(context, dest);
        context.startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    public static void setUserNameTextView(AppCompatActivity view) {
        TextView titleTextView = view.findViewById(R.id.home_title);
        usersRef.document(user.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("USERNAME");
                        titleTextView.setText("Szia " + username);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Objects.requireNonNull(getSupportActionBar()).hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }

        FirebaseAuth firebase = FirebaseAuth.getInstance();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        user = firebase.getCurrentUser();
        usersRef = firestore.collection("Users");
        bankaccountsRef = firestore.collection("Bankaccounts");

        if (user == null) finish();

        setUserNameTextView(this);
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

    public void sendmoneybtn_click(View view) {
        pageController(this, TransferActivity.class);
    }

    public void historybtn_click(View view) {
        pageController(this, HistoryInActivity.class);
    }

    public void atmbtn_click(View view) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 10);
            return;
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String uri = "geo:" + latitude + "," + longitude + "?q=atm";
                Uri geoUri = Uri.parse(uri);
                Intent intent = new Intent(Intent.ACTION_VIEW, geoUri);
                startActivity(intent);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

    }
}
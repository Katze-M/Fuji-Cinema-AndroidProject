package com.example.perez_ac08_movieticketbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // 1. Hardcoded Data
        // Replace these values with the specific names/details you want
        String fullName = "Katrina Perez";
        String contactInfo = "+63 912 345 6789 | kat.perez@email.com";

        // 2. Set UI Text
        TextView txtGreeting = findViewById(R.id.txtGreeting);
        TextView txtContactInfo = findViewById(R.id.txtContactInfo);

        // Applying the hardcoded strings to the UI
        txtGreeting.setText("Good Day, " + fullName + "!");
        txtContactInfo.setText(contactInfo);

        // 3. Navigation Setup
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_account);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_movies) {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                return true;
            } else if (id == R.id.nav_booking) {
                startActivity(new Intent(this, BookingActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                return true;
            }
            return true;
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
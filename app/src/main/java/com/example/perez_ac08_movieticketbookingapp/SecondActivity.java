package com.example.perez_ac08_movieticketbookingapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class SecondActivity extends AppCompatActivity {
    private boolean voucherApplied = false;
    private double finalPrice;
    private double discountAmount = 0;
    private int discountPercent = 0;
    private int initialPrice;
    private int ticketQty;
    private String voucherName = "None";
    private String voucherBenefit = "No Voucher Available";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // 1. Retrieve Data (Matching Keys from BookingActivity)
        Bundle extras = getIntent().getExtras();
        if (extras == null) return;

        String movie = extras.getString("MOVIE");
        String date = extras.getString("DATE");
        String time = extras.getString("TIME");
        String qtyStr = extras.getString("QTY");
        initialPrice = extras.getInt("TOTAL");
        ticketQty = Integer.parseInt(qtyStr != null ? qtyStr : "0");

        // Retrieve the Seat States Array
        int[] seatStates = extras.getIntArray("SEAT_STATES");

        // 2. Determine Voucher Tier
        calculateVoucherTier();

        // --- CHANGE: AUTO-APPLY LOGIC ---
        // If the user qualifies for a voucher, set it to applied by default
        if (!voucherName.equals("None")) {
            voucherApplied = true;
            Toast.makeText(this, "Voucher '" + voucherName + "' applied automatically!", Toast.LENGTH_SHORT).show();
        }
        // --------------------------------

        // 3. UI Elements
        TextView sumMovie = findViewById(R.id.sumMovie);
        TextView sumDateTime = findViewById(R.id.sumDateTime);
        TextView sumQty = findViewById(R.id.sumQty);
        TextView resInitialPrice = findViewById(R.id.resInitialPrice);
        TextView resDiscount = findViewById(R.id.resDiscount);
        TextView resFinalTotal = findViewById(R.id.resFinalTotal);
        TextView txtVoucherDesc = findViewById(R.id.txtVoucherDesc);
        GridLayout summarySeatGrid = findViewById(R.id.summarySeatGrid);
        Button btnVoucher = findViewById(R.id.btnVoucher);
        Button btnConfirm = findViewById(R.id.btnConfirmFinal);
        MaterialCardView btnBack = findViewById(R.id.btnBackToBooking);

        btnConfirm.setTextColor(Color.WHITE);

        // 4. Populate UI
        sumMovie.setText(movie);
        sumDateTime.setText("📅 " + date + "  •  🕒 " + time);
        sumQty.setText("🎟️ " + ticketQty + " Ticket(s)");
        resInitialPrice.setText("₱" + String.format("%.2f", (double)initialPrice));

        // This call will now reflect the auto-applied state immediately on load
        updateUI(resFinalTotal, resDiscount, txtVoucherDesc, btnVoucher);

        // 5. Seat Visualization
        if (seatStates != null) {
            for (int i = 0; i < seatStates.length; i++) {
                ImageView seatIcon = new ImageView(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 70; params.height = 70; params.setMargins(8, 8, 8, 8);
                seatIcon.setLayoutParams(params);

                if (seatStates[i] == 1) {
                    seatIcon.setImageResource(android.R.drawable.ic_menu_slideshow);
                    seatIcon.setColorFilter(Color.parseColor("#2D6A4F")); // Green (Selected)
                } else if (i % 7 == 0) {
                    seatIcon.setImageResource(android.R.drawable.ic_menu_slideshow);
                    seatIcon.setColorFilter(Color.BLACK); // Taken
                } else {
                    seatIcon.setImageResource(android.R.drawable.ic_menu_slideshow);
                    seatIcon.setColorFilter(Color.parseColor("#D1D5DB")); // Available
                }
                summarySeatGrid.addView(seatIcon);
            }
        }

        // Voucher Button Logic (Allows manual removal and re-application)
        btnVoucher.setOnClickListener(v -> {
            if (voucherName.equals("None")) {
                Toast.makeText(this, "Not enough tickets for a voucher.", Toast.LENGTH_SHORT).show();
                return;
            }
            voucherApplied = !voucherApplied;
            updateUI(resFinalTotal, resDiscount, txtVoucherDesc, btnVoucher);
        });

        btnBack.setOnClickListener(v -> finish());
        btnConfirm.setOnClickListener(v -> showConfirmModal());
    }

    private void calculateVoucherTier() {
        if (ticketQty >= 9) {
            discountPercent = 25;
            discountAmount = initialPrice * 0.25;
            voucherName = "Diamond";
            voucherBenefit = "Free 3 jumbo popcorns & 1L softdrink";
        } else if (ticketQty >= 6) {
            discountPercent = 15;
            discountAmount = initialPrice * 0.15;
            voucherName = "Premium";
            voucherBenefit = "Free 2 jumbo popcorns";
        } else if (ticketQty >= 3) {
            discountPercent = 5;
            discountAmount = initialPrice * 0.05;
            voucherName = "Standard";
            voucherBenefit = "Free 2 extra large popcorns";
        } else {
            discountPercent = 0;
            discountAmount = 0;
            voucherName = "None";
            voucherBenefit = "No voucher for less than 3 tickets";
        }
    }

    private void updateUI(TextView total, TextView discount, TextView desc, Button btn) {
        if (voucherApplied) {
            finalPrice = initialPrice - discountAmount;
            discount.setText("(" + discountPercent + "%) -₱" + String.format("%.2f", discountAmount));
            desc.setText(voucherBenefit);
            btn.setText("Remove " + voucherName);
            // Change color to Red to signal removal action
            btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D90429")));
            btn.setTextColor(Color.WHITE);
        } else {
            finalPrice = initialPrice;
            discount.setText("-₱0.00");
            desc.setText(voucherName.equals("None") ? voucherBenefit : "Available: " + voucherName);
            btn.setText("Apply Voucher");
            // Change color to Green to signal application action
            btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1B4332")));
            btn.setTextColor(Color.WHITE);
        }
        total.setText("₱" + String.format("%.2f", finalPrice));
    }

    private void showConfirmModal() {
        String customerName = getIntent().getStringExtra("NAME");
        String contactInfo = getIntent().getStringExtra("CONTACT");

        new AlertDialog.Builder(this)
                .setTitle("Finalize Booking")
                .setMessage("Confirm reservation for " + ticketQty + " seats under " + customerName + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    new AlertDialog.Builder(this)
                            .setTitle("Success!")
                            .setMessage("Tickets reserved. " + (voucherApplied ? "\nGift: " + voucherBenefit : ""))
                            .setPositiveButton("Main Menu", (d, w) -> {
                                Intent intent = new Intent(this, MainActivity.class);
                                intent.putExtra("NAME", customerName);
                                intent.putExtra("CONTACT", contactInfo);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
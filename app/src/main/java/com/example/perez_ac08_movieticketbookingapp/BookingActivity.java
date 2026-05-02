package com.example.perez_ac08_movieticketbookingapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import java.util.Calendar;

public class BookingActivity extends AppCompatActivity {
    private int selectedSeats = 0;
    private final int PRICE_PER_TICKET = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        MaterialCardView btnBack = findViewById(R.id.btnBackCard);
        AutoCompleteTextView autoMovie = findViewById(R.id.autoMovieTitle);
        Spinner spinnerTime = findViewById(R.id.spinnerTime);
        EditText editDate = findViewById(R.id.editDate);
        EditText editQty = findViewById(R.id.editTicketQty);
        TextView txtTotal = findViewById(R.id.txtTotalPrice);
        GridLayout seatGrid = findViewById(R.id.seatGrid);
        EditText editName = findViewById(R.id.editName);
        EditText editContact = findViewById(R.id.editContact);
        Button btnConfirm = findViewById(R.id.btnFinalBook);

        btnBack.setOnClickListener(v -> finish());

        // Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_booking);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_movies) {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                return true;
            } else if (id == R.id.nav_account) {
                Intent intent = new Intent(this, AccountActivity.class);
                intent.putExtra("NAME", editName.getText().toString());
                intent.putExtra("CONTACT", editContact.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            }
            return id == R.id.nav_booking;
        });

        // Setup Spinners
        String[] movies = {"Interstellar", "Doctor Sleep", "Get Out", "Me Before You", "Jumanji", "Dune: Part One", "Ready Player One", "Us"};
        autoMovie.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, movies));

        String[] times = {"10:00 AM", "01:00 PM", "03:00 PM", "05:00 PM"};
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, times);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(timeAdapter);

        // Date Picker
        editDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, day) -> editDate.setText(day + "/" + (month + 1) + "/" + year),
                    c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Price Updater
        editQty.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    try {
                        int qty = Integer.parseInt(s.toString());
                        txtTotal.setText("Amount Due: ₱" + (qty * PRICE_PER_TICKET));
                    } catch (NumberFormatException e) { txtTotal.setText("Amount Due: ₱0.00"); }
                } else {
                    txtTotal.setText("Amount Due: ₱0.00");
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Seat Grid Logic
        for (int i = 0; i < 20; i++) {
            Button seat = new Button(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 110; params.height = 110; params.setMargins(8, 8, 8, 8);
            seat.setLayoutParams(params);

            if (i % 7 == 0) {
                seat.setBackgroundColor(Color.BLACK);
                seat.setEnabled(false);
            } else {
                seat.setBackgroundColor(Color.parseColor("#9BA1A6"));
                seat.setOnClickListener(v -> {
                    if (seat.getTag() == null || (int)seat.getTag() == 0) {
                        seat.setBackgroundColor(Color.parseColor("#2D6A4F"));
                        seat.setTag(1); selectedSeats++;
                    } else {
                        seat.setBackgroundColor(Color.parseColor("#9BA1A6"));
                        seat.setTag(0); selectedSeats--;
                    }
                });
            }
            seatGrid.addView(seat);
        }

        // Final Submission
        btnConfirm.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String contact = editContact.getText().toString().trim();
            String movie = autoMovie.getText().toString();
            String date = editDate.getText().toString();
            String time = spinnerTime.getSelectedItem().toString();
            String qtyStr = editQty.getText().toString();

            // 1. Basic Validation: Empty Fields
            if (name.isEmpty() || contact.isEmpty() || movie.isEmpty() || date.isEmpty() || qtyStr.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            // 2. Quantity Validation: Zero Check
            int qty;
            try {
                qty = Integer.parseInt(qtyStr);
            } catch (NumberFormatException e) {
                qty = 0;
            }

            if (qty <= 0) {
                Toast.makeText(this, "Please enter at least 1 ticket.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 3. Selection Validation: Match tickets with selected seat grid
            if (selectedSeats == 0) {
                Toast.makeText(this, "No seats selected. Please pick your seats.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (qty != selectedSeats) {
                Toast.makeText(this, "Please select exactly " + qty + " seats.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Capture seat states into an array
            int[] seatStates = new int[20];
            for (int i = 0; i < seatGrid.getChildCount(); i++) {
                Button seat = (Button) seatGrid.getChildAt(i);
                seatStates[i] = (seat.getTag() != null && (int) seat.getTag() == 1) ? 1 : 0;
            }

            // Calculations for SecondActivity
            int finalTotal = qty * PRICE_PER_TICKET;

            Intent intent = new Intent(BookingActivity.this, SecondActivity.class);
            intent.putExtra("NAME", name);
            intent.putExtra("CONTACT", contact);
            intent.putExtra("MOVIE", movie);
            intent.putExtra("DATE", date);
            intent.putExtra("TIME", time);
            intent.putExtra("QTY", String.valueOf(qty)); // Ensure current qty is passed
            intent.putExtra("SEAT_STATES", seatStates);
            intent.putExtra("TOTAL", finalTotal);

            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
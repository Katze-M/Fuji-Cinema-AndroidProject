package com.example.perez_ac08_movieticketbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Movie> movies = new ArrayList<>();
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Setup 8 Movies
        movies.add(new Movie("Interstellar", "2h 49m", "Sci-Fi", "When Earth becomes uninhabitable in the future, a farmer and ex-NASA pilot, Joseph Cooper, is tasked to pilot a spacecraft, along with a team of researchers, to find a new planet for humans.", 5.0f, "10:00 AM", "03:00 PM", "https://www.imdb.com/title/tt0816692/", R.drawable.interstellar_poster));
        movies.add(new Movie("Doctor Sleep", "2h 32m", "Horror", "Years following the events of The Shining (1980), a now-adult Dan Torrance must protect a young girl with similar powers from a cult known as The True Knot, who prey on children with powers to remain immortal.", 4.0f, "01:00 PM", "05:00 PM", "https://www.imdb.com/title/tt5606664/", R.drawable.doctor_sleep_poster));
        movies.add(new Movie("Get Out", "1h 44m", "Horror-Thriller", "A young African-American visits his white girlfriend's parents for the weekend, where his simmering uneasiness about their reception of him eventually reaches a boiling point.", 4.5f, "10:00 AM", "01:00 PM", "https://www.imdb.com/title/tt5052448/", R.drawable.get_out_poster));
        movies.add(new Movie("Me Before You", "1h 50m", "Romance", "A girl in a small town forms an unlikely bond with a recently-paralyzed man she's taking care of.", 4.0f, "03:00 PM", "05:00 PM", "https://www.imdb.com/title/tt2674426/", R.drawable.me_before_you_poster));
        movies.add(new Movie("Jumanji", "1h 59m", "Adventure", "Four teenagers are sucked into a magical video game, and the only way they can escape is to work together to finish the game.", 3.5f, "10:00 AM", "05:00 PM", "https://www.imdb.com/title/tt2283362/", R.drawable.jumanji_poster));
        movies.add(new Movie("Dune: Part One", "2h 46m", "Sci-Fi", "Paul Atreides arrives on Arrakis after his father accepts the stewardship of the dangerous planet. However, chaos ensues after a betrayal as forces clash to control melange, a precious resource.", 4.5f, "01:00 PM", "03:00 PM", "https://www.imdb.com/title/tt1160419/", R.drawable.dune_poster));
        movies.add(new Movie("Ready Player One", "2h 20m", "Sci-Fi", "When the creator of a virtual reality called the OASIS dies, he makes a posthumous challenge to all OASIS users to find his Easter Egg, which will give the finder his fortune and control of his world.", 4.0f, "10:00 AM", "03:00 PM", "https://www.imdb.com/title/tt1677720/", R.drawable.ready_player_one_poster));
        movies.add(new Movie("Us", "1h 56m", "Horror", "In order to get away from their busy lives, the Wilson family takes a vacation to Santa Cruz, California. At night, four strangers break into Adelaide's childhood home. The family is shocked to find out that the intruders look like them.", 4.0f, "01:00 PM", "05:00 PM", "https://www.imdb.com/title/tt6857112/", R.drawable.us_poster));

        // 2. Setup ViewPager2
        viewPager = findViewById(R.id.movieViewPager);
        MovieAdapter adapter = new MovieAdapter(movies);
        viewPager.setAdapter(adapter);

        int initialPos = (Integer.MAX_VALUE / 2) - ((Integer.MAX_VALUE / 2) % movies.size());
        viewPager.setCurrentItem(initialPos, false);
        updateMovieDetails(initialPos % movies.size());

        // 3. Carousel Transformer
        viewPager.setOffscreenPageLimit(3);
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(40));
        transformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.80f + r * 0.20f);
            page.setScaleX(0.80f + r * 0.20f);
            page.setAlpha(0.6f + r * 0.4f);
        });
        viewPager.setPageTransformer(transformer);

        // 4. Parallax Scroll
        NestedScrollView mainScrollView = findViewById(R.id.mainScrollView);
        mainScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            viewPager.setTranslationY(-scrollY * 0.6f);
        });

        // 5. BUY TICKETS -> Start Booking Activity
        Button btnBookNow = findViewById(R.id.btnBookNow);
        btnBookNow.setOnClickListener(v -> {
            int currentIdx = viewPager.getCurrentItem() % movies.size();
            String currentTitle = movies.get(currentIdx).getTitle();

            Intent intent = new Intent(MainActivity.this, BookingActivity.class);
            intent.putExtra("MOVIE", currentTitle);
            startActivity(intent);
            // Apply Right-to-Left Slide
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // 6. BOTTOM NAVIGATION LINKING
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_movies);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_movies) {
                mainScrollView.smoothScrollTo(0, 0);
                return true;
            } else if (id == R.id.nav_booking) {
                startActivity(new Intent(MainActivity.this, BookingActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            } else if (id == R.id.nav_account) {
                startActivity(new Intent(MainActivity.this, AccountActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            }
            return false;
        });

        // 7. Search logic
        ImageView btnSearch = findViewById(R.id.btnSearch);
        EditText editSearch = findViewById(R.id.editSearch);
        btnSearch.setOnClickListener(v -> {
            String query = editSearch.getText().toString().toLowerCase().trim();
            if (query.isEmpty()) return;
            for (int i = 0; i < movies.size(); i++) {
                if (movies.get(i).getTitle().toLowerCase().contains(query)) {
                    int currentPos = viewPager.getCurrentItem();
                    int offset = i - (currentPos % movies.size());
                    viewPager.setCurrentItem(currentPos + offset, true);
                    return;
                }
            }
            Toast.makeText(this, "Movie not found!", Toast.LENGTH_SHORT).show();
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateMovieDetails(position % movies.size());
            }
        });
    }

    private void updateMovieDetails(int index) {
        LinearLayout detailCardContent = findViewById(R.id.detailCardContent);
        if (detailCardContent == null) return;

        Movie movie = movies.get(index);

        detailCardContent.animate().alpha(0f).translationY(20f).setDuration(200).withEndAction(() -> {
            ((TextView)findViewById(R.id.txtMovieTitle)).setText(movie.getTitle());
            ((TextView)findViewById(R.id.txtDuration)).setText(movie.getDuration());
            ((TextView)findViewById(R.id.txtGenre)).setText(movie.getGenre());
            ((TextView)findViewById(R.id.txtDescription)).setText(movie.getDescription());
            ((RatingBar)findViewById(R.id.movieRating)).setRating(movie.getRating());

            // Casting to TextView
            ((TextView)findViewById(R.id.time1)).setText(movie.getTime1());
            ((TextView)findViewById(R.id.time2)).setText(movie.getTime2());

            detailCardContent.setTranslationY(-20f);
            detailCardContent.animate().alpha(1f).translationY(0f).setDuration(200).start();
        }).start();
    }
}
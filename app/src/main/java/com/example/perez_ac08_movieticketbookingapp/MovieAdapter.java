package com.example.perez_ac08_movieticketbookingapp;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> movieList;

    public MovieAdapter(List<Movie> movieList) { this.movieList = movieList; }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position % movieList.size());
        holder.imageView.setImageResource(movie.getImageRes());

        // External URL Link Logic
        holder.imageView.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getMovieUrl()));
            v.getContext().startActivity(browserIntent);
        });
    }

    @Override
    public int getItemCount() { return Integer.MAX_VALUE; }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        MovieViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.moviePoster);
        }
    }
}
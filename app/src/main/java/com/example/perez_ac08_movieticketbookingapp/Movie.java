package com.example.perez_ac08_movieticketbookingapp;

public class Movie {
    private String title, duration, genre, description, time1, time2;
    private float rating;
    private int imageRes;
    private String movieUrl;

    public Movie(String title, String duration, String genre, String description, float rating, String time1, String time2, String movieUrl, int imageRes) {
        this.title = title;
        this.duration = duration;
        this.genre = genre;
        this.description = description;
        this.rating = rating;
        this.time1 = time1;
        this.time2 = time2;
        this.movieUrl = movieUrl;
        this.imageRes = imageRes;
    }

    public String getTitle() { return title; }
    public String getDuration() { return duration; }
    public String getGenre() { return genre; }
    public String getDescription() { return description; }
    public float getRating() { return rating; }
    public String getTime1() { return time1; }
    public String getTime2() { return time2; }
    public String getMovieUrl() { return movieUrl; }
    public int getImageRes() { return imageRes; }
}
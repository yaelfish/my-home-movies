package com.example.jbt.myhomemovies;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

public class TitleMovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_movie_detail);

        TextView title = (TextView) findViewById(R.id.textViewTitleDetailedLayout);
        Movie movie = getIntent().getParcelableExtra("movie");
        if (movie != null) {
        title.setText(movie.getTitle()); }

    }
}
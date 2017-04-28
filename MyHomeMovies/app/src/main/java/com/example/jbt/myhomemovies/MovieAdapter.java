package com.example.jbt.myhomemovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jbt on 11/15/2016.
 */
public class MovieAdapter extends ArrayAdapter <Movie> {

    public MovieAdapter(Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // check if convertView is empty (first time loading this item) - Check if an existing view is being reused, otherwise inflate the view
        if(convertView == null){
            // inflate (convert xml to Java objects) the layout file into convertView
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_title_movie_detail, null);
        }
        // find child view inside our item layout
        TextView movieTitle = (TextView) convertView.findViewById(R.id.textViewTitleDetailedLayout);
        Movie currentMovie = getItem(position);
        movieTitle.setText(currentMovie.getTitle());


        return convertView;
    }
}

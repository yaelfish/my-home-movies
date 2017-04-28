package com.example.jbt.myhomemovies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static android.R.attr.id;

public class MovieAct extends AppCompatActivity implements View.OnClickListener {

    private EditText titleEdit, bodyEdit, urlEdit;
    private ImageView imageViewPoster;
    private MoviesDatabaseHelper helper;

    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);


        titleEdit = (EditText) findViewById(R.id.editTitle);
        bodyEdit = (EditText) findViewById(R.id.editBody);
        urlEdit = (EditText) findViewById(R.id.editUrl);
        imageViewPoster = (ImageView) findViewById(R.id.imageView_poster);

        findViewById(R.id.btn_show_image).setOnClickListener(this);
        findViewById(R.id.btn_save_act2).setOnClickListener(this);
        findViewById(R.id.btn_share).setOnClickListener(this);

        helper = new MoviesDatabaseHelper(this);

        movie = getIntent().getParcelableExtra("movie");
        if (movie != null) {
            titleEdit.setText(movie.getTitle());
            bodyEdit.setText(movie.getBody());
            urlEdit.setText(movie.getUrlPoster());
        }

    }

    @Override
    public void onClick(View button) {
        switch (button.getId()) {
            case R.id.btn_show_image:
                new ImageDownloadTask().execute(urlEdit.getText().toString());

                break;

            // *** Sharing Data using intent *** //
            case R.id.btn_share:
                Intent inshare = new Intent();
                inshare.setAction(Intent.ACTION_SEND);
                inshare.setType("text/plain");

                //Add data to the intent
                String shareMvString = getString(R.string.string_share_header) +
                        getString(R.string.share_title) + titleEdit.getText().toString() +
                        getString(R.string.share_body) + bodyEdit.getText().toString() +
                        getString(R.string.share_url) + urlEdit.getText().toString();
                inshare.putExtra(Intent.EXTRA_TEXT, shareMvString);
                startActivity(inshare);
                break;

            case R.id.btn_save_act2:

                String title = titleEdit.getText().toString();
                String body = bodyEdit.getText().toString();
                String urlPoster = urlEdit.getText().toString();

                Movie m = new Movie(title, body, urlPoster);

                // *** saving movie - check if exist and if so replacing old movie with edited one *** //

                boolean sentForEditing = getIntent().getBooleanExtra("edit", false);
                if (sentForEditing) {
                    long id = movie.getId();
                    Movie editedMovie = new Movie(id, title, body, urlPoster);
                    helper.updateMovie(editedMovie);
                } else {
                    helper.addMovie(m);
                }
                Toast.makeText(this, "Movie " + " '" + m.getTitle() + "' " + "Added Successfully!", Toast.LENGTH_LONG).show();

                finish();
                break;
        }
    }

    private class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {

            Bitmap image = null;
            HttpURLConnection connection = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return null;
                }

                // decode the byte stream from the internet into a Bitmap object
                image = BitmapFactory.decodeStream(connection.getInputStream());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            // set the Bitmap in the ImageView
            imageViewPoster.setImageBitmap(bitmap);

        }
    }
}








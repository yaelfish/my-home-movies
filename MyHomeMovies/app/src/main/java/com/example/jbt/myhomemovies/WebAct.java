package com.example.jbt.myhomemovies;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class WebAct extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private EditText enterMovieName;
    private ArrayAdapter<String> adapterWebAct;
    private ArrayList<String> imdbIdsResult;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        enterMovieName = (EditText) findViewById(R.id.editTextSearchWeb);
        enterMovieName.setHintTextColor(Color.WHITE);
        adapterWebAct = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        imdbIdsResult = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView_web_act);
        listView.setAdapter(adapterWebAct);

        listView.setOnItemClickListener(this);
        findViewById(R.id.imageButton).setOnClickListener(this);

    }

    private class WebActTask extends AsyncTask<String, Void, ArrayList<Pair<String, String>>> {

        private final String URL_FILM_ARR = "http://www.omdbapi.com/?s=%s";

        @Override
        protected ArrayList<Pair<String, String>> doInBackground(String... param_search_strings) {

            HttpURLConnection connection = null;
            BufferedReader reader;
            StringBuilder builder = new StringBuilder();

            try {
                String formattedUrls = param_search_strings[0].replace(" ", "%20");
                URL url = new URL(String.format(URL_FILM_ARR, formattedUrls));
                connection = (HttpURLConnection) url.openConnection();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return new ArrayList<Pair<String, String>>() {{
                        add(new Pair<String, String>("Error from server! No Connection", null));
                    }};
                }

                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line = reader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = reader.readLine();
                }

                JSONObject root = new JSONObject(builder.toString());
                JSONArray search = root.getJSONArray("Search");
                ArrayList<Pair<String, String>> moviesArrayList = new ArrayList<>();
                for (int i = 0; i < search.length(); i++) {
                    JSONObject movieObj = search.getJSONObject(i);
                    String title = movieObj.getString("Title");
                    String imdbId = movieObj.getString("imdbID");

                    moviesArrayList.add(new Pair(title, imdbId));
                }
                return moviesArrayList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // if an error occurred send it to onPostExecute
            return new ArrayList<Pair<String, String>>() {{
                add(new Pair<String, String>("Error from server!", null));
            }};

        }

        @Override
        protected void onPostExecute(ArrayList<Pair<String, String>> stringsMovies) {
            super.onPostExecute(stringsMovies);
            adapterWebAct.clear();
            for (Pair<String, String> pair : stringsMovies) {
                adapterWebAct.add(pair.first);
                imdbIdsResult.add(pair.second);
            }
        }
    }

    @Override
    public void onClick(View view) {

        if (enterMovieName.getText().toString().contentEquals("")) {
            Toast.makeText(WebAct.this, "Please insert movie title", Toast.LENGTH_LONG).show();
        } else {
            new WebActTask().execute(enterMovieName.getText().toString());

            enterMovieName.setText("");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
        String selectedId = imdbIdsResult.get(index);
        new ItemClickedTask().execute(selectedId);
    }

    private class ItemClickedTask extends AsyncTask<String, Void, Movie> {

        private final String URL_IMDB_ID = "http://www.omdbapi.com/?i=%s";

        @Override
        protected Movie doInBackground(String... stringsIds) {

            HttpURLConnection connection = null;
            BufferedReader reader;
            StringBuilder builder = new StringBuilder();

            try {
                String stringIds = stringsIds[0];
                URL urll = new URL(String.format(URL_IMDB_ID, stringIds));
                connection = (HttpURLConnection) urll.openConnection();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line = reader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = reader.readLine();
                }

                JSONObject root = new JSONObject(builder.toString());

                String title = root.getString("Title");
                String body = root.getString("Plot");
                String urlPoster = root.getString("Poster");

                Movie m = new Movie(title, body, urlPoster);

                return m;


            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Movie movie) {
            Intent in = new Intent(WebAct.this, MovieAct.class);
            in.putExtra("movie", movie);
            startActivity(in);
            finish();
        }
    }
}



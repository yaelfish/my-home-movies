package com.example.jbt.myhomemovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jbt on 11/15/2016.
 */
public class MoviesDatabaseHelper extends SQLiteOpenHelper {

    private static final String MOVIE_TABLE_NAME = "movies";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_BODY = "body";
    private static final String COL_URL = "url";

    public MoviesDatabaseHelper(Context context) {
        super(context, "movies.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT)",
                MOVIE_TABLE_NAME, COL_ID, COL_TITLE, COL_BODY, COL_URL);
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int fromVersion, int toVersion) {
    }

    // *****************
    // COMMANDS
    //****************

    public void addMovie(Movie movie) {

        // map of columns and values in the table
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, movie.getTitle());
        values.put(COL_BODY, movie.getBody());
        values.put(COL_URL, movie.getUrlPoster());

        // get reference to the database
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        // insert the new movie values to the table movies
        sqLiteDatabase.insert(MOVIE_TABLE_NAME, null, values);

        // close the database connection
        sqLiteDatabase.close();
    }

    public void updateMovie(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, movie.getTitle());
        values.put(COL_BODY, movie.getBody());
        values.put(COL_URL, movie.getUrlPoster());

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.update(MOVIE_TABLE_NAME, values, COL_ID + "=" + movie.getId(), null);
        sqLiteDatabase.close();

    }

    public ArrayList<Movie> getAllMovies() {
        ArrayList<Movie> movies = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor c = sqLiteDatabase.query(MOVIE_TABLE_NAME, null, null, null, null, null, null);

        // loop while there are rows in the cursor
        while (c.moveToNext()) {

            long id = c.getLong(c.getColumnIndex(COL_ID));
            String title = c.getString(c.getColumnIndex(COL_TITLE));
            String body = c.getString(c.getColumnIndex(COL_BODY));
            String urlPoster = c.getString(c.getColumnIndex(COL_URL));

            movies.add(new Movie(id, title, body, urlPoster));
        }
        sqLiteDatabase.close();
        return movies;
    }

    public void deleteMovie(long id) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(MOVIE_TABLE_NAME, COL_ID + "=" + id, null);
        sqLiteDatabase.close();
    }


    public void removeAllMovies() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(MOVIE_TABLE_NAME, null, null);
        sqLiteDatabase.close();
    }
}

package com.example.jbt.myhomemovies;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AlertDialog.OnClickListener, View.OnClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    private AlertDialog dialog_delete, dialog_add_movie, dialogDeleteOrEdit;
    private MoviesDatabaseHelper helper;
    private MovieAdapter adapter;
    private int pos;
    private ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new MoviesDatabaseHelper(this);
        adapter = new MovieAdapter(this, R.layout.activity_title_movie_detail);
        list = (ListView) findViewById(R.id.listView);
        list.setAdapter(adapter);
        list.setOnItemLongClickListener(this);
        list.setOnItemClickListener(this);

        findViewById(R.id.btn_add).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.clear();
        adapter.addAll(helper.getAllMovies());
    }

    // add movie button
    @Override
    public void onClick(View view) {
        dialog_add_movie = new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_add_film_title)
                .setMessage(R.string.dialog_add_film_message)
                .setPositiveButton(R.string.web, this)
                .setNegativeButton(R.string.manual, this)
                .create();
        dialog_add_movie.show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        pos = position;
        Movie movie = adapter.getItem(pos);
        Intent inEdit = new Intent(MainActivity.this, MovieAct.class);
        inEdit.putExtra("movie", movie);
        // flag for editing
        inEdit.putExtra("edit", true);
        startActivity(inEdit);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long x) {
        pos = position;
        dialogDeleteOrEdit = new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_delete_item_title)
                .setMessage(R.string.dialog_delete_item_message)
                .setPositiveButton(R.string.dialog_delete_btn_edit, this)
                .setNegativeButton(R.string.dialog_delete_btn_delete_item, this)
                .create();
        dialogDeleteOrEdit.show();
        return true;
    }
    // *********** Creating menu  ************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Selecting item on menu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exit_app:
                finish();
                break;
            case R.id.delete_all_movies_list:
                dialog_delete = new AlertDialog.Builder(this)
                        .setTitle(R.string.dialog_delete_title)
                        .setMessage(R.string.dialog_delete_message)
                        .setPositiveButton(R.string.dialog_delete_btn_yes, this)
                        .setNegativeButton(R.string.dialog_delete_btn_no, this)
                        .create();
                dialog_delete.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // Alert dialogs

    @Override
    public void onClick(DialogInterface dialogInterface, int button) {

        if (dialogInterface == dialog_delete) {
            switch (button) {
                case DialogInterface.BUTTON_POSITIVE:
                    helper.removeAllMovies();
                    adapter.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, R.string.toast_delete_yes, Toast.LENGTH_SHORT).show();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    Toast.makeText(MainActivity.this, R.string.toast_delete_no, Toast.LENGTH_SHORT).show();
                    break;
            }
        } else if (dialogInterface == dialog_add_movie) {
            switch (button) {

                // in case user chose web
                case DialogInterface.BUTTON_POSITIVE:
                    Intent intentWeb = new Intent(MainActivity.this, WebAct.class);
                    startActivity(intentWeb);
                    break;

                // in case user chose manual
                case DialogInterface.BUTTON_NEGATIVE:
                    Intent intentMovie = new Intent(MainActivity.this, MovieAct.class);
                    startActivity(intentMovie);
                    break;
            }

        } else if (dialogInterface == dialogDeleteOrEdit) {
            try {
                Movie movie = adapter.getItem(pos);
                switch (button) {

                    // in case user chose edit
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent inEdit = new Intent(MainActivity.this, MovieAct.class);
                        inEdit.putExtra("movie", movie);
                        startActivity(inEdit);
                        break;

                    // in case user chose delete
                    case DialogInterface.BUTTON_NEGATIVE:

                        // delete from database
                        helper.deleteMovie(movie.getId());
                        // remove from the list
                        adapter.remove(movie);
                }
            } catch (NullPointerException e) {
                Toast.makeText(this, "There are no movies in your list yet", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
package com.button.musicsearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class SavedSongsActivity extends AppCompatActivity
{
    private MyAdapter adapter;
    private ListView listView;
    private EditText searchText;

    Context context;

    ArrayList<String> songNames = new ArrayList<String>();
    ArrayList<String> artistNames = new ArrayList<String>();
    ArrayList<String> albumNames = new ArrayList<String>();
    ArrayList<String> albumImages = new ArrayList<String>();
    ArrayList<String> songsPreview = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_saved_songs);
        context = this;

        listView = findViewById(R.id.listView);
        searchText = findViewById(R.id.searchText);



    }
    protected void onResume()
    {
        super.onResume();

        songNames = new ArrayList<String>(HomeActivity.savedSongNames);
        artistNames = new ArrayList<String>(HomeActivity.savedArtistNames);
        albumNames = new ArrayList<String>(HomeActivity.savedAlbumNames);
        albumImages = new ArrayList<String>(HomeActivity.savedAlbumImages);
        songsPreview = new ArrayList<String>(HomeActivity.savedSongsPreview);

        adapter = new MyAdapter(this, songNames, artistNames, albumNames, albumImages);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(context, SongView.class);
                intent.putExtra("songName", songNames.get(position));
                intent.putExtra("artistName", artistNames.get(position));
                intent.putExtra("albumName", albumNames.get(position));
                intent.putExtra("albumImage", albumImages.get(position));
                intent.putExtra("songPreview", songsPreview.get(position));
                startActivity(intent);
            }
        });
    }

    public void OnSearchClick(View view)
    {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
        finish();
    }

    public void OnHomeClick(View view)
    {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}

package com.button.musicsearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.SharedPreferencesCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SongView extends AppCompatActivity
{
    private String songName;
    private String artistName;
    private String albumName;
    private String albumImage;
    private String songPreview;

    Context context;

    TextView songNameText;
    TextView artistNameText;
    TextView albumNameText;
    ImageView albumImageView;

    private final String apiString = "https://api.deezer.com/search?q=";

    RequestQueue requestQueue;

    MyAdapter adapter;
    ListView listView;

    // Data about other songs
    ArrayList<String> songNames = new ArrayList<String>();
    ArrayList<String> artistNames = new ArrayList<String>();
    ArrayList<String> albumNames = new ArrayList<String>();
    ArrayList<String> albumImages = new ArrayList<String>();
    ArrayList<String> songsPreview = new ArrayList<String>();

    MediaPlayer mediaPlayer;

    SharedPreferences sharedPref;

    private ImageButton playButton;

    private int isSongPlaying = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_view);
        context = this;

        this.getSupportActionBar().hide();

        // Getting the views
        songNameText = findViewById(R.id.songName);
        artistNameText = findViewById(R.id.artistName);
        albumNameText = findViewById(R.id.albumName);
        albumImageView = findViewById(R.id.imageView);

        playButton = findViewById(R.id.playButton);

        // Getting the data from the intent
        Intent intent = getIntent();
        songName = intent.getStringExtra("songName");
        artistName = intent.getStringExtra("artistName");
        albumName = intent.getStringExtra("albumName");
        albumImage = intent.getStringExtra("albumImage");
        songPreview = intent.getStringExtra("songPreview");

        // Setting the data
        songNameText.setText(songName);
        artistNameText.setText("Artist:" + artistName);
        albumNameText.setText("Album:" + albumName);
        albumNameText.setMovementMethod(new ScrollingMovementMethod());
        // Loading image
        Picasso.get().load(albumImage).into(albumImageView);

        listView = findViewById(R.id.listView);

        // Creating the adapter
        adapter = new MyAdapter(this, songNames, artistNames, albumNames, albumImages);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                if (isSongPlaying != 0)
                {
                    //mediaPlayer.stop();

                    int icon = R.drawable.ic_play_circle_outline_24px;
                    playButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), icon));
                }
                Intent intent = new Intent(context, SongView.class);
                intent.putExtra("songName", songNames.get(position));
                intent.putExtra("artistName", artistNames.get(position));
                intent.putExtra("albumName", albumNames.get(position));
                intent.putExtra("albumImage", albumImages.get(position));
                intent.putExtra("songPreview", songsPreview.get(position));
                startActivity(intent);
            }
        });

        sharedPref = getApplicationContext().getSharedPreferences("Pulla12", Context.MODE_PRIVATE);

        // Searching for other songs
        SearchOtherSongs(albumName);
    }

    protected void onResume()
    {
        super.onResume();
        isSongPlaying = 0;

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    protected  void onPause()
    {
        super.onPause();
        if (mediaPlayer != null)
        {
            mediaPlayer.stop();
        }

        int icon = R.drawable.ic_play_circle_outline_24px;
        playButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), icon));
    }

    private void SearchOtherSongs(String res)
    {
        Log.d("AAA", "Searched: " + "\"" + res + "\"");
        final String apiSearch = apiString + res;

        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, apiSearch, null,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response)
                            {
                                // Log.d("AAA", response.toString());

                                try
                                {
                                    JSONArray array = response.getJSONArray("data");

                                    if (array.length() == 0)
                                    {
                                        Log.d("AAA", "No results");
                                    }
                                    else
                                    {
                                        for (int i = 0; i < array.length(); i++)
                                        {
                                            JSONObject songInfo = array.getJSONObject(i);

                                            int id = songInfo.getInt("id");
                                            String title = songInfo.getString("title");
                                            String link = songInfo.getString("link");
                                            String preview = songInfo.getString("preview");
                                            int duration = songInfo.getInt("duration");


                                            JSONObject artist = songInfo.getJSONObject("artist");

                                            int artistID = artist.getInt("id");
                                            String artistName = artist.getString("name");
                                            String artistLink = artist.getString("link");

                                            JSONObject album = songInfo.getJSONObject("album");
                                            String albumName = album.getString("title");
                                            String albumImageURL = album.getString("cover_medium");

                                            Log.d("AAA", id + " ::: " + title + " ::: " + link + " ::: " + preview + " ::: " + duration + " ::: " + artistID + " ::: " + artistName + " ::: " + artistLink + " ::: " + albumImageURL);

                                            songNames.add(title);
                                            artistNames.add("By " + artistName);
                                            albumNames.add(albumName);
                                            albumImages.add(albumImageURL);
                                            songsPreview.add(preview);
                                        }
                                        adapter.notifyDataSetChanged();

                                    }
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener()
                {

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.d("AAA", "onErrorResponse: ERROR");
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    public void onBackClick(View view)
    {
        finish();
    }

    public void OnPlayClick(View view)
    {
        if (isSongPlaying == 0)
        {
            try
            {

                mediaPlayer.setDataSource(songPreview);
                mediaPlayer.prepare(); // might take long! (for buffering, etc)
                mediaPlayer.start();
                isSongPlaying = 1;
                int icon = R.drawable.ic_pause_circle_outline_24px;
                playButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), icon));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        else if (isSongPlaying == 1)
        {
            isSongPlaying = 2;
            mediaPlayer.pause();
            int icon = R.drawable.ic_play_circle_outline_24px;
            playButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), icon));
        }
        else
        {
            isSongPlaying = 1;
            mediaPlayer.start();
            int icon = R.drawable.ic_pause_circle_outline_24px;
            playButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), icon));
        }

    }

    public void OnSaveClick(View view)
    {
        SharedPreferences.Editor editor = sharedPref.edit();

        HomeActivity.savedSongNames.add(songName);
        HomeActivity.savedArtistNames.add(artistName);
        HomeActivity.savedAlbumNames.add(albumName);
        HomeActivity.savedAlbumImages.add(albumImage);
        HomeActivity.savedSongsPreview.add(songPreview);

        editor.clear();

        HashSet<String> savedSongNamesHash = new HashSet<String>();
        HashSet<String> savedArtistNamesHash = new HashSet<String>();
        HashSet<String> savedAlbumNamesHash = new HashSet<String>();
        HashSet<String> savedAlbumImagesHash = new HashSet<String>();
        HashSet<String> savedSongsPreviewHash = new HashSet<String>();
        for (int i = 0; i < HomeActivity.savedSongNames.size(); i++)
        {
            savedSongNamesHash.add("" + (1000000 + i) + HomeActivity.savedSongNames.get(i));
            savedArtistNamesHash.add("" + (1000000 + i) + HomeActivity.savedArtistNames.get(i));
            savedAlbumNamesHash.add("" + (1000000 + i) + HomeActivity.savedAlbumNames.get(i));
            savedAlbumImagesHash.add("" + (1000000 + i) + HomeActivity.savedAlbumImages.get(i));
            savedSongsPreviewHash.add("" + (1000000 + i) + HomeActivity.savedSongsPreview.get(i));
        }

        Log.d("DDD", savedSongNamesHash.toString());

        editor.putStringSet("songNames", savedSongNamesHash);
        editor.putStringSet("artistNames", savedArtistNamesHash);
        editor.putStringSet("albumNames", savedAlbumNamesHash);
        editor.putStringSet("albumImages", savedAlbumImagesHash);
        editor.putStringSet("songsPreview", savedSongsPreviewHash);

        editor.commit();


    }
}

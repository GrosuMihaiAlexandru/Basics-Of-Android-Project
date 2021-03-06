package com.button.musicsearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeActivity extends AppCompatActivity
{
    private final String apiString = "https://api.deezer.com/search?q=";

    RequestQueue requestQueue;

    private EditText searchText;

    SharedPreferences sharedPref;

    // Data about all saved songs
    public static ArrayList<String> savedSongNames = new ArrayList<String>();
    public static ArrayList<String> savedArtistNames = new ArrayList<String>();
    public static ArrayList<String> savedAlbumNames = new ArrayList<String>();
    public static ArrayList<String> savedAlbumImages = new ArrayList<String>();
    public static ArrayList<String> savedSongsPreview = new ArrayList<String>();

    private ArrayList<String> recommendedSongNames = new ArrayList<String>();
    private ArrayList<String> recommendedArtistNames = new ArrayList<String>();
    private ArrayList<String> recommendedAlbumNames = new ArrayList<String>();
    private ArrayList<String> recommendedAlbumImages = new ArrayList<String>();
    private ArrayList<String> recommendedSongsPreview = new ArrayList<String>();

    private MyAdapter adapter;
    private ListView listView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_home);
        searchText = findViewById(R.id.searchText);
        listView = findViewById(R.id.listView);

        context = this;

        sharedPref = getApplicationContext().getSharedPreferences("Pulla14", Context.MODE_PRIVATE);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        LoadSavedSongs();
    }

    private void LoadSavedSongs()
    {
        savedSongNames = new ArrayList<String>(sharedPref.getStringSet("songNames", new HashSet<String>()));
        savedArtistNames = new ArrayList<String>(sharedPref.getStringSet("artistNames", new HashSet<String>()));
        savedAlbumNames = new ArrayList<String>(sharedPref.getStringSet("albumNames", new HashSet<String>()));
        savedAlbumImages = new ArrayList<String>(sharedPref.getStringSet("albumImages", new HashSet<String>()));
        savedSongsPreview = new ArrayList<String>(sharedPref.getStringSet("songsPreview", new HashSet<String>()));

        Collections.sort(savedSongNames);
        Collections.sort(savedArtistNames);
        Collections.sort(savedAlbumNames);
        Collections.sort(savedAlbumImages);
        Collections.sort(savedSongsPreview);

        for(int i = 0; i < savedSongNames.size(); i++)
        {
            savedSongNames.set(i, savedSongNames.get(i).substring(7, savedSongNames.get(i).length()));
            savedArtistNames.set(i, savedArtistNames.get(i).substring(7, savedArtistNames.get(i).length()));
            savedAlbumNames.set(i, savedAlbumNames.get(i).substring(7, savedAlbumNames.get(i).length()));
            savedAlbumImages.set(i, savedAlbumImages.get(i).substring(7, savedAlbumImages.get(i).length()));
            savedSongsPreview.set(i, savedSongsPreview.get(i).substring(7, savedSongsPreview.get(i).length()));
        }

        Log.d("DDD", savedSongNames.toString());

        if (savedSongNames.size() == 0)
        {
            /*
            recommendedSongNames.add("List empty");
            recommendedArtistNames.add("Favorite new songs in Search");
            recommendedAlbumNames.add("");
            recommendedAlbumImages.add("");
            recommendedSongsPreview.add("");
            */
            recommendedSongNames = new ArrayList<String>();
            recommendedArtistNames = new ArrayList<String>();
            recommendedAlbumNames = new ArrayList<String>();
            recommendedAlbumImages = new ArrayList<String>();
            recommendedSongsPreview = new ArrayList<String>();
        }
        else if (savedSongNames.size() < 5)
        {
            recommendedSongNames = new ArrayList<String>(savedSongNames);
            recommendedArtistNames = new ArrayList<String>(savedArtistNames);
            recommendedAlbumNames = new ArrayList<String>(savedAlbumNames);
            recommendedAlbumImages = new ArrayList<String>(savedAlbumImages);
            recommendedSongsPreview = new ArrayList<String>(savedSongsPreview);
        }
        else {
            ArrayList<Integer> list = new ArrayList<Integer>();
            for (int i = 1; i < savedSongNames.size(); i++) {
                list.add(new Integer(i));
            }
            Collections.shuffle(list);
            for (int i = 0; i < 5; i++) {
                System.out.println(list.get(i));

                recommendedSongNames.add(new ArrayList<String>(savedSongNames).get(list.get(i)));
                recommendedArtistNames.add(new ArrayList<String>(savedArtistNames).get(list.get(i)));
                recommendedAlbumNames.add(new ArrayList<String>(savedAlbumNames).get(list.get(i)));
                recommendedAlbumImages.add(new ArrayList<String>(savedAlbumImages).get(list.get(i)));
                recommendedSongsPreview.add(new ArrayList<String>(savedSongsPreview).get(list.get(i)));
            }
        }

        adapter = new MyAdapter(this, recommendedSongNames, recommendedArtistNames, recommendedAlbumNames, recommendedAlbumImages);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(context, SongView.class);
                intent.putExtra("songName", recommendedSongNames.get(position));
                intent.putExtra("artistName", recommendedArtistNames.get(position));
                intent.putExtra("albumName", recommendedAlbumNames.get(position));
                intent.putExtra("albumImage", recommendedAlbumImages.get(position));
                intent.putExtra("songPreview", recommendedSongsPreview.get(position));
                startActivity(intent);
            }
        });
    }

    public void OnClick(View view)
    {
        String res = searchText.getText().toString();
        Log.d("AAA", "Searched: " + "\"" + res + "\"");
        final String apiSearch = apiString + res;

        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, apiSearch, null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
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
                                        for(int i = 0; i < array.length(); i++)
                                        {
                                            JSONObject songInfo = array.getJSONObject(i);

                                            int id  = songInfo.getInt("id");
                                            String title = songInfo.getString("title");
                                            String link = songInfo.getString("link");
                                            String preview = songInfo.getString("preview");
                                            int duration = songInfo.getInt("duration");

                                            JSONObject artist = songInfo.getJSONObject("artist");

                                            int artistID = artist.getInt("id");
                                            String artistName = artist.getString("name");
                                            String artistLink = artist.getString("link");

                                            Log.d("AAA", id + " ::: " + title + " ::: " + link + " ::: " + preview + " ::: " + duration + " ::: " + artistID + " ::: " + artistName + " ::: " + artistLink);

                                        }
                                    }
                                }
                                catch(Exception e)
                                {
                                    e.printStackTrace();
                                }



                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("AAA", "onErrorResponse: ERROR");
                    }});

        requestQueue.add(jsonObjectRequest);
    }

    public void OnSearchClick(View view)
    {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
        finish();
    }

    public void OnSavedSongsClick(View view)
    {
        Intent intent = new Intent(this, SavedSongsActivity.class);
        startActivity(intent);
        finish();
    }
}

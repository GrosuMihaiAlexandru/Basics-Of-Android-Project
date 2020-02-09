package com.button.musicsearch;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class MyAdapter extends ArrayAdapter<String>
{
    Context context;
    ArrayList<String> rSongName = new ArrayList<String>();
    ArrayList<String> rArtistName = new ArrayList<String>();
    ArrayList<String> rAlbumName = new ArrayList<String>();
    ArrayList<String> rAlbumImage = new ArrayList<String>();


    MyAdapter(Context context, ArrayList<String> SongName, ArrayList<String> artistName, ArrayList<String> albumName, ArrayList<String> albumImage) {
        super(context, R.layout.row, R.id.songName, SongName);
        this.context = context;
        this.rSongName = SongName;
        this.rArtistName = artistName;
        this.rAlbumName = albumName;
        this.rAlbumImage = albumImage;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row, parent, false);
        TextView songName = row.findViewById(R.id.songName);
        TextView artistName = row.findViewById(R.id.artistName);
        TextView albumName = row.findViewById(R.id.albumName);
        ImageView albumImage = row.findViewById(R.id.image);



        songName.setText(rSongName.get(position));
        artistName.setText(rArtistName.get(position));
        albumName.setText(rAlbumName.get(position));
        Picasso.get().load(rAlbumImage.get(position)).into(albumImage);

        // Load Image

        if (position % 2 == 1)
            row.setBackgroundColor(Color.rgb(235, 235, 235));

        return row;
    }
}
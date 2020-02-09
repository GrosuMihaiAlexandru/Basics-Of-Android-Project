package com.button.musicsearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SavedSongsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();

        setContentView(R.layout.activity_saved_songs);
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

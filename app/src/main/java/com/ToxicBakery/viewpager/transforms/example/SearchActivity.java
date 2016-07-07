package com.ToxicBakery.viewpager.transforms.example;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import dgcam.DgCamActivity;

public class SearchActivity extends TabActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);
		TabHost tabHost = getTabHost();

		// Tab for Photos
		TabHost.TabSpec photospec = tabHost.newTabSpec("Photos");
		// setting Title and Icon for the Tab
		photospec.setIndicator("Photos", getResources().getDrawable(R.drawable.video_red_active));
		Intent photosIntent = new Intent(this, DgCamActivity.class);
		photospec.setContent(photosIntent);

		// Tab for Songs
		TabHost.TabSpec songspec = tabHost.newTabSpec("Songs");
		songspec.setIndicator("Songs", getResources().getDrawable(R.drawable.video_red_active));
		Intent songsIntent = new Intent(this, DgCamActivity.class);
		songspec.setContent(songsIntent);

		// Tab for Videos
		TabHost.TabSpec videospec = tabHost.newTabSpec("Videos");
		videospec.setIndicator("Videos", getResources().getDrawable(R.drawable.video_red_active));
		Intent videosIntent = new Intent(this, DgCamActivity.class);
		videospec.setContent(videosIntent);

		// Adding all TabSpec to TabHost
		tabHost.addTab(photospec); // Adding photos tab
		tabHost.addTab(songspec); // Adding songs tab
		tabHost.addTab(videospec); // Adding videos tab
	}
	}


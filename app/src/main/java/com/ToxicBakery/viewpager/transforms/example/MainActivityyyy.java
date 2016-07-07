package com.ToxicBakery.viewpager.transforms.example;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivityyyy extends Activity {
	
	private static final int CAMERA_CAPTURE = 20;
	ArrayList<String> listOfImages;
	HorizontalScrollView Sv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listvie);
		//Sv = (HorizontalScrollView)findViewById(R.id.android_list);
		DisplayImages();
	}




	private void startCapture() {

		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		 if (cameraIntent.resolveActivity(getPackageManager()) != null) {
			 
			 File photoFile = null;
				try {
					 photoFile = CreateImageFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(photoFile != null)
				{
					cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
					startActivityForResult(cameraIntent, CAMERA_CAPTURE);									
				}		        
		    }		
	}
	
	private File CreateImageFile() throws IOException
	{
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "Image_" + timeStamp + "_";
		
		File storageDirectory = getExternalFilesDir("");	
		File image = File.createTempFile(imageFileName, ".jpg",storageDirectory);		
		return image;
		
	}
	
	@Override
	public void onActivityResult(final int requestCode, int resultCode, Intent data) {
	
		switch(requestCode)
		{
		case CAMERA_CAPTURE:
			if(resultCode == RESULT_OK)
			{
				DisplayImages();				
			}
			break;
		}
		
	}
	
	private void DisplayImages() {
		// TODO Auto-generated method stub
		File myPath = getExternalFilesDir(null);
		listOfImages = new ArrayList<String>();
		

		for(File f: myPath.listFiles()) {
			listOfImages.add(f.getAbsolutePath());
		}
        	 			
		MyCustomAdapter adapter = new MyCustomAdapter(MainActivityyyy.this,listOfImages);
			// Sv.se


		
	}
	

	
}

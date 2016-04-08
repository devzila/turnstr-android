package com.ToxicBakery.viewpager.transforms.example;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class MyCustomAdapter extends ArrayAdapter<String> {

	private final Activity context;

	private final ArrayList<String> listOfImages;
	
	public MyCustomAdapter(Activity context, ArrayList<String> listOfImages) {
		super(context, R.layout.mylist, listOfImages);
		// TODO Auto-generated constructor stub
		
		this.context=context;
		this.listOfImages = listOfImages;

	}
	
	public View getView(int position,View view,ViewGroup parent) {
		
		ViewHolder holder;
		
		if(view == null)
		{		
		LayoutInflater inflater=context.getLayoutInflater();
		view =inflater.inflate(R.layout.mylist, null,true);
		
		holder = new ViewHolder();
		holder.imageView = (ImageView) view.findViewById(R.id.selfie);
		holder.txtTitle = (TextView) view.findViewById(R.id.fileName);
		view.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) view.getTag();
		}
		
		
		Bitmap bitmap = BitmapFactory.decodeFile(listOfImages.get(position));		
		File f = new File(listOfImages.get(position));	
		
		holder.txtTitle.setText(f.getName());
		holder.imageView.setImageBitmap(bitmap);

		return view;
		
	};
}

 class ViewHolder {
	 TextView txtTitle;
	 ImageView imageView;
}


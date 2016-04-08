package com.ToxicBakery.viewpager.transforms.example;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;

import java.util.ArrayList;

@SuppressLint("ResourceAsColor")
@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
	TabHost tabHost;
	ImageButton ib;
	private ViewPager mPager;
	String Name,Access_token,device_id;
	private static final ArrayList<TransformerItem> TRANSFORM_CLASSES;
	/** Called when the activity is first created. */
	static {
		TRANSFORM_CLASSES = new ArrayList<>();
//        TRANSFORM_CLASSES.add(new TransformerItem(DefaultTransformer.class));
//        TRANSFORM_CLASSES.add(new TransformerItem(AccordionTransformer.class));
//        TRANSFORM_CLASSES.add(new TransformerItem(BackgroundToForegroundTransformer.class));
//        TRANSFORM_CLASSES.add(new TransformerItem(CubeInTransformer.class))
		TRANSFORM_CLASSES.add(new TransformerItem(CubeOutTransformer.class));
//        TRANSFORM_CLASSES.add(new TransformerItem(DepthPageTransformer.class));
//        TRANSFORM_CLASSES.add(new TransformerItem(FlipHorizontalTransformer.class));
//        TRANSFORM_CLASSES.add(new TransformerItem(FlipVerticalTransformer.class));
//        TRANSFORM_CLASSES.add(new TransformerItem(ForegroundToBackgroundTransformer.class));
//        TRANSFORM_CLASSES.add(new TransformerItem(RotateDownTransformer.class));
//        TRANSFORM_CLASSES.add(new TransformerItem(RotateUpTransformer.class));
//	    TRANSFORM_CLASSES.add(new TransformerItem(ScaleInOutTransformer.class));
//	    TRANSFORM_CLASSES.add(new TransformerItem(StackTransformer.class));
//        TRANSFORM_CLASSES.add(new TransformerItem(TabletTransformer.class));
//        TRANSFORM_CLASSES.add(new TransformerItem(ZoomInTransformer.class));
//        TRANSFORM_CLASSES.add(new TransformerItem(ZoomOutSlideTransformer.class));
//        TRANSFORM_CLASSES.add(new TransformerItem(ZoomOutTranformer.class));
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		Intent intent = getIntent();
		 Name = intent.getStringExtra("Name");
		Access_token= intent.getStringExtra("access_tocken");
		device_id= intent.getStringExtra("Ostype");
		tabHost = getTabHost();
		setTabs();
	}

	private void setTabs() {
		addTab("", R.drawable.home, HomeActivity.class);
		addTab("", R.drawable.search, ListActivity.class);
		addTab("", R.drawable.camerabg, HomeActivity.class);
		addTab("", R.drawable.like, AlarmActivity.class);
		addTab("", R.drawable.profile, SettingActivity.class);

		ib = (ImageButton) findViewById(R.id.ibHome);
		ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(MainActivity.this, "Hello", Toast.LENGTH_LONG)
						.show();
			}
		});
	}

	private void addTab(String labelId, int drawableId, Class<?> c) {
		Intent intent = new Intent(this, c);
		intent.putExtra("Name",Name);
		intent.putExtra("Acess_Token",Access_token);
		intent.putExtra("device_id",device_id);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

		View tabIndicator = LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, getTabWidget(), false);

		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);


	}
	private static final class TransformerItem {

		final String title;
		final Class<? extends ViewPager.PageTransformer> clazz;

		public TransformerItem(Class<? extends ViewPager.PageTransformer> clazz) {
			this.clazz = clazz;
			title = "";
		}

		@Override
		public String toString() {
			return title;
		}

	}
}
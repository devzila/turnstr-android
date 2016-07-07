package adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.example.R;
import com.squareup.picasso.Picasso;

public class ViewPagerAdapterWithView extends PagerAdapter {

    Context activity;
    String imageArray[];

    public ViewPagerAdapterWithView(Context act, String[] items) {
        imageArray = items;
        activity = act;
    }

    public int getCount() {
        return imageArray.length;
    }

    public Object instantiateItem(View collection, int position) {
        ImageView view = new ImageView(activity);
        view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));
        view.setScaleType(ScaleType.FIT_XY);
        Picasso.with(activity).load(imageArray[position]).placeholder(R.drawable.placeholder).into(view);


        //view.setBackgroundResource(imageArray[position]);
        ((ViewPager) collection).addView(view, 0);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "click", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
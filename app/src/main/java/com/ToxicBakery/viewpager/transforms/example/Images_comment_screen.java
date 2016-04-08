package com.ToxicBakery.viewpager.transforms.example;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Images_comment_screen extends Activity {
    private static final ArrayList<TransformerItem> TRANSFORM_CLASSES;
    private PageAdapter mAdapter;
    private ViewPager mPager;
    String images;
    static {
        TRANSFORM_CLASSES = new ArrayList<>();


        TRANSFORM_CLASSES.add(new TransformerItem(CubeOutTransformer.class));
        //TRANSFORM_CLASSES.add(new TransformerItem(CubeInTransformer.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_images_comment_screen);
        mPager = (ViewPager) findViewById(R.id.container);
        Intent intent = getIntent();
         images = intent.getStringExtra("images");
        try {
            mPager.setPageTransformer(true, TRANSFORM_CLASSES.get(0).clazz.newInstance());
       } catch (Exception e) {
           throw new RuntimeException(e);
       }

        mAdapter = new PageAdapter(getFragmentManager());

        mPager.setAdapter(mAdapter);
    }

    public class PlaceholderFragment extends Fragment {

        private static final String EXTRA_POSITION = "EXTRA_POSITION";
        private  final int[] COLORS = new int[]{R.drawable.imgone, R.drawable.imgtwo, R.drawable.imgthree, R.drawable.imgfour};

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final int position = getArguments().getInt(EXTRA_POSITION);
            final ImageView textViewPosition = (ImageView) inflater.inflate(R.layout.fragment_main, container, false);
            String[] items = images.split(",");
           // Picasso.with(Images_comment_screen.this).load(items[position]).placeholder(R.drawable.placeholder).into(textViewPosition);
            Picasso.with(Images_comment_screen.this).load(items[position]).placeholder(R.drawable.placeholder).into(textViewPosition);
            //textViewPosition.setBackgroundResource(COLORS[position - 1]);

            return textViewPosition;
        }

    }

    private static final class TransformerItem {

        final String title;
        final Class<? extends ViewPager.PageTransformer> clazz;

        public TransformerItem(Class<? extends ViewPager.PageTransformer> clazz) {
            this.clazz = clazz;
            title = clazz.getSimpleName();
        }

        @Override
        public String toString() {
            return title;
        }

    }

    private final class PageAdapter extends FragmentStatePagerAdapter {

        public PageAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            final Bundle bundle = new Bundle();
            bundle.putInt(PlaceholderFragment.EXTRA_POSITION, position + 1);

            final PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.setArguments(bundle);

            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

    }

}

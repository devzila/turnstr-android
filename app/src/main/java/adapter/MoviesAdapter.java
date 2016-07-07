package adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.ToxicBakery.viewpager.transforms.example.Images_comment_screen;
import com.ToxicBakery.viewpager.transforms.example.R;


import java.util.ArrayList;
import java.util.HashMap;

import circular_imagview.CircularImageView;
import it.sephiroth.android.library.picasso.Picasso;
import lazyloading.ImageLoader;
import view_pager.viewpager;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    // private List<Movie> moviesList;
    ArrayList<HashMap<String, String>> imagess = new ArrayList<HashMap<String, String>>();
    ArrayList<String> Array_imgadpter = new ArrayList<String>();
    static String List_img;
    static String[] items;
    private static Context contexts;
    ImageLoader img_loader;
    private static final ArrayList<TransformerItem> TRANSFORM_CLASSES;

    static {
        TRANSFORM_CLASSES = new ArrayList<>();


        TRANSFORM_CLASSES.add(new TransformerItem(CubeOutTransformer.class));
        //TRANSFORM_CLASSES.add(new TransformerItem(CubeInTransformer.class));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;
        ImageView imgView, star1, star2, star3, star4;
        TextView Name, Caption, fixed_meal, price, rating, distance, TxtVw_Afterdiscount, Txtvw;
        Button BTN_DETAIL;
        private viewpager mPager;
        Button button2;
        CircularImageView Circular_imagview;


        public MyViewHolder(View view) {
            super(view);
            mPager = (viewpager) view.findViewById(R.id.containerr);
            Name = (TextView) view.findViewById(R.id.textView7);
            Caption = (TextView) view.findViewById(R.id.textView10);
            Circular_imagview = (CircularImageView) view.findViewById(R.id.imageView4);

        }
    }


    public MoviesAdapter(ArrayList<HashMap<String, String>> images, ArrayList<String> Array_img) {
        this.imagess = images;
        this.Array_imgadpter = Array_img;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_home_lv, parent, false);

        contexts = parent.getContext();
        img_loader = new ImageLoader(contexts);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        //  Movie movie = moviesList.get(position);
        Resources mResources;
        holder.Name.setText(imagess.get(position).get("username"));
        holder.Caption.setText(imagess.get(position).get("caption"));
        List_img = Array_imgadpter.get(position);
        PageAdapter mAdapter;
        FragmentManager fm = ((Activity) contexts).getFragmentManager();
        holder.mPager.setId(position + 1);
        mAdapter = new PageAdapter(fm);

        holder.mPager.setAdapter(mAdapter);
        try {
            holder.mPager.setPageTransformer(true, TRANSFORM_CLASSES.get(0).clazz.newInstance());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

            // Set the ImageView image as drawable object
          //  holder.Circular_imagview.setBorderWidth(5);
            //holder.Circular_imagview.
          //  holder.Circular_imagview.setBorderColor(Color.BLUE);
          //  holder.Circular_imagview.setImageDrawable(roundedBitmapDrawable);
            String Str_profile_pic = imagess.get(position).get("profile_picc");
            //img_loader.DisplayImage(imagess.get(position).get("profile_picc"), holder.Circular_imagview);
            Picasso.with(contexts).load(Str_profile_pic).placeholder(R.drawable.profile_placeholder).into(holder.Circular_imagview);

        holder.mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int positionn = holder.mPager.getId();
                positionn = positionn - 1;
//                String postion = String.valueOf(positionn);
                List_img = Array_imgadpter.get(positionn);
                items = List_img.split(",");

            }

            @Override
            public void onPageSelected(int position) {
                int positionn = holder.mPager.getId();
                positionn = positionn - 1;
//                String postion = String.valueOf(positionn);
                List_img = Array_imgadpter.get(positionn);
                items = List_img.split(",");

            }

            @Override
            public void onPageScrollStateChanged(int state) {

                int positionn = holder.mPager.getId();
                positionn = positionn - 1;
//                String postion = String.valueOf(positionn);
                List_img = Array_imgadpter.get(positionn);
                items = List_img.split(",");


            }


        });
        holder.mPager.setOnItemClickListener(new viewpager.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                int id = holder.mPager.getId();
                id = id - 1;
                String postion = String.valueOf(id);
                List_img = Array_imgadpter.get(id);
                String str_id = imagess.get(id).get("Post_id");
                String status_like = imagess.get(id).get("likestatus");
                String caption = imagess.get(id).get("caption");
                //Toast.makeText(mAppContext,"click "+id,Toast.LENGTH_LONG).show();
                Intent lObjIntent = new Intent(contexts, Images_comment_screen.class);
                lObjIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                lObjIntent.putExtra("images", List_img);
                lObjIntent.putExtra("Post_id", str_id);
                lObjIntent.putExtra("status_like", status_like);
                lObjIntent.putExtra("caption", caption);
                contexts.startActivity(lObjIntent);
                //finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagess.size();
    }

    public class PlaceholderFragment extends Fragment {

        private static final String EXTRA_POSITION = "EXTRA_POSITION";
        //private  final int[] COLORS = new int[]{R.drawable.imgone, R.drawable.imgtwo, R.drawable.imgthree, R.drawable.imgfour};

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final int position = getArguments().getInt(EXTRA_POSITION);
            final ImageView textViewPosition = (ImageView) inflater.inflate(R.layout.fragment_main, container, false);

            items = List_img.split(",");
            //
            //ArrayUtils.reverse(items);
//            if(position==4){
//                Collections.reverse(Arrays.asList(items));
//            }
            String url = items[position];

            try {
                //Picasso.with(contexts).load(items[position]).placeholder(R.drawable.placeholderdevzillad).into(textViewPosition);
                //
                img_loader.DisplayImage(url, textViewPosition);


            } catch (OutOfMemoryError error) {

            }


            //textViewPosition.setBackgroundResource(COLORS[position]);
            return textViewPosition;
        }


    }

    private class PageAdapter extends FragmentStatePagerAdapter {

        public PageAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            final Bundle bundle = new Bundle();
            bundle.putInt(PlaceholderFragment.EXTRA_POSITION, position);

            final PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.setArguments(bundle);

            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
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
}

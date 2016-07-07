//package adapter;
//import android.app.Activity;
//import android.app.Fragment;
//import android.app.FragmentManager;
//import android.content.Context;
//import android.os.Bundle;
//import android.support.v13.app.FragmentStatePagerAdapter;
//import android.support.v4.view.ViewPager;
//
//import android.text.Html;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
//import com.ToxicBakery.viewpager.transforms.example.R;
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//
//public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.CustomViewHolder> {
//
//    private Context mContext;
//    ArrayList<HashMap<String, String>> imagess;
//    ArrayList<String> Array_img = new ArrayList<String>();
//    private static final ArrayList<TransformerItem> TRANSFORM_CLASSES;
//    private PageAdapter mAdapter;
//    static String List_img;
//    static {
//        TRANSFORM_CLASSES = new ArrayList<>();
//
//
//        TRANSFORM_CLASSES.add(new TransformerItem(CubeOutTransformer.class));
//        //TRANSFORM_CLASSES.add(new TransformerItem(CubeInTransformer.class));
//    }
//    public MyRecyclerAdapter(Context context, ArrayList<HashMap<String, String>> images,ArrayList<String> Array_img) {
//        this.imagess = images;
//        this.Array_img=Array_img;
//        this.mContext = context;
//    }
//
//    @Override
//    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_home_lv, null);
//
//        CustomViewHolder viewHolder = new CustomViewHolder(view);
//        try {
//            viewHolder.mPager.setPageTransformer(true, TRANSFORM_CLASSES.get(0).clazz.newInstance());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        List_img = Array_img.get(i);
//        FragmentManager fm = ((Activity) mContext).getFragmentManager();
//
//        mAdapter = new PageAdapter(fm);
//
//        viewHolder.mPager.setAdapter(mAdapter);
//        viewHolder.mPager.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext,"efjdf",Toast.LENGTH_LONG).show();
//            }
//        });
//
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
//
//
//        //Download image using picasso library
////        Picasso.with(mContext).load(Array_img.get(i))
////                .error(R.drawable.placeholder)
////                .placeholder(R.drawable.placeholder)
////                .into(customViewHolder.View_pager);
//
//        //Setting text view title
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return (imagess.size());
//    }
//
//    public class CustomViewHolder extends RecyclerView.ViewHolder {
//       // protected ImageView View_pager;
//        protected TextView textView;
//        private ViewPager mPager;
//
//        public CustomViewHolder(View view) {
//            super(view);
//            this.mPager = (ViewPager) view.findViewById(R.id.container);
//
//        }
//    }
//    private static final class TransformerItem {
//
//        final String title;
//        final Class<? extends ViewPager.PageTransformer> clazz;
//
//        public TransformerItem(Class<? extends ViewPager.PageTransformer> clazz) {
//            this.clazz = clazz;
//            title = clazz.getSimpleName();
//        }
//
//        @Override
//        public String toString() {
//            return title;
//        }
//
//    }
//    public class PlaceholderFragment extends Fragment {
//
//        private static final String EXTRA_POSITION = "EXTRA_POSITION";
//        //private  final int[] COLORS = new int[]{R.drawable.imgone, R.drawable.imgtwo, R.drawable.imgthree, R.drawable.imgfour};
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//            final int position = getArguments().getInt(EXTRA_POSITION);
//            final ImageView textViewPosition = (ImageView) inflater.inflate(R.layout.fragment_main, container, false);
//
//            String[] items = List_img.split(",");
//
//            Picasso.with(mContext).load(items[position]).placeholder(R.drawable.loadingh).into(textViewPosition);
//
//
//            //textViewPosition.setBackgroundResource(COLORS[position]);
//            return textViewPosition;
//        }
//
//
//    }
//
//    private class PageAdapter extends FragmentStatePagerAdapter {
//
//        public PageAdapter(FragmentManager fragmentManager) {
//            super(fragmentManager);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            final Bundle bundle = new Bundle();
//            bundle.putInt(PlaceholderFragment.EXTRA_POSITION, position);
//
//            final PlaceholderFragment fragment = new PlaceholderFragment();
//            fragment.setArguments(bundle);
//
//            return fragment;
//        }
//
//        @Override
//        public int getCount() {
//            return 4;
//        }
//
//    }
//}
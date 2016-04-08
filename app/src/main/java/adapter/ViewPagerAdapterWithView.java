//package adapter;
//
//import android.content.Context;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
///**
// * Created by flashok on 9.8.14.
// */
//public class ViewPagerAdapterWithView extends PagerAdapter {
//    private ArrayList<ViewPagerItem> pagerItems;
//    private LayoutInflater inflater;
//    private Context context;
//
//    public ViewPagerAdapterWithView(Context context, ArrayList<ViewPagerItem> pagerItems) {
//        super();
//        this.pagerItems = pagerItems;
//        this.context = context;
//        inflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        LinearLayout layout = null;
//        layout = (LinearLayout) inflater.inflate(R.layout.item_viewpager, null);
//        TextView text = (TextView) layout.findViewById(R.id.idtext);
//        text.setText(pagerItems.get(position).getText());
//        LinearLayout main = (LinearLayout) layout.findViewById(R.id.main);
//        main.setBackgroundColor(context.getResources().getColor(pagerItems.get(position).getColor()));
//        container.addView(layout);
//        return layout;
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        ((ViewPager) container).removeView((View) object);
//    }
//
//    @Override
//    public int getCount() {
//        return pagerItems.size();
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object obj) {
//        return view.equals(obj);
//    }
//}
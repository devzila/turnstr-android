//package adapter;
//
//import android.content.Context;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.view.ViewPager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//
//import com.ToxicBakery.viewpager.transforms.example.R;
//
//import java.util.ArrayList;
//
//public class ListViewAdapter extends ArrayAdapter<ListViewItem> {
//    private ArrayList<ListViewItem> mItems;
//    private Context mContext;
//    private LayoutInflater inflater;
//
//
//    public ListViewAdapter(FragmentActivity fragmentActivity, ArrayList<ListViewItem> items) {
//        super(fragmentActivity, 0, items);
//        mContext = fragmentActivity;
//        mItems = items;
//        inflater = fragmentActivity.getLayoutInflater();
//    }
//
//    @Override
//    public int getCount() {
//        return mItems.size();
//    }
//
//    @Override
//    public ListViewItem getItem(int position) {
//        return mItems.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        Holder viewHolder;
//        if (convertView == null) {
//            convertView = inflater.inflate(R.layout.item_listview, null);
//            viewHolder = new Holder();
//            viewHolder.viewPager = (ViewPager) convertView.findViewById(R.id.pager);
//            convertView.setTag(viewHolder);
//
//
//        } else {
//            viewHolder = (Holder) convertView.getTag();
//        }
//
//        ViewPager viewPager = viewHolder.viewPager;
//        ViewPagerAdapterWithView tempMyFriendPagerAdapter = new ViewPagerAdapterWithView(mContext, mItems.get(position).getViewPagerItems());
//        viewPager.setAdapter(tempMyFriendPagerAdapter);
//        return convertView;
//    }
//
//    class Holder {
//        ViewPager viewPager;
//    }
//}
//
//

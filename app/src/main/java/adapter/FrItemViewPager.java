//package adapter;
//
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//
//public class FrItemViewPager extends Fragment {
//    private static final String ID = "id";
//    private static final String COLOR = "color";
//    private int mColor;
//    private String mText;
//
//    public static FrItemViewPager newInstance(ViewPagerItem viewPagerItem) {
//        FrItemViewPager frItemViewPager = new FrItemViewPager();
//        Bundle bundle = new Bundle();
//        bundle.putString(ID, viewPagerItem.getText());
//        bundle.putInt(COLOR, viewPagerItem.getColor());
//        frItemViewPager.setArguments(bundle);
//        return frItemViewPager;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mText = getArguments().getString(ID);
//        mColor = getArguments().getInt(COLOR);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.item_viewpager, container, false);
//        ((TextView) view.findViewById(R.id.idtext)).setText(mText);
//        ((LinearLayout) view.findViewById(R.id.main)).setBackgroundColor(getResources().getColor(mColor));
//        return view;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//}

//package adapter;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.view.animation.Animation.AnimationListener;
//import android.widget.ListView;
//import android.widget.ViewFlipper;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Random;
//
//public class MainActivity extends FragmentActivity {
//    private HashMap<Integer, Integer> mapColors = new HashMap<Integer, Integer>();
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//
//        Random r = new Random();
//
//        mapColors.put(0, R.color.blue);
//        mapColors.put(1, R.color.purple);
//        mapColors.put(2, R.color.green);
//        mapColors.put(3, R.color.orange);
//        mapColors.put(4, R.color.red);
//        mapColors.put(5, R.color.darkblue);
//        mapColors.put(6, R.color.darkpurple);
//        mapColors.put(7, R.color.darkgreen);
//        mapColors.put(8, R.color.darkorange);
//        mapColors.put(9, R.color.darkred);
//
//        ArrayList<ListViewItem> array = new ArrayList<ListViewItem>();
//        for (int i = 0; i < 20; i++) {
//            ArrayList<ViewPagerItem> viewPagerItems = new ArrayList<ViewPagerItem>();
//            for (int j = 0; j < 5; j++) {
//                viewPagerItems.add(new ViewPagerItem(Integer.toString(j), mapColors.get(r.nextInt(10))));
//            }
//            array.add(new ListViewItem(Integer.toString(i), viewPagerItems));
//        }
//        ListView listView = (ListView) findViewById(R.id.listView);
//        listView.setAdapter(new ListViewAdapter(this, array));
//    }
//}
//
//

package ENDLESS_PAGER_ADAPTER;

/**
 * Created by Nirmal on 7/7/2016.
 */
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class EndLessAdapter extends PagerAdapter {

    FragmentActivity activity;
    int imageArray[];

    public EndLessAdapter(FragmentActivity act, int[] imgArra) {
        imageArray = imgArra;
        activity = act;
    }

    public int getCount() {
        return Integer.MAX_VALUE;
    }

    private int pos = 0;

    public Object instantiateItem(View collection, int position) {

        ImageView mwebView = new ImageView(activity);
        ((ViewPager) collection).addView(mwebView, 0);
        mwebView.setScaleType(ScaleType.FIT_XY);
        mwebView.setImageResource(imageArray[pos]);

        if (pos >= imageArray.length - 1)
            pos = 0;
        else
            ++pos;

        return mwebView;
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
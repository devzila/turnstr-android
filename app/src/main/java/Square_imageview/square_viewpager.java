package Square_imageview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by Nirmal on 4/19/2016.
 */
public class square_viewpager extends ViewPager {

    public square_viewpager(final Context context) {
        super(context);
    }

    public square_viewpager(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    @Override protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int width = getDefaultSize(getSuggestedMinimumWidth(),widthMeasureSpec);
        setMeasuredDimension(width, width);
    }

    @Override protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        super.onSizeChanged(w, w, oldw, oldh);
    }
}
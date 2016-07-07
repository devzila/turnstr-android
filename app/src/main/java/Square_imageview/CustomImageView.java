package Square_imageview;

/**
 * Created by Nirmal on 6/1/2016.
 */
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class CustomImageView extends android.widget.ImageView {

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void fitYUniformly() {
        final Drawable drawable = getDrawable();
        if (drawable == null) return;

        final int dwidth = drawable.getIntrinsicWidth();
        final int dheight = drawable.getIntrinsicHeight();
        if (dwidth == -1 || dheight == -1) return;

        int vheight = this.getHeight();
        float scale = (float) vheight / (float) dheight;

        final int vwidth = (int) (dwidth * scale);
        scale(scale, vwidth, vheight);
    }

    public void fitXUniformly(int parentWidth) {
        final Drawable drawable = getDrawable();
        if (drawable == null) return;

        final int dwidth = drawable.getIntrinsicWidth();
        final int dheight = drawable.getIntrinsicHeight();
        if (dwidth == -1 || dheight == -1) return;

        int vwidth = parentWidth;// here,you need to pass the width of parentview
        //  int vwidth = this.getWidth();
        float scale = (float) vwidth / (float) dwidth;

        final int vheight = (int) (dheight * scale);
        scale(scale, vwidth, vheight);
    }

    private void scale(float scale, int newWidth, int newHeight) {
        final ViewGroup.LayoutParams params = this.getLayoutParams();
        params.width = newWidth;
        params.height = newHeight;
        this.setLayoutParams(params);
        this.setScaleType(ScaleType.MATRIX);
        final Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        this.setImageMatrix(matrix);
    }

}
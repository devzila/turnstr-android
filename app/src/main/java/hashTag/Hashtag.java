package hashTag;

/**
 * Created by Nirmal on 6/7/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.example.Hashtag_Search_Activity;
import com.ToxicBakery.viewpager.transforms.example.Other_user_profile;

import Fragments.home_fragment;

public class Hashtag extends ClickableSpan{
    Context context;
    TextPaint textPaint;
    public Hashtag(Context ctx) {
        super();
        context = ctx;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        textPaint = ds;
        ds.setColor(ds.linkColor);
        ds.setARGB(255, 30, 144, 255);
    }

    @Override
    public void onClick(View widget) {
        TextView tv = (TextView) widget;
        Spanned s = (Spanned) tv.getText();
        int start = s.getSpanStart(this);
        int end = s.getSpanEnd(this);
        String theWord = s.subSequence(start + 1, end).toString();


        Intent i1new = new Intent(context, Hashtag_Search_Activity.class);
        i1new.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i1new.putExtra("User_detail", theWord);
        context.startActivity(i1new);

        // you can start another activity here
       // Toast.makeText(context, String.format("Tag : %s", theWord), Toast.LENGTH_LONG ).show();

    }

}

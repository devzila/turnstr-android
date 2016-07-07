package hashTag;

/**
 * Created by Nirmal on 6/7/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.example.Other_user_profile;

public class CalloutLink extends ClickableSpan {
    Context context;
    public CalloutLink(Context ctx) {
        super();
        context = ctx;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setARGB(255, 51, 51, 51);
        ds.setColor(Color.RED);

    }

    @Override
    public void onClick(View widget) {
        TextView tv = (TextView) widget;
        Spanned s = (Spanned) tv.getText();
        int start = s.getSpanStart(this);
        int end = s.getSpanEnd(this);
        String theWord = s.subSequence(start + 1, end).toString();

        Intent i1new = new Intent(context, Other_user_profile.class);
        i1new.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i1new.putExtra("User_detail", "@"+theWord);
        context.startActivity(i1new);

        // you can start another activity here
       // Toast.makeText(context, String.format("@"+theWord, "@"+theWord), Toast.LENGTH_LONG).show();
    }
}

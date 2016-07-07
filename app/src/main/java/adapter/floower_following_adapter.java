package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.example.R;

import java.util.ArrayList;
import java.util.HashMap;

import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by Nirmal on 6/24/2016.
 */
public class floower_following_adapter extends BaseAdapter {

    ArrayList<HashMap<String, String>> Array_list_Follower_following = new ArrayList<HashMap<String, String>>();
    LayoutInflater inflater;
    Context context;


    public floower_following_adapter(Context context, ArrayList<HashMap<String, String>> Array_list_Follower_followinglist) {
        this.Array_list_Follower_following = Array_list_Follower_followinglist;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return Array_list_Follower_following.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_lv_layout, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
        try {
            //if(Str_profile_image!=null) {
            //img_loader.DisplayImage(fl,vh.Img_profilepic);
            Picasso.with(context).load(Array_list_Follower_following.get(position).get("profile_pic")).placeholder(R.drawable.placeholderdevzillad).into(mViewHolder.Img_profilepic);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();

        }

        mViewHolder.Name.setText((Array_list_Follower_following.get(position).get("name")));
        return convertView;
    }

    private class MyViewHolder {
        TextView Name;
        ImageView Img_profilepic;

        public MyViewHolder(View item) {
            Name = (TextView) item.findViewById(R.id.textView36);
//            tvDesc = (TextView) item.findViewById(R.id.tvDesc);
            Img_profilepic = (ImageView) item.findViewById(R.id.imageView4);
        }
    }
}
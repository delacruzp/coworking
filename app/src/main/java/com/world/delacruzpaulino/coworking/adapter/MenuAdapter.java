package com.world.delacruzpaulino.coworking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.world.delacruzpaulino.coworking.R;

/**
 * Created by delacruzpaulino on 5/14/16.
 */
public class MenuAdapter extends BaseAdapter {
    private Context mContext;

    public MenuAdapter(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public String getItem(int position) {
        return categories[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        ImageView imageView;
        View view = null;
        if (convertView == null) {

            view = LayoutInflater.from(mContext).inflate(R.layout.item_category, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);

        } else {
            view = convertView;
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.imageName.setImageResource(images[position]);
        viewHolder.textName.setText(categories[position]);
//        imageView.setImageResource(images[position]);
        return view;
    }

    // Temorary Hard Code for the images
    private Integer[] images = {
            R.drawable.library, R.drawable.library2,
            R.drawable.university, R.drawable.university2
    };

    private String[] categories = {
            "Library", "University", "Mall", "Others"
    };

    public static class ViewHolder {
        public final ImageView imageName;
        public final TextView textName;

        public ViewHolder(View view) {
            imageName = (ImageView) view.findViewById(R.id.image_category);
            textName = (TextView) view.findViewById(R.id.text_name);
        }
    }
}

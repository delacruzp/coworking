package com.world.delacruzpaulino.coworking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.world.delacruzpaulino.coworking.R;
import com.world.delacruzpaulino.coworking.dal.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by delacruzpaulino on 5/14/16.
 */
public class ItemAdapter extends BaseAdapter {
    private Context mContext;

    public ItemAdapter(Context c) {
        mContext = c;
        items = new ArrayList<Item>();
    }

    // Temorary Hard Code for the images
    public ArrayList<Item> items;

    public String getItemKey(int position) {
        return items.get(position).getKey();
    }

    public void setItemByKey(String key, Item mItem) {
        for (int i =0; i < items.size(); i++){
            if (items.get(i).getKey().equals(key)) {
                items.set(i, mItem);
                break;
            }
        }
        this.notifyDataSetChanged();
//        return items.get(position).getKey();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {

            view = LayoutInflater.from(mContext).inflate(R.layout.item_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);

        } else {
            view = convertView;
        }

        Item item = items.get(position);

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String imageUri = "empty";
        if (item.getImages().length > 0){
            imageUri = item.getImages()[0];
        }

        Picasso.with(mContext).load(imageUri).resize(100, 100).placeholder(R.drawable.library).into(viewHolder.imageName);
//        viewHolder.imageName.setImageResource(R.drawable.library);

        viewHolder.textName.setText(item.getName());
        viewHolder.ratingBar.setRating(item.getRating());
        viewHolder.textSchedule.setText(item.getSchedule());
        viewHolder.textPrice.setText(item.getPrice().toString());
//        imageView.setImageResource(images[position]);
        return view;
    }


    public static class ViewHolder {
        public final ImageView imageName;
        public final TextView textName;
        public final RatingBar ratingBar;
        public final TextView textSchedule;
        public final TextView textPrice;

        public ViewHolder(View view) {
            imageName = (ImageView) view.findViewById(R.id.img_item);
            textName = (TextView) view.findViewById(R.id.text_name);
            ratingBar = (RatingBar) view.findViewById(R.id.rating_bar);
            textSchedule = (TextView) view.findViewById(R.id.text_schedule);
            textPrice = (TextView) view.findViewById(R.id.text_price);
        }
    }
}

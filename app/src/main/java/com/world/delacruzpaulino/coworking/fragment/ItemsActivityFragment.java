package com.world.delacruzpaulino.coworking.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.world.delacruzpaulino.coworking.adapter.ItemAdapter;
import com.world.delacruzpaulino.coworking.R;
import com.world.delacruzpaulino.coworking.activity.ItemActivity;
import com.world.delacruzpaulino.coworking.dal.Item;
import com.world.delacruzpaulino.coworking.dal.ItemReceiver;

/**
 * A placeholder fragment containing a simple view.
 */
public class ItemsActivityFragment extends Fragment implements ItemReceiver {

    ListView list;
    ItemAdapter adapter;
    String category;

    public ItemsActivityFragment() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View x = inflater.inflate(R.layout.fragment_items, container, false);
        ///List

        list = (ListView) x.findViewById(R.id.listView);
        adapter = new ItemAdapter(getActivity());
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(getActivity(), ItemActivity.class);
                intent.putExtra(getString(R.string.ITEMID), adapter.getItemKey(position));
                startActivity(intent);
            }
        });

        //Start the mambo
        Bundle extras = getActivity().getIntent().getExtras();
        category = extras.getString(getString(R.string.CATEGORY));
        new Item().Get(this, category);
        return x;

    }

    @Override
    public void UpdatedItem(Item item) {
//        adapter.getItemPositionByKey();
        adapter.setItemByKey(item.getKey(),item);
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void NewItem(Item item) {
        adapter.items.add(item);
        adapter.notifyDataSetChanged();
    }

}

package com.world.delacruzpaulino.coworking.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLngBounds;
import com.world.delacruzpaulino.coworking.R;
import com.world.delacruzpaulino.coworking.components.TimePickerFragment;
import com.world.delacruzpaulino.coworking.dal.Item;
import com.world.delacruzpaulino.coworking.dal.Schedule;
import com.world.delacruzpaulino.coworking.util.CONSTANT;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewItemActivity extends AppCompatActivity {
    Item item;
    //    mButton = (Button)findViewById(R.id.button);
    EditText placeText;
    EditText addressText;
    EditText priceText;

    Button weekdaysFromButton;
    Button weekdaysToButton;
    Button weekendFromButton;
    Button weekendToButton;

    CheckBox wifiBox;
    CheckBox acBox;
    CheckBox talkingBox;
    CheckBox foodBox;

    Button saveButton;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        Bundle extras = getIntent().getExtras();
        category = extras.getString(getString(R.string.CATEGORY));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        placeText = (EditText) findViewById(R.id.placeText);
        addressText = (EditText) findViewById(R.id.addressText);
        priceText = (EditText) findViewById(R.id.priceText);
        weekdaysFromButton = (Button) findViewById(R.id.weekdaysFromButton);
        weekdaysToButton = (Button) findViewById(R.id.weekdaysToButton);
        weekendFromButton = (Button) findViewById(R.id.weekendFromButton);
        weekendToButton = (Button) findViewById(R.id.weekendToButton);

        wifiBox = (CheckBox) findViewById(R.id.wifiBox);
        acBox = (CheckBox) findViewById(R.id.acBox);
        talkingBox = (CheckBox) findViewById(R.id.talkingBox);
        foodBox = (CheckBox) findViewById(R.id.foodBox);

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(v);
            }
        });

        item = new Item();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        };
//        weekdaysFromB.setOnClickListener(listener);
//        weekdaysToText.setOnClickListener(listener);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    public void showTimePickerDialog(View v) {
        DialogFragment fragment = new TimePickerFragment();

        Bundle bundle = new Bundle(1);
        bundle.putInt("VIEW_ID", v.getId());
        bundle.putString("CURRENT_VALUE", ((Button) v).getText().toString());
        fragment.setArguments(bundle);

        fragment.show(getFragmentManager(), "timePicker");
    }

    //Seleccionador de sitios
    public void showPlacesPickerDialog(View v) {
        // Construct an intent for the place picker
        try {
            PlacePicker.IntentBuilder intentBuilder;

            if (item.getLocation() == null) {
                intentBuilder = new PlacePicker.IntentBuilder();
            } else {
                intentBuilder = new PlacePicker.IntentBuilder();
                LatLngBounds bounds=new LatLngBounds(item.getLocation().getLatLng(),item.getLocation().getLatLng());
                intentBuilder.setLatLngBounds(bounds);
            }

            Intent intent = intentBuilder.build(this);
            // Start the intent by requesting a result,
            // identified by a request code.
            startActivityForResult(intent, CONSTANT.REQUEST_PLACE_PICKER);

        } catch (GooglePlayServicesRepairableException e) {
            // ...
            Log.e("G",e.toString());
        } catch (GooglePlayServicesNotAvailableException e) {
            // ...
            Log.e("G",e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if (requestCode == CONSTANT.REQUEST_PLACE_PICKER
                && resultCode == Activity.RESULT_OK) {

            // The user has selected a place. Extract the name and address.
            final Place place = PlacePicker.getPlace(this,data);

            final CharSequence name = place.getName();
            placeText.setText(name.toString());
            addressText.setText(place.getAddress().toString());


            item.setLocation(place.getLatLng());
//            String attributions = PlacePicker.getAttributions(data);
//            if (attributions == null) {
//                attributions = "";
//            }
//
//            mViewName.setText(name);
//            mViewAddress.setText(address);
//            mViewAttributions.setText(Html.fromHtml(attributions));

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public Date getTime(int viewId) {
        Button view = (Button) findViewById(viewId);
        String[] texts = view.getText().toString().split(":");
        return new SimpleDateFormat("hh:mm a").parse(view.getText().toString(), new ParsePosition(0));
    }

    public void save(View v) {
        String place = placeText.getText().toString();

        if (!place.isEmpty()) {
            Double price = Double.parseDouble(priceText.getText().toString());

//            item = new Item(place, addressText.getText().toString(), null, category, null, price, null, acBox.isChecked(), wifiBox.isChecked(),
//                    talkingBox.isChecked(), foodBox.isChecked());

            item.setName(place);
            item.setAddress(addressText.getText().toString());
            item.setCategory(category);
            item.setPrice(price);
            item.setAc(acBox.isChecked());
            item.setWifi(wifiBox.isChecked());
            item.setTalking(talkingBox.isChecked());
            item.setFood(foodBox.isChecked());

            Schedule schedule1 = new Schedule(0, getTime(R.id.weekdaysFromButton), getTime(R.id.weekdaysToButton));
            Schedule schedule2 = new Schedule(0, getTime(R.id.weekendFromButton), getTime(R.id.weekendToButton));

            if (schedule1.getFrom() != null || schedule1.getTo() != null)
                item.addSchedule(schedule1);

            if (schedule2.getFrom() != null || schedule2.getTo() != null)
                item.addSchedule(schedule2);

//            schedule.se
//            weekdaysFromText = (Tex)findViewById();
//            weekdaysToText = (EditText)findViewById(R.id.weekdaysToText);
//            weekendFromText = (EditText)findViewById(R.id.weekendFromText);
//            weekendToText = (EditText)findViewById(R.id.weekendToText);

            item.Save();
        }
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

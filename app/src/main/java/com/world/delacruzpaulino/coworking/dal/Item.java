package com.world.delacruzpaulino.coworking.dal;

import android.location.Location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.world.delacruzpaulino.coworking.R;

import java.util.ArrayList;

/**
 * Created by delacruzpaulino on 5/14/16.
 */
public class Item {

    String key;

    String name;

    String address;

    LatLngSx location;

    String category;
    String[] categories;

    Double price;

    ArrayList<Schedule> schedules;
    Boolean ac;

    Boolean wifi;

    Boolean talking;

    Boolean food;

    ArrayList<Review> reviews;

    ArrayList<String> images;

    public class S extends Firebase {
        public S(String url) {

            super(url);
        }
    }

    private Firebase ref = new Firebase("https://coworkingrd.firebaseio.com/item");

    public Item()
    {

    }
    public Item(String name, String address,  String category, String[] categories, Double price, ArrayList<Schedule> schedules, Boolean ac, Boolean wifi, Boolean talking, Boolean food) {
        this.name = name;
        this.address = address;
        this.category = category;
        this.categories = categories;
        this.price = price;
        this.schedules = schedules;
        this.ac = ac;
        this.wifi = wifi;
        this.talking = talking;
        this.food = food;
    }

    public void Save(){
        if (this.key == null){
            ref.push().setValue(this);
        } else {
            ref.child(this.getKey()).setValue(this);
        }

    }

    public void Get(final ItemReceiver itemReceiver, String category) {
        Query queryRef = new Firebase("https://coworkingrd.firebaseio.com").child("item").orderByChild("category").equalTo(category);
//        queryRef.equalTo(category, "category");
//        queryRef;

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Item item = snapshot.getValue(Item.class);
                item.key = snapshot.getKey();
                itemReceiver.NewItem(item);
//                int x =0;
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String s) {
                Item item = snapshot.getValue(Item.class);
                item.key = snapshot.getKey();
//                itemReceiver.NewItem(item);
                itemReceiver.UpdatedItem(item);
//                int x = 0;
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                firebaseError.toException().printStackTrace();
                int x = 0;

            }
        });
    }

    public void GetByKey(String key, final ItemReceiver itemReceiver) {
        Query queryRef = ref.child(key);

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Item item = snapshot.getValue(Item.class);
                item.key = snapshot.getKey();
                itemReceiver.NewItem(item);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
    }



    public Schedule[] getSchedules() {
        return schedules.toArray(new Schedule[schedules.size()]);
    }

    @JsonIgnore
    public String getSchedule() {
        String scheduleDescription = "";
        for (Schedule schedule:schedules
             ) {
            scheduleDescription += schedule.toString() + " ";
        }
        return scheduleDescription;
    }
    public void addSchedule(Schedule schedules) {
        if (this.schedules == null){
            this.schedules = new ArrayList<Schedule>();
        }
        this.schedules.add(schedules);
    }

    @JsonIgnore
    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLngSx getLocation() {
        return location;
    }

    public void setLocation(LatLngSx location) {
        this.location = location;
    }

    @JsonIgnore
    public void setLocation(LatLng location) {
        this.location = LatLngSx.creator(location);
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getAc() {
        return ac;
    }

    public void setAc(Boolean ac) {
        this.ac = ac;
    }

    public Boolean getWifi() {
        return wifi;
    }

    public void setWifi(Boolean wifi) {
        this.wifi = wifi;
    }

    public Boolean getTalking() {
        return talking;
    }

    public void setTalking(Boolean talking) {
        this.talking = talking;
    }

    public Boolean getFood() {
        return food;
    }

    public void setFood(Boolean food) {
        this.food = food;
    }


    public String[] getImages() {
        if (images == null) {
            return new String[0];
        }

        return images.toArray(new String[images.size()]);
    }

    public void addImage(String image) {
        if (this.images == null){
            this.images = new ArrayList<String>();
        }
        this.images.add(image);
    }

    @JsonIgnore
    public int getUserReviewId(String userId) {
        if (this.reviews != null) {
            for(int x = 0; x < this.reviews.size(); x++){
                String pUserId = reviews.get(x).getUserId();
                int z = userId.length();
                int y = pUserId.length();
                if (android.text.TextUtils.equals(pUserId, userId)) {
//                if (pUserId.equals(userId)) {
                    return x;
                }

                for(int i =0; i < pUserId.length();i++){
                    if(pUserId.charAt(i) == userId.charAt(i) ){
                        int w = 0;

                    } else
                    {
                        break;
                    }
                }
            }
        }

        return -1;
    }

    @JsonIgnore
    public Review getUserReview(String userId) {
        int id = getUserReviewId(userId);

        if (id < 0) {
            return new Review(0,null,userId);
        } else {
            return this.reviews.get(id);
        }
    }

    @JsonIgnore
    public void setReview(Review review) {
        int id = getUserReviewId(review.getUserId());

        if (id < 0) {
            if (this.reviews == null){
                this.reviews = new ArrayList<Review>();
            }
            this.reviews.add(review);
        } else {
            this.reviews.set(id,review);
        }
    }

    @JsonIgnore
    public float getRating() {
        float rating = 0;

        if (reviews != null && reviews.size() > 0 ) {
            for (Review review :reviews) {
//            for(int x = 0; x < this.reviews.size(); x++){
//                Review xx = this.reviews.get(x);
                rating += review.getStars();
            }
            rating = rating/reviews.size();
        }
        return rating;
    }

//    public void setRating(Double rating) {
//        this.rating = rating;
//    }

}


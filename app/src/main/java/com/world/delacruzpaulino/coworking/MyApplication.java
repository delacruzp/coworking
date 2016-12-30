package com.world.delacruzpaulino.coworking;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by delacruzpaulino on 5/22/16.
 */
public class MyApplication extends Application {
    public MyApplication(){
        super();
        Firebase.setAndroidContext(this);
//        FirebaseStorage storage = FirebaseStorage.getInstance();


    }
}

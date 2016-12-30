package com.world.delacruzpaulino.coworking.components;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.world.delacruzpaulino.coworking.util.CONSTANT;

/**
 * Created by delacruzpaulino on 5/22/16.
 */
public class ImageUploader {
    StorageReference storageRef;

    public ImageUploader(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl(CONSTANT.STORAGE_URL);
        storageRef.child("images");
    }

    public UploadTask fromFile(Uri file){
        StorageReference riversRef = storageRef.child(file.getLastPathSegment());
        return riversRef.putFile(file);

    }
}

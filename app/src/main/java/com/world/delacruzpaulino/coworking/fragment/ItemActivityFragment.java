package com.world.delacruzpaulino.coworking.fragment;

import android.provider.Settings.Secure;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.world.delacruzpaulino.coworking.R;
import com.world.delacruzpaulino.coworking.activity.ItemActivity;
import com.world.delacruzpaulino.coworking.components.Camera;
import com.world.delacruzpaulino.coworking.components.ImageUploader;
import com.world.delacruzpaulino.coworking.dal.Item;
import com.world.delacruzpaulino.coworking.dal.ItemReceiver;
import com.world.delacruzpaulino.coworking.dal.Review;
import com.world.delacruzpaulino.coworking.util.CONSTANT;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import java.util.concurrent.Executor;

/**
 * A placeholder fragment containing a simple view.
 */
public class ItemActivityFragment extends Fragment implements ItemReceiver, GoogleApiClient.OnConnectionFailedListener {
    String TAG = "S";
    int nextStep = 0;
    private final int PICTURE = 1;
    private final int RATING = 2;

    private String android_id;

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    Item item;
    Review review;


    LinearLayout llPicture;
    ImageView imgNewPicture;

    private Uri fileUri;
    private Uri downloadUri;
    TextView nameText;
    TextView addressText;
    TextView scheduleText;
    TextView priceText;

    TextView txtWifi;
    TextView txtAc;
    TextView txtFood;
    TextView txtTalking;

    RatingBar ratingBar;
    RatingBar ratingBarEdit;

    public ItemActivityFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mAuth = FirebaseAuth.getInstance();

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
// options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */,this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        android_id = Secure.getString(getContext().getContentResolver(),
                Secure.ANDROID_ID);

        View view = inflater.inflate(R.layout.fragment_item, container, false);

        llPicture = (LinearLayout) view.findViewById(R.id.ll_picture);

        nameText = (TextView)view.findViewById(R.id.nameText);
        addressText = (TextView)view.findViewById(R.id.addressText);
        priceText = (TextView)view.findViewById(R.id.priceText);
        scheduleText = (TextView)view.findViewById(R.id.scheduleText);

        txtAc = (TextView)view.findViewById(R.id.txt_ac);
        txtFood= (TextView)view.findViewById(R.id.txt_food);
        txtTalking = (TextView)view.findViewById(R.id.txt_talking);
        txtWifi = (TextView)view.findViewById(R.id.txt_wifi);

        ratingBar = (RatingBar) view.findViewById(R.id.rating_bar);
        ratingBarEdit = (RatingBar)view.findViewById(R.id.rating_bar_edit);

        llPicture = (LinearLayout) view.findViewById(R.id.ll_picture);
        imgNewPicture = (ImageView) view.findViewById(R.id.img_new_picture);

        imgNewPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
                nextStep = PICTURE;
//                addImage(v);
            }
        });

        ratingBarEdit.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(fromUser) {
                    signIn();
                    nextStep = RATING;
//                    review.setStars((int)(rating));
//                    item.setReview(review);
//                    item.Save();
                }

            }
        });
//        wifiBox = (CheckBox)view.findViewById(R.id.wifiBox);
//        acBox = (CheckBox)view.findViewById(R.id.acBox);
//        talkingBox = (CheckBox)view.findViewById(R.id.talkingBox);
//        foodBox = (CheckBox)view.findViewById(R.id.foodBox);

        Bundle extras = getActivity().getIntent().getExtras();

        if (extras != null) {
            new Item().GetByKey(extras.getString(getString(R.string.ITEMID)),this);
        }

        return  view;
    }

    @Override
    public void NewItem(Item item) {
        this.item = item;

        for (String image:item.getImages()){
            pushImage(image);
        }

        ratingBar.setRating(item.getRating());
        nameText.setText(item.getName());
        addressText.setText(item.getAddress());
        scheduleText.setText(item.getSchedule());
        priceText.setText(item.getPrice().toString());

        review = item.getUserReview(android_id);

        ratingBarEdit.setRating(review.getStars());

        if (!item.getAc()){
            txtAc.setVisibility(View.GONE);
        }
        if (!item.getFood()){
            txtFood.setVisibility(View.GONE);
        }
        if (!item.getTalking()){
            txtTalking.setVisibility(View.GONE);
        }
        if (!item.getWifi()){
            txtWifi.setVisibility(View.GONE);
        }

    }

    @Override
    public void UpdatedItem(Item item) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_item, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_go:
                Toast.makeText(getContext(), "FRAGMENT MENU", Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // create Intent to take a picture and return control to the calling application

        fileUri = Camera.getOutputMediaFileUri(Camera.MEDIA_TYPE_IMAGE); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        startActivityForResult(intent, CONSTANT.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE); // start the image capture Intent
    }

    public void addImage(Uri uri) {
        new ImageUploader().fromFile(uri)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),"Image could not be uploaded",Toast.LENGTH_LONG);
                        e.printStackTrace();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                String downloadHttp = taskSnapshot.getDownloadUrl().toString();

                item.addImage(downloadHttp);
                pushImage(downloadHttp);
                item.Save();
            }
        });
    }
//    public void pushImage(String uri) {
//        pushImage(Uri.parse(uri));
//    }

    public void pushImage(String uri) {
        float density = getResources().getDisplayMetrics().density;
        int dimenssion = (int)(90*density+0.5f);
        ImageView image = new ImageView(getActivity());
//        image.setImageURI(uri);
        image.setLayoutParams(new LinearLayout.LayoutParams(dimenssion,dimenssion));
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Picasso.with(getActivity()).load(uri).resize(90, 90).into(image);

        llPicture.addView(image,0);
    }

    public void pushImage(Uri uri) {
        float density = getResources().getDisplayMetrics().density;
        int dimenssion = (int)(90*density+0.5f);
        ImageView image = new ImageView(getActivity());
        image.setImageURI(uri);
        image.setLayoutParams(new LinearLayout.LayoutParams(dimenssion,dimenssion));
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        Picasso.with(getActivity()).load(uri).into(image);

        llPicture.addView(image,0);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, CONSTANT.RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("SIGN IN", "handleSignInResult:" + result.isSuccess() + result.getStatus() + "-" + result.toString());

        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogle(account);

            // Signed in successfully, show authenticated UI.
//            GoogleSignInAccount acct = result.getSignInAccount();
            android_id = account.getId();

        } else {
            // Signed out, show unauthenticated UI.
//            updateUI(false);
            Toast.makeText(getActivity(),"Sign In failed.",Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == CONSTANT.RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else if (requestCode == CONSTANT.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                pushImage(fileUri);
                addImage(fileUri);


            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
                Toast.makeText(getActivity(), "Image could not be saved.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getActivity(),"Connection failed.",Toast.LENGTH_LONG);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",Toast.LENGTH_SHORT).show();
                        } else {
                            signedSuccesfully();
                        }

                    }
                });
    }

    public void signedSuccesfully (){
        switch (nextStep) {
            case 1:
                addImage();
            case 2:
                review.setStars((int)(ratingBarEdit.getRating()));
                ratingBar.setRating(item.getRating());
                item.setReview(review);
                item.Save();
        }
    }
}

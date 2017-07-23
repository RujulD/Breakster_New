package com.example.rujul.breakster;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.TimedText;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by Rujul
 */

public class MyUploadService extends AppCompatActivity {

    Button select_image,upload_button,View;
    ImageView user_image;
    EditText title;
    public static final int READ_EXTERNAL_STORAGE = 0;
    private static final int GALLERY_INTENT = 2;
    private ProgressDialog mProgressDialog;
    private Firebase mRoofRef;
    private FirebaseAuth mAuth;
    private Uri mImageUri = null;
    private DatabaseReference mdatabaseRef;
    private StorageReference mStorage;
    TextView info;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.upload_layout);

        Firebase.setAndroidContext(this);


        mAuth = FirebaseAuth.getInstance();
        select_image = (Button)findViewById(R.id.select_image);
        upload_button = (Button)findViewById(R.id.upload_bttn);
        user_image = (ImageView) findViewById(R.id.user_image);
        title = (EditText) findViewById(R.id.etTitle);
        View = (Button)findViewById(R.id.View);
        info = (TextView)findViewById( R.id.textView11 );
        //Initialize the Progress Bar
        mProgressDialog = new ProgressDialog(MyUploadService.this);
info.setVisibility( android.view.View.GONE );
info.setOnClickListener( new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                 AlertDialog.Builder builder = new AlertDialog.Builder((MyUploadService.this));
                                 builder.setMessage("The Categories Should Be: Our Specials/Samosa/Namkeens/Rolls/Toasts/Vadas/Sandwiches/Burgers/Wraps").setCancelable(false)
                                         .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                             @Override
                                             public void onClick(DialogInterface dialog, int which) {
                                                 dialog.cancel();
                                             }
                                         })
                                         .setNegativeButton("", new DialogInterface.OnClickListener() {
                                             @Override
                                             public void onClick(DialogInterface dialog, int which) {
                                                 dialog.cancel();
                                             }
                                         });
                                 AlertDialog dialog = builder.create();
                                 dialog.setTitle("Confirm");
                                 dialog.show();
                             }
                         });
        //Select image from External Storage...
        select_image.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check for Runtime Permission
                if (ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE )
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText( getApplicationContext(), "Call for Permission", Toast.LENGTH_SHORT ).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE );
                    }
                } else {
                    callgalary();
                }
            }
        } );

        //Initialize Firebase Database paths for database and Storage

        mdatabaseRef = FirebaseDatabase.getInstance().getReference();
        mRoofRef = new Firebase("https://first-project-7f5d4.firebaseio.com/").child("Item_Details(categories)").push();  // Push will create new child every time we upload data
        mStorage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://first-project-7f5d4.appspot.com");


        //Click on Upload Button Title will upload to Database
        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String mName = title.getText().toString().trim();


                if(mName.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Fill all Field", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mName.equals("Our Specials") || mName.equals("Samosa")||mName.equals("Namkeens")||mName.equals("Burgers")
                        ||mName.equals("Toasts")||mName.equals("Rolls")||mName.equals("Wraps")||mName.equals("Sandwiches")||mName.equals("Vadas"))
                {
                    Firebase childRef_name = mRoofRef.child( "Image_Title" );
                    childRef_name.setValue( mName );


                    Toast.makeText( getApplicationContext(), "Updated Info", Toast.LENGTH_SHORT ).show();
                    mRoofRef = new Firebase( "https://first-project-7f5d4.firebaseio.com/" ).child( "Item_Details(categories)" ).push();

                }
                else {
                    //title.setError( "Invalid Input!" );
                    Toast.makeText(getApplicationContext(),"Please Select An Approapriate Category", Toast.LENGTH_SHORT).show();
                    info.setVisibility( android.view.View.VISIBLE );
                }
                //signInAnonymously();
            }
        });

        View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MyUploadService.this, HomeActivity.class));
            }
        });


    }


    //Check for Runtime Permissions for Storage Access
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    callgalary();
                return;
        }
        Toast.makeText(getApplicationContext(), "...", Toast.LENGTH_SHORT).show();
    }



    //If Access Granted gallery Will open
    private void callgalary() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }


    //After Selecting image from gallery image will directly uploaded to Firebase Database
    //and Image will Show in Image View
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {

            mImageUri = data.getData();
            user_image.setImageURI(mImageUri);
            StorageReference filePath = mStorage.child("Item_Images_By_Category").child(mImageUri.getLastPathSegment());

            mProgressDialog.setMessage("Uploading Image....");
            mProgressDialog.show();

            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUri = taskSnapshot.getDownloadUrl();  //Ignore This error

                    mRoofRef.child("Image_URL").setValue(downloadUri.toString());

                    Glide.with(getApplicationContext())
                            .load(downloadUri)
                            .crossFade()
                            .placeholder(R.drawable.loading)
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .into(user_image);
                    Toast.makeText(getApplicationContext(), "Updated.", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                }
            });
        }
    }

    private void signInAnonymously() {


         final String TAG = "Storage#MainActivity";

        // Sign in anonymously. Authentication is required to read or write from Firebase Storage.

        mAuth.signInAnonymously()
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d(TAG, "signInAnonymously:SUCCESS");
                   Toast.makeText(getApplicationContext(), "Successful Signin", Toast.LENGTH_SHORT).show();
                        updateUI(authResult.getUser());
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "signInAnonymously:FAILURE", exception);
                        hideProgressDialog();
                        updateUI(null);
                    }
                });
    }


    private void updateUI(FirebaseUser user) {
        // Signed in or Signed out
        if (user != null) {

        } else {

        }


    }



    private void showProgressDialog(String caption) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.setMessage(caption);
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }



}


package com.example.rujul.breakster;

import android.*;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class UploadService extends AppCompatActivity {
    Button select_image,upload_button,View;
    ImageView user_image;
    EditText title,price,desc;
    public static final int READ_EXTERNAL_STORAGE = 0;
    private static final int GALLERY_INTENT = 2;
    private ProgressDialog mProgressDialog;
    private Firebase namkeen,ourspecials,samosa,burgers,toasts,rolls,vadas,sandwiches,wraps ;
    private FirebaseAuth mAuth;
    private Uri mImageUri = null;
    private DatabaseReference mdatabaseRef;
    private StorageReference mStorage;
    TextView info;
    String cn;
    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_upload_service);

        Firebase.setAndroidContext(this);


        mAuth = FirebaseAuth.getInstance();
        select_image = (Button)findViewById(R.id.select_image);
        upload_button = (Button)findViewById(R.id.upload_bttn);
        user_image = (ImageView) findViewById(R.id.user_image);
        title = (EditText) findViewById(R.id.etTitle);
        price = (EditText) findViewById(R.id.price);
        desc = (EditText)findViewById( R.id.desc );
        View = (Button)findViewById(R.id.View);
      //  info = (TextView)findViewById( R.id.textView11 );
        //Initialize the Progress Bar
        mProgressDialog = new ProgressDialog(UploadService.this);
       // info.setVisibility( android.view.View.GONE );

        Intent i= getIntent();
        cn = i.getStringExtra( "categoryname" );

       /* info.setOnClickListener( new android.view.View.OnClickListener() {
        *  @Override
        *    public void onClick(View v) {
        *       AlertDialog.Builder builder = new AlertDialog.Builder((UploadService.this));
        *       builder.setMessage("The Categories Should Be: Our Specials/Samosa/Namkeens/Rolls/Toasts/Vadas/Sandwiches/Burgers/Wraps").setCancelable(false)
        *               .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        *                   @Override
        *                   public void onClick(DialogInterface dialog, int which) {
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
        });*/
        //Select image from External Storage...
        select_image.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check for Runtime Permission
                if (ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE )
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText( getApplicationContext(), "Call for Permission", Toast.LENGTH_SHORT ).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions( new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE );
                    }
                } else {
                    callgalary();
                }
            }
        } );

        //Initialize Firebase Database paths for database and Storage

        mdatabaseRef = FirebaseDatabase.getInstance().getReference();
       namkeen = new Firebase("https://first-project-7f5d4.firebaseio.com/").child("Namkeens").push();// Push will create new child every time we upload data
        samosa = new Firebase("https://first-project-7f5d4.firebaseio.com/").child("Samosa").push();
        ourspecials = new Firebase("https://first-project-7f5d4.firebaseio.com/").child("Our Specials").push();
        burgers = new Firebase("https://first-project-7f5d4.firebaseio.com/").child("Burgers").push();
        toasts = new Firebase("https://first-project-7f5d4.firebaseio.com/").child("Toasts").push();
        rolls = new Firebase("https://first-project-7f5d4.firebaseio.com/").child("Rolls").push();
        wraps = new Firebase("https://first-project-7f5d4.firebaseio.com/").child("Wraps").push();
        sandwiches= new Firebase("https://first-project-7f5d4.firebaseio.com/").child("Sandwiches").push();
        vadas = new Firebase("https://first-project-7f5d4.firebaseio.com/").child("Vadas").push();
        mStorage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://first-project-7f5d4.appspot.com");


        //Click on Upload Button Title will upload to Database
        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String mName = title.getText().toString().trim();
                final String pr = price.getText().toString().trim();
                final String descr = desc.getText().toString().trim();

                if(mName.isEmpty()|| pr.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Fill all Field", Toast.LENGTH_SHORT).show();
                    return;
                }
               if(cn.equals( "Namkeens" )) {
                   Firebase childRef_name = namkeen.child( "Image_Title" );
                   Firebase price = namkeen.child( "Price" );
                   Firebase description = namkeen.child( "Description" );
                   childRef_name.setValue( mName );
                   price.setValue( pr );
                   description.setValue( descr );


                   Toast.makeText( getApplicationContext(), "Updated Info", Toast.LENGTH_SHORT ).show();
                   namkeen = new Firebase( "https://first-project-7f5d4.firebaseio.com/" ).child( "Namkeens" ).push();

               }
                if(cn.equals( "Burgers" )) {
                    Firebase childRef_name8 = burgers.child( "Image_Title" );
                    Firebase price8 = burgers.child( "Price" );
                    Firebase description8 = burgers.child( "Description" );
                    childRef_name8.setValue( mName );
                    price8.setValue( pr );
                    description8.setValue( descr );

                    Toast.makeText( getApplicationContext(), "Updated Info", Toast.LENGTH_SHORT ).show();
                    burgers = new Firebase( "https://first-project-7f5d4.firebaseio.com/" ).child( "Burgers" ).push();

                }
                if(cn.equals( "Our Specials" )) {
                    Firebase childRef_name1 = ourspecials.child( "Image_Title" );
                    childRef_name1.setValue( mName );
                    Firebase price1 = ourspecials.child( "Price" );
                    Firebase description1 = ourspecials.child( "Description" );
                    price1.setValue( pr );
                    description1.setValue( descr );

                    Toast.makeText( getApplicationContext(), "Updated Info", Toast.LENGTH_SHORT ).show();
                    ourspecials = new Firebase( "https://first-project-7f5d4.firebaseio.com/" ).child( "Our Specials" ).push();

                }
                if(cn.equals( "Rolls" )) {
                    Firebase childRef_name2 = rolls.child( "Image_Title" );
                    childRef_name2.setValue( mName );
                    Firebase price2 = rolls.child( "Price" );
                    Firebase description2 = rolls.child( "Description" );
                    price2.setValue( pr );
                    description2.setValue( descr );

                    Toast.makeText( getApplicationContext(), "Updated Info", Toast.LENGTH_SHORT ).show();
                   rolls = new Firebase( "https://first-project-7f5d4.firebaseio.com/" ).child( "Rolls" ).push();

                }
                if(cn.equals( "Samosas" )) {
                    Firebase childRef_name3 = samosa.child( "Image_Title" );
                    childRef_name3.setValue( mName );
                    Firebase price3 = samosa.child( "Price" );
                    Firebase description3 = samosa.child( "Description" );
                    price3.setValue( pr );
                    description3.setValue( descr );

                    Toast.makeText( getApplicationContext(), "Updated Info", Toast.LENGTH_SHORT ).show();
                    samosa = new Firebase( "https://first-project-7f5d4.firebaseio.com/" ).child( "Samosa" ).push();

                }
                if(cn.equals( "Sandwiches" )) {
                    Firebase childRef_name4 = sandwiches.child( "Image_Title" );
                    childRef_name4.setValue( mName );
                    Firebase price4 = sandwiches.child( "Price" );
                    Firebase description4 = sandwiches.child( "Description" );
                    price4.setValue( pr );
                    description4.setValue( descr );

                    Toast.makeText( getApplicationContext(), "Updated Info", Toast.LENGTH_SHORT ).show();
                    sandwiches = new Firebase( "https://first-project-7f5d4.firebaseio.com/" ).child( "Sandwiches" ).push();

                }
                if(cn.equals( "Toasts" )) {
                    Firebase childRef_name5 = toasts.child( "Image_Title" );
                    childRef_name5.setValue( mName );
                    Firebase price5 = toasts.child( "Price" );
                    Firebase description5 = toasts.child( "Description" );
                    price5.setValue( pr );
                    description5.setValue( descr );

                    Toast.makeText( getApplicationContext(), "Updated Info", Toast.LENGTH_SHORT ).show();
                    toasts = new Firebase( "https://first-project-7f5d4.firebaseio.com/" ).child( "Toasts" ).push();

                }
                if(cn.equals( "Vadas" )) {
                    Firebase childRef_name6 = vadas.child( "Image_Title" );
                    childRef_name6.setValue( mName );
                    Firebase price6 = vadas.child( "Price" );
                    Firebase description6 = vadas.child( "Description" );
                    price6.setValue( pr );
                    description6.setValue( descr );


                    Toast.makeText( getApplicationContext(), "Updated Info", Toast.LENGTH_SHORT ).show();
                    vadas = new Firebase( "https://first-project-7f5d4.firebaseio.com/" ).child( "Vadas" ).push();

                }
                if(cn.equals( "Wraps" )) {
                    Firebase childRef_name7 = wraps.child( "Image_Title" );
                    childRef_name7.setValue( mName );
                    Firebase price7 = wraps.child( "Price" );
                    Firebase description7 = wraps.child( "Description" );

                    price7.setValue( pr );
                    description7.setValue( descr );


                    Toast.makeText( getApplicationContext(), "Updated Info", Toast.LENGTH_SHORT ).show();
                    wraps = new Firebase( "https://first-project-7f5d4.firebaseio.com/" ).child( "Wraps" ).push();

                }
                //signInAnonymously();
            }
        });

        View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(UploadService.this, HomeActivity.class));
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
                    if(cn.equals( "Namkeens" )) {
                        namkeen.child( "Image_URL" ).setValue( downloadUri.toString() );
                    }
                    if(cn.equals( "Burgers" )) {
                        burgers.child( "Image_URL" ).setValue( downloadUri.toString() );
                    }
                    if(cn.equals( "Our Specials" )) {
                        ourspecials.child( "Image_URL" ).setValue( downloadUri.toString() );
                    }
                    if(cn.equals( "Rolls" )) {
                        rolls.child( "Image_URL" ).setValue( downloadUri.toString() );
                    }
                    if(cn.equals( "Samosas" )) {
                        samosa.child( "Image_URL" ).setValue( downloadUri.toString() );
                    }
                    if(cn.equals( "Sandwiches" )) {
                        sandwiches.child( "Image_URL" ).setValue( downloadUri.toString() );
                    }
                    if(cn.equals( "Toasts" )) {
                        toasts.child( "Image_URL" ).setValue( downloadUri.toString() );
                    }if(cn.equals( "Vadas" )) {
                        vadas.child( "Image_URL" ).setValue( downloadUri.toString() );
                    }
                    if(cn.equals( "Wraps" )) {
                        wraps.child( "Image_URL" ).setValue( downloadUri.toString() );
                    }


                    Glide.with(getApplicationContext())
                            .load(downloadUri)
                            .crossFade()
                            .placeholder(R.drawable.loading)
                            .diskCacheStrategy( DiskCacheStrategy.RESULT)
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

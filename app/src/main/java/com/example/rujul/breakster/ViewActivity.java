package com.example.rujul.breakster;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.client.Firebase;
import com.firebase.client.collection.LLRBNode;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.R.attr.direction;
import static android.R.attr.fingerprintAuthDrawable;
import static android.R.attr.value;
import static java.security.AccessController.getContext;

public class ViewActivity extends AppCompatActivity{

    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    ShowDataItems s;
    View v;
    int counter = 1;
    private FirebaseAuth mAuth;
    int clickcount = 1;
    private DatabaseReference mdatabaseRef;
    int newcount;
    private Firebase orders;
    private FirebaseRecyclerAdapter<ShowDataItems, ShowDataViewHolder> mFirebaseAdapter;
    CoordinatorLayout coordinatorLayout;

    public ViewActivity() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // myRef = firebaseDatabase.getReference("User_Details")
        setTitle("Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        String tt= i.getStringExtra( "Title" );

        myRef = FirebaseDatabase.getInstance().getReference(tt);
        mAuth = FirebaseAuth.getInstance();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinator_layout);

        //  add= (Button)findViewById( R.id.add);
        recyclerView = (RecyclerView)findViewById(R.id.show_data_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(ViewActivity.this));

        Toast.makeText(ViewActivity.this, "Wait !  Fetching List...", Toast.LENGTH_SHORT).show();
      
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<ShowDataItems, ShowDataViewHolder>(ShowDataItems.class, R.layout.show_data_items, ShowDataViewHolder.class, myRef) {

            public void populateViewHolder(final ShowDataViewHolder viewHolder, final ShowDataItems model, final int position) {
                viewHolder.Image_URL(model.getImage_URL());
                viewHolder.Image_Title(model.getImage_Title());
                viewHolder.Image_Price(model.getPrice());
                viewHolder.Image_Desc(model.getDescription());

                viewHolder.plus.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counter++;

                        viewHolder.counttxt.setText(Integer.toString(counter));
                        String uid = mAuth.getCurrentUser().getUid();

                        mdatabaseRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("orders").child(viewHolder.Image_Title(model.getImage_Title()));
                        mdatabaseRef.child("ItemCount").setValue(viewHolder.counttxt.getText());

                        SpannableStringBuilder builder = new SpannableStringBuilder();

                        builder.append("CHECKOUT").append("").append("").append("");
                        builder.setSpan(new ImageSpan(ViewActivity.this, R.drawable.exit), builder.length() - 1, builder.length(), 0);

                         Snackbar snackbar = Snackbar.make(coordinatorLayout, "", Snackbar.LENGTH_INDEFINITE);
                                snackbar.setAction("CHECKOUT", handleclick());
                        snackbar.setActionTextColor(Color.WHITE);
                        snackbar.show();
                    }
                });


                viewHolder.minus.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counter--;
                        viewHolder.counttxt.setText(Integer.toString(counter));
                        String uid = mAuth.getCurrentUser().getUid();
                        mdatabaseRef = FirebaseDatabase.getInstance().getReference( "users" ).child( uid ).child("orders").child(viewHolder.Image_Title(model.getImage_Title()));
                        mdatabaseRef.child("ItemCount").setValue(viewHolder.counttxt.getText());

                        if (counter == 0){


                                            mdatabaseRef.child("ItemCount").setValue(null);
                                            mdatabaseRef.child("ItemName").setValue(null);
                                            mdatabaseRef.child("Price").setValue(null);
                                            viewHolder.add.setVisibility(View.VISIBLE);
                                            viewHolder.plus.setVisibility(View.INVISIBLE);
                                            viewHolder.minus.setVisibility(View.INVISIBLE);
                                            viewHolder.counttxt.setVisibility(View.INVISIBLE);
                                }

                    }
                } );

                viewHolder.add.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.plus.setVisibility( View.VISIBLE );
                        viewHolder.minus.setVisibility( View.VISIBLE );
                        viewHolder.add.setVisibility( View.INVISIBLE );

                        String uid = mAuth.getCurrentUser().getUid();
                        mdatabaseRef = FirebaseDatabase.getInstance().getReference( "users" ).child( uid ).child("orders").child(viewHolder.Image_Title(model.getImage_Title()));
                        mdatabaseRef.child("ItemName").setValue( viewHolder.Image_Title( model.getImage_Title() ) );
                        mdatabaseRef.child("Price").setValue( viewHolder.Image_Price( model.getPrice() ) );
                        mdatabaseRef.child("ItemCount").setValue(viewHolder.counttxt.getText());



                    }

                } );
                //OnClick Item
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(final View v) {
                        int s = position;
                        mFirebaseAdapter.getRef((s));
                    }
                });
            }
        };
        recyclerView.setAdapter(mFirebaseAdapter);
    }

    public View.OnClickListener handleclick()
    {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewActivity.this, CheckoutActivity.class));
            }
        };
        return listener;
    }


    //View Holder For Recycler View
    public static class ShowDataViewHolder extends RecyclerView.ViewHolder {
        private final TextView image_title,image_price,image_desc,counttxt;
        private final ImageView image_url;
        private final Button add,plus,minus;


        public ShowDataViewHolder(final View itemView) {
            super(itemView);
            image_url = (ImageView) itemView.findViewById(R.id.fetch_image);
            image_title = (TextView) itemView.findViewById(R.id.fetch_title);
            image_price = (TextView) itemView.findViewById(R.id.fetch_price);
            image_desc = (TextView) itemView.findViewById(R.id.fetch_desc);
            add = (Button) itemView.findViewById( R.id.add );
            plus = (Button) itemView.findViewById( R.id.plus);
            minus = (Button) itemView.findViewById( R.id.minus);
            counttxt = (TextView)itemView.findViewById( R.id.counttext );
            image_title.setAllCaps( true );
            plus.setVisibility( View.INVISIBLE );
            minus.setVisibility( View.INVISIBLE );
        }

        public String Image_Title(String title) {
            image_title.setText(title);
            return title;
        }
        public String Image_Price(String price) {
            image_price.setText(price);
            Log.d( "ViewActivity" ,"This is price ");
            return price;
        }
        public String Image_Desc(String desc) {
            image_desc.setText(desc);
            return desc;
        }

        public  void Image_URL(String title) {
            // image_url.setImageResource(R.drawable.loading);
            Glide.with(itemView.getContext())
                    .load(title)
                    .crossFade()
                    .placeholder(R.drawable.loading)
                    .thumbnail(0.1f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(image_url);
        }
    }
}
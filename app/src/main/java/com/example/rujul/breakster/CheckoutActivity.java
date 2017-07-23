package com.example.rujul.breakster;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CheckoutActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    ShowDataItems s;
    View v;
    int calling =0;
    int price = 0;
    int temp =0;
    int total =0;
    private FirebaseAuth mAuth;
    int clickcount = 1;
    private DatabaseReference mdatabaseRef;
    int newcount;
    Button confirmorder;
    private Firebase orders;
    private FirebaseRecyclerAdapter<ShowDataItems, CheckoutActivity.ShowDataViewHolder> mFirebaseAdapter;
    CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        setTitle("Checkout");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        String uid = mAuth.getCurrentUser().getUid();
        myRef = FirebaseDatabase.getInstance().getReference("users" ).child( uid ).child("orders");
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinator_layout);

        //  add= (Button)findViewById( R.id.add);
        recyclerView = (RecyclerView)findViewById(R.id.show_data_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(CheckoutActivity.this));
        confirmorder=(Button)findViewById(R.id.Confirm);
        Toast.makeText(CheckoutActivity.this, "Wait !  Fetching List...", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<ShowDataItems, CheckoutActivity.ShowDataViewHolder>(ShowDataItems.class, R.layout.show_data_items_checkout, ShowDataViewHolder.class, myRef) {

            public void populateViewHolder(final ShowDataViewHolder viewHolder, final ShowDataItems model, final int position) {
                viewHolder.ItemName(model.getItemName());
                viewHolder.Price(model.getPrice());
                viewHolder.Image_Count(model.getItemCount());

                calling++;

                viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myRef.child("ItemCount").setValue(null);
                        myRef.child("ItemName").setValue(null);
                        myRef.child("Price").setValue(null);

                    }
                });

                //OnClick Item
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(final View v) {
                        int s = position;
                        mFirebaseAdapter.getRef((s));
                    }
                });


                for(int i = 0; i<calling; i++)
                {
                    price = Integer.parseInt(viewHolder.Price(model.getPrice()));;
                    temp = price;
                    total = temp +price;


                }

            }
        };
        recyclerView.setAdapter(mFirebaseAdapter);


        confirmorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),""+ total, Toast.LENGTH_LONG).show();

            }
        });

    }


    //View Holder For Recycler View
    public static class ShowDataViewHolder extends RecyclerView.ViewHolder {
        private final TextView image_title,image_price,item_count;
        Button delete,confirmorder;



        public ShowDataViewHolder(final View itemView) {
            super(itemView);

            image_title = (TextView) itemView.findViewById(R.id.itemname);
            image_price = (TextView) itemView.findViewById(R.id.price);
            item_count = (TextView)itemView.findViewById(R.id.itemcount);
            delete =(Button)itemView.findViewById(R.id.delete);
            //confirmorder=(Button)itemView.findViewById(R.id.Confirm);

        }

        public String ItemName(String title) {
            image_title.setText(title);
            return title;
        }
        public String Price(String price) {
            image_price.setText(price);
            Log.d( "ViewActivity" ,"This is price ");
            return price;
        }
        public String Image_Count(String count) {
            item_count.setText(count);
            return count;
        }

    }

}

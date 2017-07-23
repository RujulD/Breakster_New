package com.example.rujul.breakster;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends Fragment{
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    ShowDataItems s;
    private FirebaseRecyclerAdapter<ShowDataItems, ShowData.ShowDataViewHolder> mFirebaseAdapter;

    public HomeActivity() {
        // Required empty public constructor
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // myRef = firebaseDatabase.getReference("User_Details");
        myRef = FirebaseDatabase.getInstance().getReference("Item_Details(categories)");

        recyclerView = (RecyclerView) v.findViewById(R.id.show_data_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager( (HomeActivity.this).getActivity()));
        Toast.makeText((HomeActivity.this).getActivity(), "Wait ! Fetching List...", Toast.LENGTH_SHORT).show();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<ShowDataItems, ShowData.ShowDataViewHolder>(ShowDataItems.class, R.layout.show_data_single_item, ShowData.ShowDataViewHolder.class, myRef) {

            public void populateViewHolder(final ShowData.ShowDataViewHolder viewHolder, ShowDataItems model, final int position) {
                viewHolder.Image_URL(model.getImage_URL());
              final String t=  viewHolder.Image_Title(model.getImage_Title());

                //OnClick Item
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(final View v) {
                        int s = position;
                        mFirebaseAdapter.getRef((s));

                        Intent i = new Intent(getActivity(), ViewActivity.class);
                         i.putExtra( "Title", t );
                        startActivity(i);

                    }
                });
            }
        };
        recyclerView.setAdapter(mFirebaseAdapter);
    }

}

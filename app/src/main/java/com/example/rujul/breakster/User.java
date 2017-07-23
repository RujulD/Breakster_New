package com.example.rujul.breakster;

import android.os.Handler;
import android.util.Log;
import android.util.StringBuilderPrinter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

@IgnoreExtraProperties
public class User{

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    public static String FB_REFERENCE_USER = "user";
   public String username;
    public String email;
public String pno;
   public String n;
    String uid;

    public User() {

    }

    //public String getUsername() {return username;}
    public String getPno() {
        return pno;
    }
   public String getN() {return n;}


    public String getEmail() {
        return email;
    }

    public User( String n,  String email, String pno) {
      //  this.username = username;
        this.email = email;
        this.pno=pno;
       this.n=n;
    }


    public void writeNewUser( String uid, String name, String email, String pno){
        database.child("users").child(uid).child("Username").setValue(username);
        database.child("users").child(uid).child("Email").setValue(email);
        //database.child("users").child(uid).child("PhoneNumber").setValue(pno);
       database.child("users").child(uid).child("Name").setValue(name);
        database.child("users").child(uid).child("Phone Number").setValue(pno);
    }
    public void updateUser(String uid, String name, String email, String pno) {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FB_REFERENCE_USER);
        
        Map<String, Object> postValues = this.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(uid, postValues);

        myRef.updateChildren(childUpdates);

    }

    private Map<String,Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Username", username);
        result.put("Name", n);
        result.put("Email", email);
        result.put("Phone Number", pno);

        return result;
    }
}

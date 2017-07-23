package com.example.rujul.breakster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {
    EditText email, pass, Name;
    Button submit;
    private FirebaseAuth mAuth;
    private DatabaseReference db;
    User userc;
    TextView alreadysignedin;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "PhoneAuthActivity";
    EditText mob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Register ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email = (EditText) findViewById(R.id.edtemail);
        pass = (EditText) findViewById(R.id.edtpass);
        Name = (EditText) findViewById(R.id.edtname);
        submit = (Button) findViewById(R.id.submit);
        alreadysignedin = (TextView) findViewById(R.id.textView4);
        mob = (EditText) findViewById(R.id.edtmob);
        final SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();


        userc = new User();
        alreadysignedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    final String user = username.getText().toString().trim();
                final String e = email.getText().toString().trim();

                final String newnum = mob.getText().toString().trim();
                final String n = Name.getText().toString().trim();
                final String p = pass.getText().toString().trim();
                if (Name.getText().toString().equals("") || pass.getText().toString().equals("") || email.getText().toString().equals("") || mob.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please fill in all the fields!", Toast.LENGTH_SHORT).show();
                } else if (mob.getText().toString().length() < 10 || mob.getText().toString().length() > 10) {

                    mob.setError("Invalid Phone Number");
                }
                else {
                    mAuth.createUserWithEmailAndPassword(e, p)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    final String userid = mAuth.getCurrentUser().getUid();
                                    if (!task.isSuccessful()) {

                                                  Toast.makeText(getApplicationContext(), "Email already exists. Please try again!", Toast.LENGTH_SHORT).show();
                                                  //  userc.updateUser(userid, n, e, newnum);
                                                    finish();
                                        Intent i = new Intent(RegisterActivity.this, RegisterActivity.class);
                                        startActivity(i);
                                    }
                                    else {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("email", e);
                                        editor.putString("phone", newnum);
                                        editor.putString("user", n);
                                        editor.putString("Password", p);
                                        editor.commit();
                                        userc.writeNewUser(userid, n, e, newnum);
                                        finish();
                                        Intent i = new Intent(RegisterActivity.this, OTPActivity.class);
                                        i.putExtra("mob_number", newnum);
                                        i.putExtra("Names", n);
                                        startActivity(i);
                                    }
                                }

                            });


                }
            }
        });
    }
}
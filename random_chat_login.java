package com.ab1.excuseme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.security.SecureRandom;
import java.math.BigInteger;

public class random_chat_login extends AppCompatActivity {

    EditText name_submission;
    Button proceed;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_chat_login);
        name_submission = (EditText) findViewById(R.id.nameSubmission);
        proceed = (Button) findViewById(R.id.proceed);


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = name_submission.getText().toString();
                SecureRandom random = new SecureRandom();
                //Unique ID for a session
                String location = new BigInteger(130, random).toString(32);
                _user user = new _user(name, "student", location);

                Intent I = new Intent(random_chat_login.this, random_chat_homepage.class);
                I.putExtra("currentUser", user);
                startActivity(I);

                //Initialize Firebase Components
//                FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
//                DatabaseReference mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("Random Chat Session");
//                mMessagesDatabaseReference.push().setValue(_user);

            }
        });
    }
}

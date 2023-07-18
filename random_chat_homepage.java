package com.ab1.excuseme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class random_chat_homepage extends AppCompatActivity {

    Button join_chat;
    Button start_chat;
    EditText sessionName;
    _user user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_chat_homepage);
        join_chat = (Button) findViewById(R.id.join_chat);
        start_chat = (Button) findViewById(R.id.start_chat);
        sessionName = (EditText) findViewById(R.id.session_name);
        Intent I = getIntent();
        user = (_user)I.getSerializableExtra("currentUser");
        Toast.makeText(this, "YOU ARE LOGGED IN AS " + user.toString(), Toast.LENGTH_SHORT).show();

        start_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(random_chat_homepage.this, feed.class);
                I.putExtra("currentUser", user);
                startActivity(I);
            }
        });

        join_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setStorage_name(sessionName.getText().toString());
                Intent I = new Intent(random_chat_homepage.this, feed.class);
                I.putExtra("currentUser", user);
                startActivity(I);
            }
        });

    }
}

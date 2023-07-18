package com.ab1.excuseme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class _opening_screen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);
    }

    public void determineUser(View view){
        String button_text;

        button_text = ((Button) view).getText().toString();

        if(button_text.equals("ADMINISTRATOR")){
            Intent intent = new Intent(this, admin_login.class);
            startActivity(intent);
        }else if(button_text.equals("TEACHER")){
            Intent intent = new Intent(this, teacher_login.class);
            startActivity(intent);
        }else if(button_text.equals("STUDENT")){
            Intent intent = new Intent(this, student_login.class);
            startActivity(intent);
        }else if(button_text.equals("START RANDOM CHAT")) {
            Intent intent = new Intent(this, random_chat_login.class);
            startActivity(intent);
        }

    }

}

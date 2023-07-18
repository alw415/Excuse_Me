package com.ab1.excuseme;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.ab1.excuseme.R;

public class student_homepage extends AppCompatActivity {

    Button Student_Available_Classes_Btn;
    Button Send_Feedback_Btn;
    Button Student_Logout_Btn;
	_user user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_homepage);


        //New code for user FEED
        Intent I = getIntent();
        user = (_user)I.getSerializableExtra("currentUser");

        addListenerOnButton();
    }

    public void addListenerOnButton() {
        final Context context = this;
        Student_Available_Classes_Btn = (Button) findViewById(R.id.Student_Available_Classes_button);
        Send_Feedback_Btn = (Button) findViewById(R.id.Send_Feedback_Button);
        Student_Logout_Btn = (Button) findViewById(R.id.Student_Logout_Button);

        //Go to list of available student classes
        Student_Available_Classes_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View act) {
                Intent intent = new Intent(context, student_available_classes.class);
                intent.putExtra("currentUser", user);
                startActivity(intent);

            }
        });

        //Go to feed back screen
        Send_Feedback_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View act) {
                Intent intent = new Intent(context, student_send_feedback.class);
                intent.putExtra("currentUser", user);
                startActivity(intent);

            }
        });

        //TEACHER LOG OUT
        Student_Logout_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View act) {
                Intent intent = new Intent(context, _opening_screen.class);
                startActivity(intent);

            }
        });


    }
}

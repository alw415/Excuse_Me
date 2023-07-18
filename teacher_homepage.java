package com.ab1.excuseme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class  teacher_homepage extends Activity {
    Button create_new_class;
    Button start_delete_class;
    Button teacher_logout;
    Button view_feedback;

    _user user;
    //TextView tUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_homepage);
        addListenerOnButton();

        //New code for the feed
        Intent I = getIntent();
        user = (_user)I.getSerializableExtra("currentUser");
    }

    public void addListenerOnButton() {

        final Context context = this;

        create_new_class = (Button) findViewById(R.id.create_new_class_request);
        start_delete_class = (Button) findViewById(R.id.SDC);
        teacher_logout = (Button) findViewById(R.id.TLO);
        view_feedback = (Button) findViewById(R.id.viewFeedbackButton);

        //CREATE NEW CLASS
        create_new_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View act) {
                Intent intent = new Intent(context, teacher_create_new_class_request.class);
                intent.putExtra("currentUser", user);
                startActivity(intent);

            }
        });

        //START/DELETE CLASS
        start_delete_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View act) {
                Intent intent = new Intent(context, teacher_list_of_classes.class);
                intent.putExtra("currentUser", user);
                startActivity(intent);

            }
        });


        // VIEW FEEDBACK
        view_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View act) {
                Intent intent = new Intent(context, teacher_view_feedback.class);
                startActivity(intent);

            }
        });

        //TEACHER LOG OUT
        teacher_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View act) {
                Intent intent = new Intent(context, _opening_screen.class);
                startActivity(intent);

            }
        });

    }
}

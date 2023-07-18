package com.ab1.excuseme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Andrew Whitehead on 2/20/2017.
 */

public class admin_homepage extends Activity{
    Button log_out;
    Button view_new_class; //view new request button
    Button delete_class; //delete class button
    Button student_delete;
    Button teacher_delete;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_homepage);
        addListenerOnButton();
    }
    public void addListenerOnButton() {
        final Context context = this;
        log_out = (Button) findViewById(R.id.log_out);
        view_new_class = (Button) findViewById(R.id.view_nr);
        delete_class = (Button) findViewById(R.id.dc);
        student_delete = (Button) findViewById(R.id.delete_student);
        teacher_delete = (Button) findViewById(R.id.delete_teacher);

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View act) {
                Intent intent = new Intent(context, _opening_screen.class);
                startActivity(intent);

            }
        });
        view_new_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View act) {
                Intent intent = new Intent(context, admin_view_new_requests.class);
                startActivity(intent);

            }
        });
        delete_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View act) {
                Intent intent = new Intent(context, admin_delete_classes.class);
                startActivity(intent);

            }
        });
        student_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View act) {
                Intent intent = new Intent(context, admin_delete_student.class);
                startActivity(intent);

            }
        });
        teacher_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, admin_delete_teacher.class);
                startActivity(intent);
            }
        });

    }
}

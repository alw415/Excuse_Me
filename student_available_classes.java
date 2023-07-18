package com.ab1.excuseme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Andrew Whitehead on 2/16/2017.
 */

public class student_available_classes extends Activity {
    Button B2L;
    Button GTC;
    //TextView Student_available_classes;
    EditText Student_Class_Name;
    EditText Student_Section_Name;
    String server_url = "http://proj-309-ab-1.cs.iastate.edu/ExcuseME_OFFICIAL/studentUtil/student_available_classes.php";
    _user user;
    ArrayList<String> class_list;

    ListView list_view;
    ArrayAdapter<String> arrayAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_available_classes);
        addListenerOnButton();
        Intent I = getIntent();
        user = (_user)I.getSerializableExtra("currentUser");
        list_view = (ListView) findViewById(R.id.list_of_available_classes);
        class_list = new ArrayList<String>();
        list_of_available_classes();

        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                class_list );

        list_view.setAdapter(arrayAdapter);
    }

    public void addListenerOnButton() {
        final Context context = this;
        B2L = (Button) findViewById(R.id.B2SL);
        GTC = (Button) findViewById(R.id.GTC);
        Student_Class_Name = (EditText) findViewById(R.id.Name_of_Student_Class);
        Student_Section_Name = (EditText) findViewById(R.id.Student_Class_Section);

        B2L.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View act) {
                Intent intent = new Intent(context, student_homepage.class);
                startActivity(intent);

            }
        });

        GTC.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View act){
                String storage = Student_Class_Name.getText().toString() + " " +
                        Student_Section_Name.getText().toString();

                user.setStorage_name(storage);

                Intent I = new Intent(student_available_classes.this, feed.class);
                I.putExtra("currentUser", user);
                startActivity(I);
            }
        });

    }
    private void list_of_available_classes() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //StringBuilder sb = new StringBuilder();
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray = obj.getJSONArray("res");
                            //list_of_classes.setText("Loading....");
                            //class_list.clear();
                            for (int i=0 ; i < jsonArray.length();i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String class_name = jsonObject.getString("class_name");
                                String section_name = jsonObject.getString("section_name");

                                class_list.add(class_name + " " + section_name);

                                //sb.append(class_name + "\n");
                            }
                            //arrayAdapter.notifyDataSetChanged();

                            //list_of_classes.setText(sb.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue req = Volley.newRequestQueue(this);
        _MySingleton.getInstance(student_available_classes.this).addToRequestQueue(stringRequest);

    }
}
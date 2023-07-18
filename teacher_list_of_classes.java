package com.ab1.excuseme;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.Map;



public class teacher_list_of_classes extends Activity {


    Button delete_class_btn;
    Button start_class_btn;
    EditText class_name_txt;
    EditText section_name_txt;
    //TextView list_of_classes;
    String delete_server_url = "http://proj-309-ab-1.cs.iastate.edu/ExcuseME_OFFICIAL/teacherUtil/delete_class.php";
    String fetch_server_url = "http://proj-309-ab-1.cs.iastate.edu/ExcuseME_OFFICIAL/teacherUtil/fetch_class_names.php";
    String classToDelete;
    String sectionToDelete;
    //ArrayAdapter<String> adapter;
    AlertDialog.Builder builder;
    _user user;
    ArrayList<String> class_list;

    ListView list_view;
    ArrayAdapter<String> arrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list_of_classes);

        //New code for user feed
        Intent I = getIntent();
        user = (_user)I.getSerializableExtra("currentUser");

        list_view = (ListView)findViewById(R.id.listview);
        builder = new AlertDialog.Builder(teacher_list_of_classes.this);
        class_list = new ArrayList<String>();
        //list_of_classes = (TextView)findViewById(R.id.list_of_classes);


        listClasses();
        addListenerOnButton();

        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                class_list );

        list_view.setAdapter(arrayAdapter);
    }

    private void listClasses() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, fetch_server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //StringBuilder sb = new StringBuilder();
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray = obj.getJSONArray("res");
                            //list_of_classes.setText("Loading....");
                            class_list.clear();
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

        _MySingleton.getInstance(teacher_list_of_classes.this).addToRequestQueue(stringRequest);

    }


    public void addListenerOnButton() {
        final Context context = this;

        delete_class_btn = (Button)findViewById(R.id.delete_class);
        start_class_btn = (Button)findViewById(R.id.start_class);
        class_name_txt = (EditText)findViewById(R.id.class_name);
        section_name_txt = (EditText)findViewById(R.id.section_name);


        delete_class_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                classToDelete = class_name_txt.getText().toString();
                sectionToDelete = section_name_txt.getText().toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, delete_server_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    String code = jsonObject.getString("code");
                                    String message = jsonObject.getString("message");
                                    builder.setTitle("Server Response....");
                                    builder.setMessage(message);
                                    displayAlert(code);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("class_name", classToDelete);
                        params.put("section_name", sectionToDelete);
                        return params;
                    }
                };



                _MySingleton.getInstance(teacher_list_of_classes.this).addToRequestQueue(stringRequest);

            }
        });

        start_class_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String storage = class_name_txt.getText().toString() + " " +
                        section_name_txt.getText().toString();

                user.setStorage_name(storage);

                Intent I = new Intent(teacher_list_of_classes.this, feed.class);
                I.putExtra("currentUser", user);
                startActivity(I);

            }
        });


    }

    public void displayAlert(final String code) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (code.equals("delete_sucessful")) {
                    class_name_txt.setText("");
                    section_name_txt.setText("");
                    listClasses();
                    arrayAdapter.notifyDataSetChanged();
                    //finish();;
                } else  {

                    finish();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

}

package com.ab1.excuseme;


/**
 *  Created by Brad Beadle on 4/15/2017.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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



public class admin_delete_teacher extends Activity {

    Button delete_teacher_button;

    EditText teacher_name_text;

    //TextView teacher_list;


    String url_to_delete = "http://proj-309-ab-1.cs.iastate.edu/ExcuseME_OFFICIAL/adminUtil/admin_delete_teacher.php";
    String url_to_fetch = "http://proj-309-ab-1.cs.iastate.edu/ExcuseME_OFFICIAL/adminUtil/fetch_teacher_names.php";

    String teacherToDelete;

    ArrayAdapter<String> adapter;
    AlertDialog.Builder alertBox;
    ArrayList<String> teacher_list;

    ListView list_view;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete_teacher);

        addListenerOnButton();
        alertBox = new AlertDialog.Builder(admin_delete_teacher.this);
        list_view = (ListView) findViewById(R.id.teacher_list);
        teacher_list = new ArrayList<String>();

        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                teacher_list );

        list_view.setAdapter(arrayAdapter);

        listTeachers();
    }

    private void listTeachers() {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url_to_fetch,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //StringBuilder sb = new StringBuilder();
                                JSONObject obj = new JSONObject(response);
                                JSONArray jsonArray = obj.getJSONArray("res");
                                //list_of_classes.setText("Loading....");
                                teacher_list.clear();
                                for (int i=0 ; i < jsonArray.length();i++)
                                {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String name = jsonObject.getString("teacher_name");

                                    teacher_list.add(name);

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

        _MySingleton.getInstance(admin_delete_teacher.this).addToRequestQueue(stringRequest);

    }

        


    public void addListenerOnButton() {
        //final Context context = this;

        delete_teacher_button = (Button)findViewById(R.id.delete_teacher);

        teacher_name_text = (EditText)findViewById(R.id.teacher_name);



        delete_teacher_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                teacherToDelete = teacher_name_text.getText().toString();


                StringRequest stringRequest = new StringRequest(Request.Method.POST, url_to_delete,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    String code = jsonObject.getString("code");
                                    String message = jsonObject.getString("message");
                                    alertBox.setTitle("Server Response....");
                                    alertBox.setMessage(message);
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
                        Map<String, String> params = new HashMap<>();
                        params.put("teacher_name", teacherToDelete);
                        return params;
                    }
                };



                _MySingleton.getInstance(admin_delete_teacher.this).addToRequestQueue(stringRequest);

            }
        });

    }

    public void displayAlert(final String code) {
        alertBox.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (code.equals("delete_successful")) {
                    teacher_name_text.setText("");
                    listTeachers();
                    arrayAdapter.notifyDataSetChanged();
                } else  {

                    finish();
                }
            }
        });
        AlertDialog alertDialog = alertBox.create();
        alertDialog.show();

    }

}





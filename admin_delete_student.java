package com.ab1.excuseme;

import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class admin_delete_student extends Activity {
    Button Deletion;
    EditText studentName;
    String server_url = "http://proj-309-ab-1.cs.iastate.edu/ExcuseME_OFFICIAL/adminUtil/delete_student.php";
    String fetch_student_names = "http://proj-309-ab-1.cs.iastate.edu/ExcuseME_OFFICIAL/adminUtil/fetch_student_names.php";
    AlertDialog.Builder builder;
    String S_Delete;
    ArrayList<String> student_list;

    ListView list_view;
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete_student);
        listStudents();
        addListenerOnButton();
        builder = new AlertDialog.Builder(admin_delete_student.this);
        list_view = (ListView)findViewById(R.id.listview);
        student_list = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                student_list );

        list_view.setAdapter(arrayAdapter);
    }

    private void listStudents() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, fetch_student_names,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //StringBuilder sb = new StringBuilder();
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray = obj.getJSONArray("res");
                            //list_of_classes.setText("Loading....");
                            student_list.clear();
                            for (int i=0 ; i < jsonArray.length();i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String name = jsonObject.getString("student_name");

                                student_list.add(name);

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

        _MySingleton.getInstance(admin_delete_student.this).addToRequestQueue(stringRequest);

    }

    public void addListenerOnButton() {
        final Context context = this;

        Deletion = (Button) findViewById(R.id.delete_button);
        studentName = (EditText) findViewById(R.id.editText2);

        Deletion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                S_Delete = studentName.getText().toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
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
                        params.put("student_name", S_Delete);
                        return params;
                    }
                };


                _MySingleton.getInstance(admin_delete_student.this).addToRequestQueue(stringRequest);


            }

        });

    }

    public void displayAlert(final String code) {

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {

                if (code.equals("input_error")) {

                    finish();;

                } else if (code.equals("delete_success")) {
                    listStudents();
                    arrayAdapter.notifyDataSetChanged();
                    finish();

                } else if (code.equals("delete_failed")) {

                    studentName.setText("");


                }

            }

        });

        AlertDialog alertDialog = builder.create();

        alertDialog.show();


    }

}

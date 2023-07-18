package com.ab1.excuseme;

/**
 * Created by Brad Beadle on 4/11/2017.
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

public class admin_delete_classes extends Activity{

    Button delete_class_button;

    EditText class_name_text;
    EditText section_name_text;

    //TextView class_list;
    ArrayList<String> list_of_classes;

    ListView list_view;
    ArrayAdapter<String> arrayAdapter;

    String url_to_delete = "http://proj-309-ab-1.cs.iastate.edu/ExcuseME_OFFICIAL/adminUtil/admin_delete_class.php";
    String url_to_fetch = "http://proj-309-ab-1.cs.iastate.edu/ExcuseME_OFFICIAL/teacherUtil/fetch_class_names.php";
    String classToDelete;
    String sectionToDelete;

    ArrayAdapter<String> adapter;
    AlertDialog.Builder alertBox;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete_classes);
        addListenerOnButton();
        alertBox = new AlertDialog.Builder(admin_delete_classes.this);
        list_view = (ListView)findViewById(R.id.class_list);
        list_of_classes = new ArrayList<String>();

        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                list_of_classes );

        list_view.setAdapter(arrayAdapter);

        listClasses();
    }

    private void listClasses() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_to_fetch,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //StringBuilder sb = new StringBuilder();
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray = obj.getJSONArray("res");
                            //list_of_classes.setText("Loading....");
                            list_of_classes.clear();
                            for (int i=0 ; i < jsonArray.length();i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String class_name = jsonObject.getString("class_name");
                                String section_name = jsonObject.getString("section_name");

                                list_of_classes.add(class_name + " " + section_name);

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
        _MySingleton.getInstance(admin_delete_classes.this).addToRequestQueue(stringRequest);
    }

    public void addListenerOnButton() {
        final Context context = this;

        delete_class_button = (Button)findViewById(R.id.delete_class);

        class_name_text = (EditText)findViewById(R.id.class_name);
        section_name_text = (EditText)findViewById(R.id.section_name);


        delete_class_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                classToDelete = class_name_text.getText().toString();
                sectionToDelete = section_name_text.getText().toString();

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
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("class_name", classToDelete);
                        params.put("section_name", sectionToDelete);
                        return params;
                    }
                };



                _MySingleton.getInstance(admin_delete_classes.this).addToRequestQueue(stringRequest);

            }
        });

    }

    public void displayAlert(final String code) {
        alertBox.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (code.equals("delete_successful")) {
                    class_name_text.setText("");
                    section_name_text.setText("");
                    listClasses();
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

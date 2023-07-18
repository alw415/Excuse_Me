package com.ab1.excuseme;

/**
 * Created by Andrew Whitehead on 3/19/2017.
 */
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

public class admin_view_new_requests extends Activity{
    Button Done;
    Button Create;
    EditText class_name_txt;
    EditText section_name_txt;
    String delete_request_url = "http://proj-309-ab-1.cs.iastate.edu/ExcuseME_OFFICIAL/adminUtil/DeleteRequest.php";

    String classToCreate;
    String sectionToCreate;

    AlertDialog.Builder builder;
    String server_url = "http://proj-309-ab-1.cs.iastate.edu/ExcuseME_OFFICIAL/adminUtil/view_new_requests.php";

    ArrayList<String> request_list;
    ListView list_view;
    ArrayAdapter<String> arrayAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_new_requests);

        builder = new AlertDialog.Builder(admin_view_new_requests.this);
        list_view = (ListView)findViewById(R.id.list_view);
        request_list = new ArrayList<String>();

        list_Requests();
        addListenerOnButton();

        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                request_list );

        list_view.setAdapter(arrayAdapter);
    }

    private void list_Requests() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //StringBuilder sb = new StringBuilder();
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray = obj.getJSONArray("res");

                            request_list.clear();
                            for (int i=0 ; i < jsonArray.length();i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String class_name = jsonObject.getString("class_name");
                                String section_name = jsonObject.getString("section_name");
                                //sb.append(class_name + "\n");

                                request_list.add(class_name + " " + section_name);
                            }
                            //listRequests.setText(sb.toString());


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
        _MySingleton.getInstance(admin_view_new_requests.this).addToRequestQueue(stringRequest);
    }

    public void addListenerOnButton() {
        final Context context = this;
        Done = (Button) findViewById(R.id.button2);
        Create = (Button) findViewById(R.id.CC);
        class_name_txt = (EditText)findViewById(R.id.request_class_name);
        section_name_txt = (EditText)findViewById(R.id.request_section_name);

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View act) {
                Intent intent = new Intent(context, admin_homepage.class);
                startActivity(intent);
            }
        });

        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classToCreate = class_name_txt.getText().toString();
                sectionToCreate = section_name_txt.getText().toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, delete_request_url,
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
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("class_name", classToCreate);
                        params.put("section_name", sectionToCreate);
                        return params;
                    }
                };

                _MySingleton.getInstance(admin_view_new_requests.this).addToRequestQueue(stringRequest);
            }
        });

    }

    public void displayAlert(final String code) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (code.equals("Create_successful")) {
                    class_name_txt.setText("");
                    section_name_txt.setText("");
                    list_Requests();
                    arrayAdapter.notifyDataSetChanged();

                } else  {

                    finish();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

}

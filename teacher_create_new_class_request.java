package com.ab1.excuseme;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class teacher_create_new_class_request extends AppCompatActivity {

    Button create_class_bn;
    EditText studentEmails, sectionName, className, teacherName;
    String _studentEmails, _sectionName, _className, _teacherName;
    AlertDialog.Builder builder;
    String server_url = "http://proj-309-ab-1.cs.iastate.edu/ExcuseME_OFFICIAL/teacherUtil/createNewClassRequest.php";
    _user user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_create_new_class_request);

        //New code for Feed user
        Intent I = getIntent();
        user = (_user)I.getSerializableExtra("currentUser");

        builder = new AlertDialog.Builder(teacher_create_new_class_request.this);

        create_class_bn = (Button) findViewById(R.id.create_class_bn);
        studentEmails = (EditText) findViewById(R.id.student_email_submission);
        sectionName = (EditText) findViewById(R.id.sectionNameSubmission);
        className = (EditText) findViewById(R.id.classNameSubmission);
        teacherName = (EditText) findViewById(R.id.teacherNameSubmission);

        create_class_bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Grab all of the data from the EditText
                _studentEmails = studentEmails.getText().toString();
                _sectionName = sectionName.getText().toString();
                _className = className.getText().toString();
                _teacherName = teacherName.getText().toString();

                if (_studentEmails.equals("") || _sectionName.equals("") || _className.equals("")) {
                    builder.setTitle("Tsk Tsk...");
                    builder.setMessage("You left a field blank...");
                    displayAlert("input_error");
                }
                else {
                    //All Fields filled
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
                            params.put("class_name", _className);
                            params.put("student_emails", _studentEmails);
                            params.put("section_name", _sectionName);
                            params.put("teacher_name", _teacherName);
                            return params;
                        }
                    };

                    _MySingleton.getInstance(teacher_create_new_class_request.this).addToRequestQueue(stringRequest);

                }


            }


        });

    }

    public void displayAlert(final String code) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (code.equals("input_error")) {
                    finish();;
                } else if (code.equals("reg_success")) {
                    finish();
                } else if (code.equals("reg_failed")) {
                    className.setText("");
                    studentEmails.setText("");
                    sectionName.setText("");
                    sectionName.setText("");
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}


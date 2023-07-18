package com.ab1.excuseme;

import android.content.Context;
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

public class student_send_feedback extends AppCompatActivity {

    Button Cancel;
    Button Send_Feedback;
    EditText Class_Section, Name_of_Class, Feedback_Input;
    String class_section, name_of_class, feedback_input;
    String server_url = "http://proj-309-ab-1.cs.iastate.edu/ExcuseME_OFFICIAL/studentUtil/Create_Feedback.php";
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_send_feedback);
        addListenerOnButton();
        builder = new AlertDialog.Builder(student_send_feedback.this);

    }

    public void addListenerOnButton() {
        final Context context = this;
        Cancel = (Button) findViewById(R.id.Cancel_Feedback);
        Send_Feedback = (Button) findViewById(R.id.Send_Feedback);
        Class_Section = (EditText) findViewById(R.id.Entered_Class_Section);
        Name_of_Class = (EditText) findViewById(R.id.Entered_Class);
        Feedback_Input = (EditText) findViewById(R.id.Entered_Feedback);

        //GO BACK
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View act) {
                Intent intent = new Intent(context, student_homepage.class);
                startActivity(intent);

            }
        });

        //SEND FEEDBACK
        Send_Feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View act) {
                class_section = Class_Section.getText().toString();
                name_of_class = Name_of_Class.getText().toString();
                feedback_input = Feedback_Input.getText().toString();

                if(class_section.equals("") || name_of_class.equals("") || feedback_input.equals("")){
                    builder.setTitle("Try Again");
                    builder.setMessage("Enter text in all fields");
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
                            params.put("className", name_of_class);
                            params.put("sectionName", class_section);
                            params.put("feedBack", feedback_input);
                            return params;
                        }
                    };

                    _MySingleton.getInstance(student_send_feedback.this).addToRequestQueue(stringRequest);

                }

            }
        });
    }
    public void displayAlert(final String code) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (code.equals("input_error")) {
                    finish();
                } else if (code.equals("reg_success")) {
                    finish();
                } else if (code.equals("reg_failed")) {
                    Name_of_Class.setText("");
                    Class_Section.setText("");
                    Feedback_Input.setText("");
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}

package com.ab1.excuseme;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class student_login extends AppCompatActivity {

    Button login_button, register_button;
    EditText UserName, Password;
    String username, password;
    String login_url = "http://proj-309-ab-1.cs.iastate.edu/ExcuseME_OFFICIAL/studentLogin/loginSTUDENT.php";
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        register_button = (Button) findViewById(R.id.register_bn_student);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(student_login.this, student_register.class));
            }
        });

        builder = new AlertDialog.Builder(student_login.this);
        login_button = (Button) findViewById(R.id.login_bn_student);
        UserName = (EditText) findViewById(R.id.userNameSubmission);
        Password = (EditText) findViewById(R.id.passwordSubmission);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = UserName.getText().toString();
                password = Password.getText().toString();

                if (username.equals("") || password.equals("")) {
                    builder.setTitle("Mayhaps you should fill in the username and password...");
                    displayAlert("Enter a valid username and password por favor...");
                } else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String code = jsonObject.getString("code");
                                        if (code.equals("login_failed")) {
                                            builder.setTitle("Login Error...");
                                            displayAlert(jsonObject.getString("message"));
                                        } else {
                                            //New code for user FEED
                                            _user user = new _user(username, "student", "PLACEHOLDER");

                                            Intent intent = new Intent(student_login.this, student_homepage.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("name", jsonObject.getString("name"));
                                            intent.putExtras(bundle);

                                            //New code for user FEED
                                            intent.putExtra("currentUser", user);

                                            startActivity(intent);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(student_login.this,"Error", Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                        }
                    }) {
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("user_name", username);
                            params.put("password", password);
                            return params;
                        }
                    };
                    _MySingleton.getInstance(student_login.this).addToRequestQueue(stringRequest);
                }
            }
        });


    }

    public void displayAlert(String message) {
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserName.setText("");
                Password.setText("");
            }

        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}

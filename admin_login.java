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

public class admin_login extends AppCompatActivity {

    Button login_button, register_button;
    EditText UserName, Password;
    String username, password;
    String login_url = "http://proj-309-ab-1.cs.iastate.edu/ExcuseME_OFFICIAL/adminLogin/loginADMIN.php";
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_log);
        register_button = (Button) findViewById(R.id.register_bn_admin);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(admin_login.this, admin_register.class));
            }
        });

        builder = new AlertDialog.Builder(admin_login.this);
        login_button = (Button) findViewById(R.id.login_bn_admin);
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
                                            Intent intent = new Intent(admin_login.this, admin_homepage.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("name", jsonObject.getString("name"));
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(admin_login.this,"Error", Toast.LENGTH_LONG).show();
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
                    _MySingleton.getInstance(admin_login.this).addToRequestQueue(stringRequest);
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

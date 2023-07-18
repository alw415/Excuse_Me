package com.ab1.excuseme;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class teacher_view_feedback extends Activity {

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> classList = new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    // PHP TO FETCH CLASSES AND SECTION
    String url_to_fetch = "http://proj-309-ab-1.cs.iastate.edu/ExcuseME_OFFICIAL/teacherUtil/fetch_feedback.php";

    // LISTVIEW
    ListView feedbackListView;

    // HANDLE FEEDBACK
    ArrayList<String> feedbackList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_view_feedback);


        /**
         * CAN ALSO BE CODED AS:
         *  ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, classList)
         */

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,
                classList);

        // GET LISTVIEW OBJECT from XML
        feedbackListView = (ListView) findViewById(R.id.feedbackListView);
        populateClassList();

        // ASSIGN ADAPTER TO LISTVIEW
        feedbackListView.setAdapter(adapter);

        // LISTVIEW ITEM CLICK LISTENER
        feedbackListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //  GET CLASS CHOSEN FROM POSITION
                    String class_chosen = "help";//(String) feedbackList.get(position);

                    Toast.makeText(teacher_view_feedback.this, class_chosen, Toast.LENGTH_SHORT).show();
                }
            }
        );



    }

    private void populateClassList () {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_to_fetch,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray = obj.getJSONArray("res");

                            for (int i=0 ; i < jsonArray.length();i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String class_name = jsonObject.getString("class_name");
                                //String section_name = jsonObject.getString("section_name");
                                String feedback = jsonObject.getString(("feedback"));

                                classList.add(class_name + "          " + feedback);
                                //feedbackList.add(feedback);

                            }
                            adapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        _MySingleton.getInstance(teacher_view_feedback.this).addToRequestQueue(stringRequest);

    }

    public void addListenerOnButton() {

        final Context context = this;


    }

}

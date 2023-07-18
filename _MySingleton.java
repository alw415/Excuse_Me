package com.ab1.excuseme;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by William Donahoe on 3/07/2017.
 */

public class _MySingleton {

    private static _MySingleton mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;

    private _MySingleton(Context context){
        mCtx = context;
        requestQueue = getRequestQueue();
    }


    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized _MySingleton getInstance(Context context){
        if (mInstance == null){
            mInstance = new _MySingleton(context);
        }
        return mInstance;
    }

    public <T> void addToRequestQueue(Request<T> request){
        requestQueue.add(request);
    }



}
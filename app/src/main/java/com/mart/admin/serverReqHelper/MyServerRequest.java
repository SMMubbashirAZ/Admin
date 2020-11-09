package com.mart.admin.serverReqHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mart.admin.R;
import com.mart.admin.activities.LoginActivity;
import com.mart.admin.constants.AppConstants;
import com.mart.admin.listeners.CustomInfoDialogListener;
import com.mart.admin.listeners.ServerRequestListener;
import com.mart.admin.sharedPref.SessionManager;
import com.mart.admin.utils.HeaderDialog;
import com.mart.admin.utils.UtilityFunctions;

import org.json.JSONException;
import org.json.JSONObject;


public class MyServerRequest {

    private String url;
    private JSONObject jsonObject;
    private int requestCode = 0;
    private Context context;
    private RequestQueue queue;
    private CustomInfoDialogListener listener;
    private SessionManager userSettings;
    private ServerRequestListener serverRequestListener;

    public MyServerRequest(Context context, String url, JSONObject jsonObject, int requestCode, ServerRequestListener serverRequestListener) {
        this.url = url;
        this.jsonObject = jsonObject;
        this.requestCode = requestCode;
        this.context = context;
        this.serverRequestListener = serverRequestListener;
        queue = VolleySingleton.getInstance(context).getReq_queue();
        userSettings = SessionManager.getInstance(context);
    }

    public MyServerRequest(Context context, String url, JSONObject jsonObject, ServerRequestListener serverRequestListener) {
        this.url = url;
        this.jsonObject = jsonObject;
        this.context = context;
        this.serverRequestListener = serverRequestListener;
        queue = VolleySingleton.getInstance(context).getReq_queue();
        userSettings = SessionManager.getInstance(context);
    }

    public MyServerRequest(Context context, String url, int requestCode, ServerRequestListener serverRequestListener) {
        this.url = url;
        this.context = context;
        this.requestCode = requestCode;
        this.serverRequestListener = serverRequestListener;
        queue = VolleySingleton.getInstance(context).getReq_queue();
        userSettings = SessionManager.getInstance(context);
    }

    public MyServerRequest(Context context, String url, ServerRequestListener serverRequestListener) {
        this.url = url;
        this.context = context;
        this.serverRequestListener = serverRequestListener;
        queue = VolleySingleton.getInstance(context).getReq_queue();
        userSettings = SessionManager.getInstance(context);
    }


    public void sendRequest() {

      /*  if (UtilityFunctions.isNetworkAvailable(context)) {
            serverRequestListener.onPreResponse();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, response -> {
                try {
                    if (response.getBoolean("isUserActive")) {
                        serverRequestListener.onPostResponse(response, requestCode);
                    } else {
                        userSettings.clear();
                        userSettings.clearAllPreferences();
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    }
                } catch (JSONException e) {

                }

            }, error -> {

                VolleyLog.e("TransactionRequest", "Error: " + error);
                Log.e("TransactionRequest", "Error: " + error);
                VolleyErrorHelper.ShowError(error, context, listener);
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsonObjectRequest);
        } else {
            HeaderDialog.showInfoDialog(context, context.getResources().getString(R.string.messages),
                    "No internet connection found please check your wifi or network state", () -> ((Activity) context).finish());
        }*/

        if (UtilityFunctions.isNetworkAvailable(context)) {
            serverRequestListener.onPreResponse();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, response -> {
                serverRequestListener.onPostResponse(response, requestCode);

            }, error -> {
                UtilityFunctions.hideProgressDialog(true);
                VolleyLog.e("TransactionRequest", "Error: " + error);
                Log.e("TransactionRequest", "Error: " + error);
                VolleyErrorHelper.ShowError(error, context, listener);
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsonObjectRequest);
        } else {
            HeaderDialog.showInfoDialog(context, context.getResources().getString(R.string.messages),
                    "No internet connection found please check your wifi or network state", () -> ((Activity) context).finish());
        }

    }
    public void sendLoginRequest() {

        if (UtilityFunctions.isNetworkAvailable(context)) {
            serverRequestListener.onPreResponse();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, response -> {
                try {
                    if (response.getBoolean(AppConstants.HAS_RESPONSE)) {

                        UtilityFunctions.hideProgressDialog(true);
                        serverRequestListener.onPostResponse(response, requestCode);
                        userSettings.set(AppConstants.IS_USER_LOGIN,true);

                    } else {
                        UtilityFunctions.hideProgressDialog(true);
                        Toast.makeText(context, response.getString(AppConstants.MESSAGE), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {

                }

            }, error -> {

                UtilityFunctions.hideProgressDialog(true);
                VolleyLog.e("TransactionRequest", "Error: " + error);
                Log.e("TransactionRequest", "Error: " + error);
                VolleyErrorHelper.ShowError(error, context, listener);
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsonObjectRequest);
        } else {
            HeaderDialog.showInfoDialog(context, context.getResources().getString(R.string.messages),
                    "No internet connection found please check your wifi or network state", () -> ((Activity) context).finish());
        }



    }

    public void sendGetRequest() {

        if (UtilityFunctions.isNetworkAvailable(context)) {
            serverRequestListener.onPreResponse();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
                serverRequestListener.onPostResponse(response, requestCode);

            }, error -> {
                VolleyLog.e("TransactionRequest", "Error: " + error);
                UtilityFunctions.hideProgressDialog(true);
                Log.e("TransactionRequest", "Error: " + error);
                VolleyErrorHelper.ShowError(error, context, listener);
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsonObjectRequest);
        } else {
            HeaderDialog.showInfoDialog(context, context.getResources().getString(R.string.messages),
                    "No internet connection found please check your wifi or network state", () -> ((Activity) context).finish());
        }

    }

    public void setCustomDialogListener(CustomInfoDialogListener listener) {
        this.listener = listener;
    }
}

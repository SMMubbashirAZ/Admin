package com.mart.admin.listeners;

import org.json.JSONObject;

public interface ServerRequestListener {

    void onPreResponse();
    void onPostResponse(JSONObject jsonResponse, int requestCode);

}

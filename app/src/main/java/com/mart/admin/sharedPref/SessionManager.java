package com.mart.admin.sharedPref;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.bumptech.glide.signature.ObjectKey;
import com.google.gson.Gson;
import com.mart.admin.activities.LoginActivity;
import com.mart.admin.constants.AppConstants;
import com.mart.admin.model.ProductModel;
import com.mart.admin.model.ShopModel;
import com.mart.admin.model.UserModel;

import java.util.HashMap;
import java.util.UUID;

public class SessionManager {

    private static final String SHARED_PREF_NAME = "user_settings";
    private static SessionManager sSharedPrefs;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private boolean mBulkUpdate = false;
    private Context mContextRef;

    public SessionManager(Context context) {
        mPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        mContextRef = context;
    }

    public void createUserSession(UserModel userModel)
    {
        doEdit();
        mEditor.putBoolean(AppConstants.LOGIN, true);
        Gson gson = new Gson();
        mEditor.putString("UserModel",gson.toJson(userModel));
        mEditor.apply();
        doCommit();
    }

    public void createShopObject(ShopModel userModel)
    {
//        mEditor.putBoolean(AppConstants.SHOPS, true);
        doEdit();
        Gson gson = new Gson();
        mEditor.putString(AppConstants.SHOPS,gson.toJson(userModel));
        mEditor.apply();
    }


    public ShopModel getShopsObj(){

        Gson gson = new Gson();
        String json = mPref.getString(AppConstants.SHOPS, "");
        return gson.fromJson(json, ShopModel.class);
    }
    public void createProdObject(ProductModel prodModel)
    {
//        mEditor.putBoolean(AppConstants.SHOPS, true);
        doEdit();
        Gson gson = new Gson();
        mEditor.putString(AppConstants.PRODUCT,gson.toJson(prodModel));
        mEditor.apply();
    }


    public ProductModel getProdObj(){

        Gson gson = new Gson();
        String json = mPref.getString(AppConstants.PRODUCT, "");
        return gson.fromJson(json, ProductModel.class);
    }
    public UserModel getUserSession(){
        Gson gson = new Gson();
        String json = mPref.getString("UserModel", "");
        return gson.fromJson(json, UserModel.class);
    }

    public boolean isLoggin() {
        return mPref.getBoolean(AppConstants.LOGIN, AppConstants.DEFAULT_BOOLEAN_VALUE);
    }


    public static SessionManager getInstance(Context context) {
        if (sSharedPrefs == null) {
            sSharedPrefs = new SessionManager(context);
        }
        return sSharedPrefs;
    }

    public void logout() {
        clearAllPreferences();
        Intent i = new Intent(mContextRef, LoginActivity.class);
        mContextRef.startActivity(i);
        ( (Activity) mContextRef).finish();

    }

    public static SessionManager getInstance() {
        if (sSharedPrefs != null) {
            return sSharedPrefs;
        }

        //Option 1:
        throw new IllegalArgumentException("Should use getInstance(Context) at least once before using this method.");

        //Option 2:
        // Alternatively, you can create a new instance here
        // with something like this:
        // getInstance(MyCustomApplication.getAppContext());
    }

    public void set(String key, String val) {
        doEdit();
        mEditor.putString(key, val);
        doCommit();
    }

    public void set(String key, int val) {
        doEdit();
        mEditor.putInt(key, val);
        doCommit();
    }

    public void set(String key, boolean val) {
        doEdit();
        mEditor.putBoolean(key, val);
        doCommit();
    }

    public void set(String key, float val) {
        doEdit();
        mEditor.putFloat(key, val);
        doCommit();
    }

    public void set(String key, double val) {
        doEdit();
        mEditor.putString(key, String.valueOf(val));
        doCommit();
    }

    public void set(String key, long val) {
        doEdit();
        mEditor.putLong(key, val);
        doCommit();
    }

    public boolean containValue(String Key) {
        return mPref.contains(Key);
    }

    public void deletePreferencesForKey(String key) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.remove(key);
        editor.commit();
    }

    public void clearAllPreferences() {
        SharedPreferences.Editor editor = mPref.edit();
        editor.clear();
        editor.commit();
    }

    public String getString(String key) {
        return mPref.getString(key, null);
    }

    public int getInt(String key) {
        return mPref.getInt(key, 0);
    }

    public long getLong(String key) {
        return mPref.getLong(key, 0);
    }

    public float getFloat(String key) {
        return mPref.getFloat(key, 0);
    }

    public double getDouble(String key) {
        return getDouble(key, 0);
    }

    public boolean getBoolean(String key) {
        return mPref.getBoolean(key, false);
    }

    public String getString(String key, String defaultValue) {
        return mPref.getString(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return mPref.getInt(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return mPref.getLong(key, defaultValue);
    }

    public float getFloat(String key, float defaultValue) {
        return mPref.getFloat(key, defaultValue);
    }

    public double getDouble(String key, double defaultValue) {
        try {
            return Double.valueOf(mPref.getString(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mPref.getBoolean(key, defaultValue);
    }

    /**
     * Remove keys from SharedPreferences.
     *
     * @param keys The name of the key(s) to be removed.
     */
    public void remove(String... keys) {
        doEdit();
        for (String key : keys) {
            mEditor.remove(key);
        }
        doCommit();
    }

    /**
     * Remove all keys from SharedPreferences.
     */
    public void clear() {
        doEdit();
        mEditor.clear();
        doCommit();
    }

    public void edit() {
        mBulkUpdate = true;
        mEditor = mPref.edit();
    }

    public void commit() {
        mBulkUpdate = false;
        mEditor.commit();
        mEditor = null;
    }

    private void doEdit() {
        if (!mBulkUpdate && mEditor == null) {
            mEditor = mPref.edit();
        }
    }

    private void doCommit() {
        if (!mBulkUpdate && mEditor != null) {
            mEditor.commit();
            mEditor = null;
        }
    }

    public String getKeyFor(String key, int index) {
        return String.format(key, index);
    }




}

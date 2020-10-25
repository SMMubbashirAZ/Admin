package com.mart.admin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.mart.admin.R;
import com.mart.admin.constants.AppConstants;
import com.mart.admin.constants.URLS;
import com.mart.admin.listeners.ServerRequestListener;
import com.mart.admin.serverReqHelper.MyServerRequest;
import com.mart.admin.utils.UtilityFunctions;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseCompatActivity implements Validator.ValidationListener{
    private static final String TAG=LoginActivity.class.getSimpleName();



    private String lastChar = " ";
    @BindView(R.id.tiedt_login_phn_no)
    @NotEmpty
    @Length(min = 11, max = 11)
    @Order(1)
    TextInputEditText phn_no;

    @BindView(R.id.tiedt_login_pass)
    @NotEmpty
    @Password(min = 8)
    @Order(2)
    TextInputEditText password;

    private Validator mValidator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        hideUI();

        // Validator
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);




        phn_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                int digits = phn_no.getText().toString().length();
                if (digits > 1)
                    lastChar = phn_no.getText().toString().substring(digits - 1);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int digits = phn_no.getText().toString().length();
                Log.d("LENGTH", "" + digits);
                if (!lastChar.equals("-")) {
                    if (digits == 4) {
                        phn_no.append("-");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }




    @OnClick(R.id.btn_login_signin)
    public void Login(){
        try {
            mValidator.validate();
        } catch (IllegalStateException e) {
            Toast.makeText(this, "CRASH", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onValidationSucceeded() {
        String number = phn_no.getText().toString().trim();
        String pass = password.getText().toString().trim();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Number",number);
            jsonObject.put("Password",pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyServerRequest myServerRequest = new MyServerRequest(LoginActivity.this, URLS.LOGIN, jsonObject, new ServerRequestListener() {
            @Override
            public void onPreResponse() {
                UtilityFunctions.showProgressDialog(LoginActivity.this,true);
            }

            @Override
            public void onPostResponse(JSONObject jsonResponse, int requestCode) {

                UtilityFunctions.hideProgressDialog(true);
                try {
                    if (jsonResponse.getBoolean(AppConstants.HAS_RESPONSE)){
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
                        finish();
                    }
                    Log.i(TAG, "onPostResponse: "+jsonResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        myServerRequest.sendLoginRequest();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for(ValidationError error : errors){
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if(view instanceof TextInputEditText){
                ((TextInputEditText) view).setError(message);
            } else {
                Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
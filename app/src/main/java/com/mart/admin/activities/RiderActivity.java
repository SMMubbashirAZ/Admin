package com.mart.admin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mart.admin.R;
import com.mart.admin.adapter.RiderAdapter;
import com.mart.admin.adapter.ShopAdapter;
import com.mart.admin.constants.AppConstants;
import com.mart.admin.constants.URLS;
import com.mart.admin.listeners.ServerRequestListener;
import com.mart.admin.model.RiderModel;
import com.mart.admin.model.ShopModel;
import com.mart.admin.serverReqHelper.MyServerRequest;
import com.mart.admin.utils.UtilityFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RiderActivity extends AppCompatActivity {

    private static final String TAG = RiderActivity.class.getSimpleName();
    @BindView(R.id.rv_rider_approval)
    RecyclerView rv_prod;
    @BindView(R.id.edt_rider_search)
    EditText edt_search;
    private RiderAdapter RiderAdapter;
    private View include_layout;
    private ImageView iv_back;
    private TextView tv_title;
    private int GET_PROD_REQ = 10;
    private List<RiderModel> riderModelList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);
        ButterKnife.bind(this);
        BindID();
        if (UtilityFunctions.isNetworkAvailable(this)) {
            getProducts();
            edt_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    filter(editable.toString(), riderModelList);
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        tv_title.setText("RIDERS");
        iv_back.setOnClickListener(view -> onBackPressed());

    }

    private void filter(String text, List<RiderModel> filtr_list) {
        //new array list that will hold the filtered data
        List<RiderModel> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (RiderModel s : filtr_list) {
            //if the existing elements contains the search input
            if (s.getRiderName().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        if (filterdNames.size() == 0) {
            Toast.makeText(this, "No Record Found", Toast.LENGTH_SHORT).show();
        }
        //calling a method of the adapter class and passing the filtered list
        RiderAdapter.filterList(filterdNames);
    }

    private void getProducts() {

        MyServerRequest myServerRequest = new MyServerRequest(this, URLS.getAllRiders, GET_PROD_REQ, new ServerRequestListener() {
            @Override
            public void onPreResponse() {
                UtilityFunctions.showProgressDialog(RiderActivity.this, true);
            }

            @Override
            public void onPostResponse(JSONObject jsonResponse, int requestCode) {

                UtilityFunctions.hideProgressDialog(true);
                try {
                    if (jsonResponse.getBoolean(AppConstants.HAS_RESPONSE)) {
                        JSONArray jsonArray = jsonResponse.getJSONArray(AppConstants.RESPONSE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            RiderModel riderModel = new RiderModel();
                            riderModel.setRiderId(jsonArray.getJSONObject(i).getString("id"));
                            riderModel.setRiderName(jsonArray.getJSONObject(i).getString("name"));
                            riderModel.setRiderNo(jsonArray.getJSONObject(i).getString("number"));
                            riderModel.setRiderEmail(jsonArray.getJSONObject(i).getString("email"));
                            riderModel.setRiderImage(jsonArray.getJSONObject(i).getString("img"));
                            riderModel.setApproved(jsonArray.getJSONObject(i).getBoolean("isApproved"));
                            riderModelList.add(riderModel);
                        }
                        RiderAdapter = new RiderAdapter(RiderActivity.this, riderModelList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(RiderActivity.this);
                        rv_prod.setLayoutManager(mLayoutManager);
                        rv_prod.setItemAnimator(new DefaultItemAnimator());
                        // Set adapter to recyclerView
                        rv_prod.setAdapter(RiderAdapter);
                    } else {
                        Toast.makeText(RiderActivity.this, " " + jsonResponse.getString(AppConstants.MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        myServerRequest.sendGetRequest();
    }

    private void BindID() {
        include_layout = findViewById(R.id.include_rider);
        iv_back = include_layout.findViewById(R.id.iv_appbar_back_icon);
        tv_title = include_layout.findViewById(R.id.tv_appbar_title);
        iv_back.setVisibility(View.VISIBLE);

//        mSmileRating = (SmileRating) findViewById(R.id.test1);
//        mSmileRating.setOnSmileySelectionListener(this);
//        mSmileRating.setOnRatingSelectedListener(this);
//        mSmileRating.setSelectedSmile(BaseRating.GREAT,true);

    }
}
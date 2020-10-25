package com.mart.admin.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mart.admin.R;
import com.mart.admin.adapter.ProductAdapter;
import com.mart.admin.constants.AppConstants;
import com.mart.admin.constants.URLS;
import com.mart.admin.listeners.ServerRequestListener;
import com.mart.admin.model.ProductModel;
import com.mart.admin.serverReqHelper.MyServerRequest;
import com.mart.admin.utils.UtilityFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductActivity extends AppCompatActivity {
    private static final String TAG = ProductActivity.class.getSimpleName();
    @BindView(R.id.rv_prod_approval)
    RecyclerView rv_prod;
    @BindView(R.id.edt_prod_search)
    EditText edt_search;
    private View include_layout;
    private ImageView iv_back;
    private TextView tv_title;
    private ProductAdapter productAdapter;
    private int GET_PROD_REQ = 10;
    private List<ProductModel> productModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
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
                    filter(editable.toString(), productModelList);
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        tv_title.setText("PRODUCTS");
        iv_back.setOnClickListener(view -> onBackPressed());

    }

    private void filter(String text, List<ProductModel> filtr_list) {
        //new array list that will hold the filtered data
        List<ProductModel> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (ProductModel s : filtr_list) {
            //if the existing elements contains the search input
            if (s.getProd_name().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        if (filterdNames.size() == 0) {
            Toast.makeText(this, "No Record Found", Toast.LENGTH_SHORT).show();
        }
        //calling a method of the adapter class and passing the filtered list
        productAdapter.filterList(filterdNames);
    }

    private void getProducts() {

        MyServerRequest myServerRequest = new MyServerRequest(this, URLS.getAllProducts, GET_PROD_REQ, new ServerRequestListener() {
            @Override
            public void onPreResponse() {
                UtilityFunctions.showProgressDialog(ProductActivity.this, true);
            }

            @Override
            public void onPostResponse(JSONObject jsonResponse, int requestCode) {

                UtilityFunctions.hideProgressDialog(true);
                try {
                    if (jsonResponse.getBoolean(AppConstants.HAS_RESPONSE)) {
                        JSONArray jsonArray = jsonResponse.getJSONArray(AppConstants.RESPONSE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ProductModel productModel = new ProductModel();
                            productModel.setProd_id(jsonArray.getJSONObject(i).getString("id"));
                            productModel.setProd_name(jsonArray.getJSONObject(i).getString("name"));
                            productModel.setProd_image(jsonArray.getJSONObject(i).getString("img"));
                            productModel.setProd_price(jsonArray.getJSONObject(i).getString("price"));
                            productModel.setProd_unit(jsonArray.getJSONObject(i).getString("unit"));
                            productModel.setApproval(jsonArray.getJSONObject(i).getString("isApproved"));
                            productModelList.add(productModel);
                        }
                        productAdapter = new ProductAdapter(ProductActivity.this, productModelList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ProductActivity.this);
                        rv_prod.setLayoutManager(mLayoutManager);
                        rv_prod.setItemAnimator(new DefaultItemAnimator());
                        // Set adapter to recyclerView
                        rv_prod.setAdapter(productAdapter);
                    } else {
                        Toast.makeText(ProductActivity.this, " " + jsonResponse.getString(AppConstants.MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        myServerRequest.sendGetRequest();
    }

    private void BindID() {
        include_layout = findViewById(R.id.include_prod);
        iv_back = include_layout.findViewById(R.id.iv_appbar_back_icon);
        tv_title = include_layout.findViewById(R.id.tv_appbar_title);
        iv_back.setVisibility(View.VISIBLE);

//        mSmileRating = (SmileRating) findViewById(R.id.test1);
//        mSmileRating.setOnSmileySelectionListener(this);
//        mSmileRating.setOnRatingSelectedListener(this);
//        mSmileRating.setSelectedSmile(BaseRating.GREAT,true);

    }
}
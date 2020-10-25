package com.mart.admin.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mart.admin.R;
import com.mart.admin.adapter.OrderAdapter;
import com.mart.admin.constants.AppConstants;
import com.mart.admin.constants.URLS;
import com.mart.admin.listeners.ServerRequestListener;
import com.mart.admin.model.OrderModel;
import com.mart.admin.serverReqHelper.MyServerRequest;
import com.mart.admin.utils.UtilityFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderActivity extends AppCompatActivity {
    private static final String TAG = OrderActivity.class.getSimpleName();
    @BindView(R.id.rv_order_approval)
    RecyclerView rv_order;
    @BindView(R.id.edt_order_search)
    EditText edt_search;
    private View include_layout;
    private OrderAdapter orderAdapter;
    private ImageView iv_back;
    private TextView tv_title;
    private List<OrderModel> orderModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        BindID();
        if (UtilityFunctions.isNetworkAvailable(this)) {
            getOrders();
            edt_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    filter(editable.toString(), orderModelList);
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        tv_title.setText("ORDER");
        iv_back.setOnClickListener(view -> onBackPressed());
    }

    private void getOrders() {
        MyServerRequest myServerRequest = new MyServerRequest(this, URLS.approvedOrder, new ServerRequestListener() {
            @Override
            public void onPreResponse() {
                UtilityFunctions.showProgressDialog(OrderActivity.this, true);
            }

            @Override
            public void onPostResponse(JSONObject jsonResponse, int requestCode) {

                UtilityFunctions.hideProgressDialog(true);
                Log.i(TAG, "onPostResponse: " + jsonResponse);
                try {
                    if (jsonResponse.getBoolean(AppConstants.HAS_RESPONSE)) {
                        JSONArray jsonArray = jsonResponse.getJSONArray(AppConstants.RESPONSE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                                OrderModel orderModel = new OrderModel();
                                orderModel.setOrderID(jsonArray.getJSONObject(i).getString("orderID"));
                                orderModel.setUserName(jsonArray.getJSONObject(i).getString("userName"));
                                orderModel.setUserNumber(jsonArray.getJSONObject(i).getString("userNumber"));
                                orderModel.setRiderName(jsonArray.getJSONObject(i).getString("riderName"));
                                orderModel.setRiderNumber(jsonArray.getJSONObject(i).getString("riderNumber"));
                                orderModel.setShopName(jsonArray.getJSONObject(i).getString("shopName"));
                                orderModel.setShopImg(jsonArray.getJSONObject(i).getString("shopImg"));
                                orderModel.setStatusName(jsonArray.getJSONObject(i).getString("statusName"));
                                orderModel.setTotalPrice(jsonArray.getJSONObject(i).getString("totalPrice"));
                                orderModel.setUserLocation(jsonArray.getJSONObject(i).getString("userLocation"));
                                orderModel.setProductDetails(jsonArray.getJSONObject(i).getString("productDetails"));
                                orderModel.setOrderDate(jsonArray.getJSONObject(i).getString("orderDate"));
                                orderModelList.add(orderModel);

                                    /*
                                    "orderID": 6,
      "userName": "Syed Muhammad Mubashir Ali Zaidi",
      "userNumber": "03423519692",
      "riderName": null,
      "riderNumber": null,
      "riderImg": null,
      "statusName": "Order Sent To The Shop",
      "productDetails": " 2x Supreme Supari , 15x Supreme Supari , 3x Supreme Supari",
      "totalPrice": 900,
      "userLat": 0,
      "userLang": 0,
      "userLocation": "",
      "orderTime": "13:11:03.9207016",
      "orderDate": "2020-10-20T00:00:00"
                                    * */

                        }
                        orderAdapter = new OrderAdapter(OrderActivity.this, orderModelList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(OrderActivity.this);
                        rv_order.setLayoutManager(mLayoutManager);

                        rv_order.setItemAnimator(new DefaultItemAnimator());
                        rv_order.addItemDecoration(
                                new DividerItemDecoration(OrderActivity.this, DividerItemDecoration.VERTICAL));
                        // Set adapter to recyclerView
                        rv_order.setAdapter(orderAdapter);
                    } else {
                        Toast.makeText(OrderActivity.this, " " + jsonResponse.getString(AppConstants.MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    UtilityFunctions.hideProgressDialog(true);
                    e.printStackTrace();
                }
            }
        });
        myServerRequest.sendGetRequest();
    }

    private void filter(String text, List<OrderModel> filtr_list) {
        //new array list that will hold the filtered data
        List<OrderModel> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (OrderModel s : filtr_list) {
            //if the existing elements contains the search input
            if (s.getShopName().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        if (filterdNames.size() == 0) {
            Toast.makeText(this, "No Record Found", Toast.LENGTH_SHORT).show();
        }
        //calling a method of the adapter class and passing the filtered list
        orderAdapter.filterList(filterdNames);
    }

    private void BindID() {
        include_layout = findViewById(R.id.include_order);
        iv_back = include_layout.findViewById(R.id.iv_appbar_back_icon);
        tv_title = include_layout.findViewById(R.id.tv_appbar_title);
        iv_back.setVisibility(View.VISIBLE);

//        mSmileRating = (SmileRating) findViewById(R.id.test1);
//        mSmileRating.setOnSmileySelectionListener(this);
//        mSmileRating.setOnRatingSelectedListener(this);
//        mSmileRating.setSelectedSmile(BaseRating.GREAT,true);

    }
}
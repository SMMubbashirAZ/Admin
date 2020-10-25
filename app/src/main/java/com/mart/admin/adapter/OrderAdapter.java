package com.mart.admin.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mart.admin.R;
import com.mart.admin.constants.AppConstants;
import com.mart.admin.constants.URLS;
import com.mart.admin.listeners.ServerRequestListener;
import com.mart.admin.model.OrderModel;
import com.mart.admin.model.ProductModel;
import com.mart.admin.model.RiderModel;
import com.mart.admin.serverReqHelper.MyServerRequest;
import com.mart.admin.sharedPref.SessionManager;
import com.mart.admin.utils.UtilityFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by WeMartDevelopers .
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {


    private static final String TAG =OrderAdapter.class.getSimpleName() ;
    private Context context;
    private Activity activity;
    private List<OrderModel> orderModelList;
    private List<RiderModel> riderModelList=new ArrayList<>();
    private SessionManager sessionManager;
    private RecyclerView rv_rider;
    private RiderFilterAdapter riderAdapter;

    public OrderAdapter(Context context,  List<OrderModel> orderModelList) {
        this.context = context;
        this.orderModelList = orderModelList;
        sessionManager = SessionManager.getInstance(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_recylerview, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        OrderModel orderModel =orderModelList.get(position);
        if (orderModel.getStatusName().equals(AppConstants.APPROVED_BY_RIDER)){
            holder.acceptOrder.setVisibility(View.GONE);
        }else{
            holder.acceptOrder.setVisibility(View.VISIBLE);
        }
        holder.deliveredTo.setText(MessageFormat.format("Order from {0}",orderModel.getUserName()));
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.main_logo_sub)
                .error(R.drawable.ic_baseline_error_outline_24);

        //holder.imgBrandDeal.setScaleType(ImageView.ScaleType.FIT_XY);

        Glide.with(context).load(URLS.BASEIMGURL+orderModel.getShopImg()).apply(options).into(holder.riderImage);
        Date date;
        String formattedDate = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(orderModel.getOrderDate());
            // format the java.util.Date object to the desired format
            formattedDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.date.setText(formattedDate);
        holder.status.setText(orderModel.getStatusName());
        holder.totalPrice.setText(MessageFormat.format("Rs.{0}",orderModel.getTotalPrice()));
        holder.riderName.setText(orderModel.getShopName());
        holder.orders.setText(orderModel.getProductDetails());
        holder.acceptOrder.setOnClickListener(view -> {
            showRiderDialog(context,position);
        });

    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }


    public void addItem(OrderModel dealModel) {
        orderModelList.add(dealModel);
        notifyDataSetChanged();
    }
    public void filterList(List<OrderModel> filterdNames) {
        this.orderModelList = filterdNames;
        notifyDataSetChanged();
    }
    public void showRiderDialog(Context context, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.rider_assign_dialog, new FrameLayout(context), false);
        final Dialog myDialog = new Dialog(context, R.style.FullDialogTheme);
        myDialog.setContentView(view);

        EditText edt_Search = view.findViewById(R.id.filtersearch_search_edt);
        rv_rider = view.findViewById(R.id.filtersearch_recylarview);

        GetRiderApi(position,myDialog);

        edt_Search.addTextChangedListener(new TextWatcher() {
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

        ImageView btn_cancel =   view.findViewById(R.id.filtersearch_cancel_iv);
        btn_cancel.setOnClickListener(view1 -> myDialog.dismiss());
        myDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        myDialog.getWindow().setDimAmount(0.90f);
        myDialog.getWindow().getDecorView().setBackground(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.show();
    }


    public void GetRiderApi(int position,Dialog myDialog) {
        if (riderModelList.size()>0){
            riderModelList.clear();
        }

//        String url = URLS.assignRider + "/" + orderId+"/"+riderId;
        MyServerRequest myServerRequest = new MyServerRequest(context, URLS.getapprovedRiders, new ServerRequestListener() {
            @Override
            public void onPreResponse() {
                UtilityFunctions.showProgressDialog(context, true);
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
                            riderModel.setRiderEmail(jsonArray.getJSONObject(i).getString("email"));
                            riderModel.setRiderImage(jsonArray.getJSONObject(i).getString("img"));
                            riderModel.setRiderNo(jsonArray.getJSONObject(i).getString("number"));
                            riderModelList.add(riderModel);
                        }
                        riderAdapter = new RiderFilterAdapter(context, riderModelList,orderModelList.get(position).getOrderID(),myDialog);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                        rv_rider.setLayoutManager(mLayoutManager);
                        rv_rider.setItemAnimator(new DefaultItemAnimator());
                        // Set adapter to recyclerView
                        rv_rider.setAdapter(riderAdapter);
                    }else{
                        Toast.makeText(context, " " + jsonResponse.getString(AppConstants.MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                    Log.i(TAG, "onPostResponse: "+jsonResponse.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        myServerRequest.sendGetRequest();
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
            Toast.makeText(context, "No Record Found", Toast.LENGTH_SHORT).show();
        }
        //calling a method of the adapter class and passing the filtered list
        riderAdapter.filterList(filterdNames);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView riderImage;
        private TextView riderName,deliveredTo,date,totalPrice,orders,status;
        private Button acceptOrder;
        private LinearLayout ll_approval;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            riderImage = itemView.findViewById(R.id.hmv_order_history_image);
            riderName = itemView.findViewById(R.id.tv_order_history_rider_name);
            deliveredTo = itemView.findViewById(R.id.tv_order_history_cust_name);
            date = itemView.findViewById(R.id.tv_order_history_date);
            totalPrice = itemView.findViewById(R.id.tv_order_history_price);
            orders = itemView.findViewById(R.id.tv_order_history_order);
            status = itemView.findViewById(R.id.tv_order_history_status);
            acceptOrder = itemView.findViewById(R.id.btn_order_history_accept);
            ll_approval = itemView.findViewById(R.id.ll__order_history_approval);
        }
    }


}

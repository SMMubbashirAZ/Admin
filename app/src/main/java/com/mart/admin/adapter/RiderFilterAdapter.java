package com.mart.admin.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mart.admin.R;
import com.mart.admin.constants.AppConstants;
import com.mart.admin.constants.URLS;
import com.mart.admin.listeners.ServerRequestListener;
import com.mart.admin.model.RiderModel;
import com.mart.admin.serverReqHelper.MyServerRequest;
import com.mart.admin.sharedPref.SessionManager;
import com.mart.admin.utils.UtilityFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by WeMartDevelopers .
 */
public class RiderFilterAdapter extends RecyclerView.Adapter<RiderFilterAdapter.ViewHolder> {


    private static final String TAG =RiderFilterAdapter.class.getSimpleName() ;
    private Context context;
    private Activity activity;
    private String orderId;
    private List<RiderModel> riderModelList;
    private Dialog mainDialog;
    private SessionManager sessionManager;

    public RiderFilterAdapter(Context context, List<RiderModel> riderModelList,String orderid,Dialog myDialog ) {
        this.context = context;
        this.riderModelList = riderModelList;
        this.orderId=orderid;
        this.mainDialog=myDialog;
        sessionManager = SessionManager.getInstance(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assign_rider_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        RiderModel riderModel =riderModelList.get(position);
        holder.riderName.setText(riderModel.getRiderName());
        holder.assignOrder.setOnClickListener(view -> {
            showAssignRiderDialog(context,"Alert","Do you want to assign this Rider",position);
        });
    }

    @Override
    public int getItemCount() {
        return riderModelList.size();
    }


    public void addItem(RiderModel dealModel) {
        riderModelList.add(dealModel);
        notifyDataSetChanged();
    }

    public void filterList(List<RiderModel> filterdNames) {
        this.riderModelList = filterdNames;
        notifyDataSetChanged();
    }

    public void showAssignRiderDialog(Context context, String title, String message, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_dialog_layout, new FrameLayout(context), false);
        final Dialog myDialog = new Dialog(context, R.style.FullDialogTheme);
        myDialog.setContentView(view);

        TextView titleTv = view.findViewById(R.id.custom_dialog_title);
        TextView messageTv = view.findViewById(R.id.custom_dialog_msg);
        Button okBtn = view.findViewById(R.id.custom_dialog_ok_btn);
        Button cancelBtn = view.findViewById(R.id.custom_dialog_cancel_btn);
        Button acceptBtn = view.findViewById(R.id.custom_dialog_accept_btn);
        cancelBtn.setVisibility(View.VISIBLE);
        acceptBtn.setVisibility(View.VISIBLE);
        okBtn.setVisibility(View.GONE);

        titleTv.setText(title);
        messageTv.setText(message);
//
        acceptBtn.setOnClickListener(v -> {
            AssignRider(riderModelList.get(position).getRiderId(),orderId);
            myDialog.dismiss();
        });
        cancelBtn.setOnClickListener(v -> {
            myDialog.dismiss();
        });

        myDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        myDialog.getWindow().setDimAmount(0.90f);
        myDialog.getWindow().getDecorView().setBackground(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.show();
    }


    public void AssignRider(String riderId, String orderId) {

        String url = URLS.assignRider + "/" + orderId+"/"+riderId;
        MyServerRequest myServerRequest = new MyServerRequest(context, url, new ServerRequestListener() {
            @Override
            public void onPreResponse() {
                UtilityFunctions.showProgressDialog(context, true);
            }

            @Override
            public void onPostResponse(JSONObject jsonResponse, int requestCode) {

                UtilityFunctions.hideProgressDialog(true);

                try {
                    if (jsonResponse.getBoolean(AppConstants.HAS_RESPONSE)) {
                        Toast.makeText(context, " " + jsonResponse.getString(AppConstants.RESPONSE), Toast.LENGTH_SHORT).show();
                        mainDialog.dismiss();
//                        productModelList.remove(position);
//                        notifyItemRemoved(position);
//                        notifyItemRangeChanged(position,productModelList.size());
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView riderName;
        private Button assignOrder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            riderName = itemView.findViewById(R.id.tv_rider_name);
            assignOrder = itemView.findViewById(R.id.btn_rider_assign);
        }
    }


}

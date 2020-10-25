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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mart.admin.R;
import com.mart.admin.constants.AppConstants;
import com.mart.admin.constants.URLS;
import com.mart.admin.listeners.ServerRequestListener;
import com.mart.admin.model.RiderModel;
import com.mart.admin.model.RiderModel;
import com.mart.admin.serverReqHelper.MyServerRequest;
import com.mart.admin.sharedPref.SessionManager;
import com.mart.admin.utils.UtilityFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by WeMartDevelopers .
 */
public class RiderAdapter  extends RecyclerView.Adapter<RiderAdapter.ViewHolder> {


    private static final String TAG = RiderAdapter.class.getSimpleName();
    private Context context;
    private Activity activity;
    private List<RiderModel> RiderModelList;
    private SessionManager sessionManager;

    public RiderAdapter(Context context, List<RiderModel> RiderModelList) {
        this.context = context;
        this.RiderModelList = RiderModelList;
        sessionManager = SessionManager.getInstance(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.approved_shop_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        RiderModel model = RiderModelList.get(position);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.main_logo_sub)
                .error(R.drawable.ic_baseline_error_outline_24);

        //holder.imgBrandDeal.setScaleType(ImageView.ScaleType.FIT_XY);

        Glide.with(context).load(URLS.BASEIMGURL + model.getRiderImage()).apply(options).into(holder.shopImge);
//        holder.priceUnit.setText(MessageFormat.format("Rs.{0} /- {1}", model.getProd_price(), model.getProd_unit()));
        holder.tvShopName.setText(model.getRiderName());
        holder.email.setText(model.getRiderEmail());
        holder.no.setText(model.getRiderNo());
        holder.location.setText("");

        if (model.isApproved()) {
            holder.tvNotApproved.setVisibility(View.GONE);
            holder.tvApproved.setVisibility(View.VISIBLE);

        } else {
            holder.tvApproved.setVisibility(View.GONE);
            holder.tvNotApproved.setVisibility(View.VISIBLE);
        }
        holder.tvNotApproved.setOnClickListener(view -> {
            showApprovedDialog(context, "Alert", "Do you want to approve " + model.getRiderName(), position);
        });



    }

    @Override
    public int getItemCount() {
        return RiderModelList.size();
    }


    public void addItem(RiderModel dealModel) {
        RiderModelList.add(dealModel);
        notifyDataSetChanged();
    }

    public void filterList(List<RiderModel> filterdNames) {
        this.RiderModelList = filterdNames;
        notifyDataSetChanged();
    }

    public void showApprovedDialog(Context context, String title, String message, int position) {
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

        acceptBtn.setOnClickListener(v -> {
            ApprovedItem(RiderModelList.get(position).getRiderId(), position);
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


    public void ApprovedItem(String prodId, int position) {

        String url = URLS.approvedRider + "/" + prodId;
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
                        RiderModelList.get(position).setApproved(true);
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, " " + jsonResponse.getString(AppConstants.MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                    Log.i(TAG, "onPostResponse: " + jsonResponse.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        myServerRequest.sendRequest();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView shopImge;
        private TextView tvApproved, tvNotApproved, tvShopName,  email,no,location;
//        private ImageView prodDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shopImge = itemView.findViewById(R.id.civ_shopview_image);
            tvApproved = itemView.findViewById(R.id.tv_shopview_approved);
            tvNotApproved = itemView.findViewById(R.id.tv_shopview_not_approved);
            tvShopName = itemView.findViewById(R.id.tv_shopview_name);
            email = itemView.findViewById(R.id.tv_shopview_email);
            no = itemView.findViewById(R.id.tv_shopview_number);
            location = itemView.findViewById(R.id.tv_shopview_location);
        }
    }
}

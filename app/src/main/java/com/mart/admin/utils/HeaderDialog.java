package com.mart.admin.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mart.admin.R;
import com.mart.admin.listeners.CustomInfoDialogListener;



/**
 * Created by WeMartDevelopers .
 */
public class HeaderDialog {
    public static void showInfoDialog(Context context, String title, String message, CustomInfoDialogListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_dialog_layout, new FrameLayout(context), false);
        final Dialog myDialog = new Dialog(context, R.style.FullDialogTheme);
        myDialog.setContentView(view);

        TextView titleTv = view.findViewById(R.id.custom_dialog_title);
        TextView messageTv = view.findViewById(R.id.custom_dialog_msg);
        Button okBtn = view.findViewById(R.id.custom_dialog_ok_btn);
        Button cancelBtn = view.findViewById(R.id.custom_dialog_cancel_btn);
        Button acceptBtn = view.findViewById(R.id.custom_dialog_accept_btn);
        cancelBtn.setVisibility(View.GONE);
        acceptBtn.setVisibility(View.GONE);
        okBtn.setVisibility(View.VISIBLE);

        titleTv.setText(title);
        messageTv.setText(message);

        okBtn.setOnClickListener(v -> {
            myDialog.dismiss();
//            if (listener != null) {
//                listener.onOkClick();
//            }
        });

        myDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        myDialog.getWindow().setDimAmount(0.90f);
        myDialog.getWindow().getDecorView().setBackground(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.show();
    }

}

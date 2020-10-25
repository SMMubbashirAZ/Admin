package com.mart.admin.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mart.admin.R;
import com.mart.admin.sharedPref.SessionManager;

/**
 * Created by WeMartDevelopers .
 */
public class BaseCompatActivity extends AppCompatActivity {



    public void showDialogue(int layout, String message ,String dialoguType, Intent nextIntent)
    {
        SessionManager sessionManager = SessionManager.getInstance(this);
        View customDialogue = LayoutInflater.from(this).inflate(layout, new FrameLayout(this),false);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(customDialogue);

//        if(dialoguType.equals("flag")){
//            final Button sendBtn = customDialogue.findViewById(R.id.btn_flag_dialogue);
//            sendBtn.setOnClickListener(btn1 -> {
//                dialog.dismiss();
//            });
//        }

        if (dialoguType.equals("alert")){
            TextView cancel = customDialogue.findViewById(R.id.custom_dialog_cancel_btn);
            TextView accept = customDialogue.findViewById(R.id.custom_dialog_accept_btn);
            TextView ok = customDialogue.findViewById(R.id.custom_dialog_ok_btn);

            cancel.setVisibility(View.GONE);
            accept.setVisibility(View.GONE);
            ok.setVisibility(View.VISIBLE);

            ok.setOnClickListener(view1 -> dialog.dismiss());
        }
        if (dialoguType.equals("condition")){
            TextView cancel = customDialogue.findViewById(R.id.custom_dialog_cancel_btn);
            TextView accept = customDialogue.findViewById(R.id.custom_dialog_accept_btn);
            TextView ok = customDialogue.findViewById(R.id.custom_dialog_ok_btn);
            TextView msg = customDialogue.findViewById(R.id.custom_dialog_msg);

            msg.setText(message);
            cancel.setVisibility(View.VISIBLE);
            accept.setVisibility(View.VISIBLE);
            ok.setVisibility(View.GONE);

            cancel.setOnClickListener(view1 -> dialog.dismiss());
            accept.setOnClickListener(view12 -> dialog.dismiss());
        }
        if(dialoguType.equals("logout")){
            final Button confirmBtn = customDialogue.findViewById(R.id.btn_confirm_logout_dialogue);
            final Button cancelBtn = customDialogue.findViewById(R.id.btn_cancel_logout_dialogue);
            confirmBtn.setOnClickListener(btn1 -> {
                sessionManager.clear();
                sessionManager.clearAllPreferences();
                dialog.dismiss();
                startActivity(new Intent(BaseCompatActivity.this,LoginActivity.class));
                finish();
//                BaseCompatActivity.this.finishAffinity();
//                System.exit(0);
            });
            cancelBtn.setOnClickListener(btn2 -> {
                dialog.dismiss();
            });
        }

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setDimAmount(0.90f);
        dialog.getWindow().getDecorView().setBackground(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
    public void hideUI(){
        Runnable hideUI = new Runnable() {
            @Override
            public void run() {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        };
        runOnUiThread(hideUI);
    }
}


package com.mart.admin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.mart.admin.R;
import com.mart.admin.constants.AppConstants;
import com.mart.admin.sharedPref.SessionManager;

public class SplashActivity extends BaseCompatActivity {

    private RelativeLayout main_layout;
    private ImageView logo_iv, shadow_iv;

    private ProgressBar pb;
    private SessionManager sessionManager;
    private int mProgressStatus = 0;
    private Handler mHandler = new Handler();
    Runnable postAnim = new Runnable() {
        @Override
        public void run() {
            while (mProgressStatus < 100) {
                mProgressStatus++;
                android.os.SystemClock.sleep(50);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        pb.setProgress(mProgressStatus);
                    }
                });
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    final Intent t = new Intent(SplashActivity.this, LoginActivity.class);
                    Thread timer = new Thread() {
                        public void run() {
                            try {
                                sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } finally {
                                if (sessionManager.getBoolean(AppConstants.IS_USER_LOGIN))
                                {
                                    startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
                                }
                                else{
                                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                                finish();
                            }
                        }
                    };
                    timer.start();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        hideUI();
        BindingAndInitializingComponents();
        sessionManager=SessionManager.getInstance(this);
        YoYo.with(Techniques.FadeIn)
                .delay(100)
                .duration(1000)
                .repeat(0)
                .onStart(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        main_layout.setVisibility(View.VISIBLE);
                    }
                })
                .onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        new Thread(postAnim).start();
                    }
                })
                .playOn(findViewById(R.id.splash_main_rel));

    }

    private void BindingAndInitializingComponents() {
        main_layout=findViewById(R.id.splash_main_rel);
        logo_iv=findViewById(R.id.splash_logo_iv);
        shadow_iv=findViewById(R.id.splash_shadow_iv);
        pb=findViewById(R.id.splash_progress);
    }


}
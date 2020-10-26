package com.mart.admin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mart.admin.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardActivity extends BaseCompatActivity {
    private View include_layout;
    private ImageView iv_back;
    private TextView tv_title;
    @BindView(R.id.btn_home_shop)
    Button shop_btn;
    @BindView(R.id.btn_home_rider)
    Button rider_btn;
    @BindView(R.id.btn_home_product)
    Button product_btn;
    @BindView(R.id.btn_home_order)
    Button order_btn;
    @BindView(R.id.btn_home_logout)
    Button logout_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        BindID();

        tv_title.setText("ADMIN");
        shop_btn.setOnClickListener(view -> startActivity(new Intent(this,ShopActivity.class)));
        rider_btn.setOnClickListener(view -> startActivity(new Intent(this,RiderActivity.class)));
        product_btn.setOnClickListener(view -> startActivity(new Intent(this,ProductActivity.class)));
        order_btn.setOnClickListener(view -> startActivity(new Intent(this,OrderActivity.class)));
        logout_btn.setOnClickListener(view ->  showDialogue(R.layout.dialogue_logout_layout,"You will be logged out of the application. \\nDo You wish to continue","logout",null));
    }

    private void BindID() {
        include_layout = findViewById(R.id.include_home);
        iv_back = include_layout.findViewById(R.id.iv_appbar_back_icon);
        tv_title = include_layout.findViewById(R.id.tv_appbar_title);
    }
}
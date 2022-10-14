package com.example.poslj.newprojectview;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.LinearLayout;

import com.example.poslj.R;
import com.example.poslj.base.BaseActivity;
import com.example.poslj.homefragment.homemerchants.memerchants.activity.MeMerchantsActivity;

/**
 * 作者: qgl
 * 创建日期：2022/2/28
 * 描述:商户功能选择
 */
public class MeMerchantsSelectActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout iv_back;
    private ConstraintLayout query_merchants;
    private ConstraintLayout new_merchants;
    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.memerchantsselect_activity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        query_merchants = findViewById(R.id.query_merchants);
        new_merchants = findViewById(R.id.new_merchants);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        query_merchants.setOnClickListener(this);
        new_merchants.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.query_merchants:
               startActivity(new Intent(this, MeMerchantsActivity.class));
                break;
            case R.id.new_merchants:
                startActivity(new Intent(this, NewMeQuoteActivity.class));
                break;
        }
    }
}
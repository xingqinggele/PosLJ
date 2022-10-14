package com.example.poslj.homefragment.homequoteactivity;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.example.poslj.R;
import com.example.poslj.base.BaseActivity;
import com.example.poslj.net.HttpRequest;
import com.example.poslj.net.OkHttpException;
import com.example.poslj.net.RequestParams;
import com.example.poslj.net.ResponseCallback;


/**
 * 作者: qgl
 * 创建日期：2022/3/22
 * 描述:报件成功
 */
public class QuoteSucceccActivity extends BaseActivity {
    private Button finls_btn;

    @Override
    protected int getLayoutId() {
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.quote_success_activity;
    }

    @Override
    protected void initView() {
        finls_btn = findViewById(R.id.finls_btn);
        finls_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeQuoteActivity1.instance.finish();
                HomeQuoteActivity2.instance.finish();
                HomeQuoteActivity3.instance.finish();
                finish();
            }
        });
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        if (!TextUtils.isEmpty(getIntent().getStringExtra("id"))) {
            finls_btn.setVisibility(View.VISIBLE);
            getNewOutOperation(getIntent().getStringExtra("id"));
        }

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }


    // 提醒后台进行报件提交
    private void getNewOutOperation(String id){
        RequestParams params = new RequestParams();
        params.put("id",id);
        HttpRequest.getNewOutOperation(params, "",new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {

            }

            @Override
            public void onFailure(OkHttpException failuer) {

            }
        });
    }
}
package com.example.poslj.newprojectview;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.poslj.R;
import com.example.poslj.base.BaseActivity;
import com.example.poslj.utils.StatusBarUtil;
import com.facebook.common.util.UriUtil;

/**
 * 作者: qgl
 * 创建日期：2023/6/21
 * 描述:图片展示
 */
public class PicImageActivity extends BaseActivity implements View.OnClickListener {
    private ImageView pic_iv;
    private LinearLayout iv_back;
    @Override
    protected int getLayoutId() {
        //导航栏设置
        StatusBarUtil.transparencyBar(this);
        return R.layout.pic_image_activity;
    }

    @Override
    protected void initView() {
        pic_iv = findViewById(R.id.pic_iv);
        iv_back = findViewById(R.id.iv_back);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        String pic = getIntent().getStringExtra("pic");
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_FILE_SCHEME)
                .path(pic)
                .build();
        pic_iv.setImageURI(uri);
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
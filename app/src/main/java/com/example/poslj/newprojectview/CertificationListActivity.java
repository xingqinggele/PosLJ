package com.example.poslj.newprojectview;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.poslj.R;
import com.example.poslj.base.BaseActivity;
import com.example.poslj.net.HttpRequest;
import com.example.poslj.net.OkHttpException;
import com.example.poslj.net.RequestParams;
import com.example.poslj.net.ResponseCallback;
import com.example.poslj.newprojectview.adapter.CertificationListAdapter;
import com.example.poslj.newprojectview.bean.CertificationBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2023/4/27
 * 描述:认证银行卡列表
 */
public class CertificationListActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{
    private LinearLayout iv_back;
    private SwipeRefreshLayout swipe_layout;
    private RecyclerView recycler_view;
    private Button nextTo;
    private CertificationListAdapter adapter;
    private List<CertificationBean>mData = new ArrayList<>();
    private String merchCode;
    @Override
    protected int getLayoutId() {
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.certification_list_activity;
    }

    @Override
    protected void initView() {
        merchCode = getIntent().getStringExtra("merchCode");
        iv_back=findViewById(R.id.iv_back);
        swipe_layout=findViewById(R.id.swipe_layout);
        recycler_view=findViewById(R.id.recycler_view);
        nextTo=findViewById(R.id.nextTo);
        initList();
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        nextTo.setOnClickListener(this);
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
            case R.id.nextTo:
                Intent intent = new Intent(CertificationListActivity.this, CertificationAddActivity.class);
                intent.putExtra("merchCode",merchCode);
                startActivity(intent);
                break;
        }
    }

    //适配列表、刷新控件、adapter
    public void initList() {
        //下拉样式
        swipe_layout.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        //上拉刷新初始化
        swipe_layout.setOnRefreshListener(this);
        //adapter配置data
        adapter = new CertificationListAdapter(R.layout.item_certification, mData);
        //打开加载动画
        adapter.openLoadAnimation();
        //设置启用加载更多
        adapter.setEnableLoadMore(false);
        //设置为加载更多监听器
        //adapter.setOnLoadMoreListener(this, recycler_view);
        //数据为空显示xml
        adapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.list_empty, null));
        // RecyclerView设置布局管理器
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        //RecyclerView配置adapter
        recycler_view.setAdapter(adapter);
        //请求接口
        posData();
    }

    //数据请求
    private void posData() {
        RequestParams params = new RequestParams();
        params.put("merchantCode", merchCode);
        HttpRequest.backAccountList(params,getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                swipe_layout.setRefreshing(false);
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    List<CertificationBean> memberList = gson.fromJson(result.getJSONArray("rows").toString(),
                            new TypeToken<List<CertificationBean>>() {
                            }.getType());
                    mData.addAll(memberList);
                    adapter.loadMoreEnd();
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                swipe_layout.setRefreshing(false);
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

    //刷新
    @Override
    public void onRefresh() {
        //开启刷新
        swipe_layout.setRefreshing(true);
        if (mData!=null){
            mData.clear();
        }
        posData();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        onRefresh();
    }
}
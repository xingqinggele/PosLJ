package com.example.poslj.homefragment.homeInvitepartners;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.poslj.R;
import com.example.poslj.base.BaseActivity;
import com.example.poslj.datafragment.databillbean.BillBean;
import com.example.poslj.homefragment.homequoteactivity.bean.MerchTypeBean1;
import com.example.poslj.net.HttpRequest;
import com.example.poslj.net.OkHttpException;
import com.example.poslj.net.RequestParams;
import com.example.poslj.net.ResponseCallback;
import com.example.poslj.views.SwitchButtonView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/8/27
 * 描述:填写生成二维码
 */
public class HomeFillActivity extends BaseActivity implements View.OnClickListener {
    //信用卡结算
    private RelativeLayout js_flt1_relative;
    private TextView js_flt1_tv;
    private String type1 = "";
    //扫码结算
    private RelativeLayout js_flt0_relative;
    private TextView js_flt0_tv;
    private String type2 = "";
    //提交按钮
    private TextView submit_btn;
    //套餐ID
    private String accountId = "";
    //选择商户类型弹出控件
    private OptionsPickerView reasonPicker1;
    private OptionsPickerView reasonPicker2;
    //界面状态、1 新增 2 修改
    private String Status = "1";
    //返回键
    private LinearLayout iv_back;

    private SwitchButtonView mBtnSwitch;
    private String serverSwitch = "0";   //0 关 1开

    //新修改的
    private TextView Tv1;
    private TextView Tv2;
    private TextView Tv3;
    private TextView Tv4;
    private TextView Tv5;
    private TextView Tv6;
    private TextView Tv7;
    private TextView Tv8;
    private TextView Tv9;
    private TextView Tv10;
    private TextView Tv11;
    private TextView Tv12;
    private TextView Tv13;
    private TextView Tv14;
    private TextView Tvv1;
    private TextView Tvv2;
    private TextView Tvv3;
    private TextView Tvv4;
    private TextView Tvv5;
    private TextView Tvv6;
    private TextView Tvv7;
    private TextView Tvv8;
    private TextView Tvv9;
    private TextView Tvv10;
    private TextView Tvv11;
    private TextView Tvv12;
    private TextView Tvv13;
    private TextView Tvv14;
    private EditText ed1;
    private EditText ed2;
    private EditText ed3;
    private EditText ed4;
    private EditText ed5;
    private EditText ed6;
    private EditText ed7;
    private EditText ed8;
    private EditText ed9;
    private EditText ed10;
    private EditText ed11;
    private EditText ed12;
    private EditText ed13;
    private EditText ed14;
    private int sNum1 = 0;
    private int sNum2 = 0;
    private int sNum3 = 0;
    private int sNum4 = 0;
    private int sNum5 = 0;
    private int sNum6 = 0;
    private int sNum7 = 0;
    private int sNum8 = 0;
    private int sNum9 = 0;
    private int sNum10 = 0;
    private int sNum11 = 0;
    private int sNum12 = 0;
    private int sNum13 = 0;
    private int sNum14 = 0;

    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.homefill_activity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        js_flt1_relative = findViewById(R.id.js_flt1_relative);
        js_flt1_tv = findViewById(R.id.js_flt1_tv);
        js_flt0_relative = findViewById(R.id.js_flt0_relative);
        js_flt0_tv = findViewById(R.id.js_flt0_tv);
        submit_btn = findViewById(R.id.submit_btn);
        mBtnSwitch = findViewById(R.id.swith_btn);
        Tv1 = findViewById(R.id.Tv1);
        Tv2 = findViewById(R.id.Tv2);
        Tv3 = findViewById(R.id.Tv3);
        Tv4 = findViewById(R.id.Tv4);
        Tv5 = findViewById(R.id.Tv5);
        Tv6 = findViewById(R.id.Tv6);
        Tv7 = findViewById(R.id.Tv7);
        Tv8 = findViewById(R.id.Tv8);
        Tv9 = findViewById(R.id.Tv9);
        Tv10 = findViewById(R.id.Tv10);
        Tv11 = findViewById(R.id.Tv11);
        Tv12 = findViewById(R.id.Tv12);
        Tv13 = findViewById(R.id.Tv13);
        Tv14 = findViewById(R.id.Tv14);

        Tvv1 = findViewById(R.id.Tvv1);
        Tvv2 = findViewById(R.id.Tvv2);
        Tvv3 = findViewById(R.id.Tvv3);
        Tvv4 = findViewById(R.id.Tvv4);
        Tvv5 = findViewById(R.id.Tvv5);
        Tvv6 = findViewById(R.id.Tvv6);
        Tvv7 = findViewById(R.id.Tvv7);
        Tvv8 = findViewById(R.id.Tvv8);
        Tvv9 = findViewById(R.id.Tvv9);
        Tvv10 = findViewById(R.id.Tvv10);
        Tvv11 = findViewById(R.id.Tvv11);
        Tvv12 = findViewById(R.id.Tvv12);
        Tvv13 = findViewById(R.id.Tvv13);
        Tvv14 = findViewById(R.id.Tvv14);

        ed1 = findViewById(R.id.ed1);
        ed2 = findViewById(R.id.ed2);
        ed3 = findViewById(R.id.ed3);
        ed4 = findViewById(R.id.ed4);
        ed5 = findViewById(R.id.ed5);
        ed6 = findViewById(R.id.ed6);
        ed7 = findViewById(R.id.ed7);
        ed8 = findViewById(R.id.ed8);
        ed9 = findViewById(R.id.ed9);
        ed10 = findViewById(R.id.ed10);
        ed11 = findViewById(R.id.ed11);
        ed12 = findViewById(R.id.ed12);
        ed13 = findViewById(R.id.ed13);
        ed14 = findViewById(R.id.ed14);


        posData();
        newPosData();
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        js_flt1_relative.setOnClickListener(this);
        js_flt0_relative.setOnClickListener(this);
        submit_btn.setOnClickListener(this);
        mBtnSwitch.setmOnCheckedChangeListener(new SwitchButtonView.OnCheckedChangeListener() {
            @Override
            public void OnCheckedChanged(boolean isChecked) {
                if (isChecked) {
                    serverSwitch = "1";
                } else {
                    serverSwitch = "0";
                }
            }
        });

    }

    @Override
    protected void initData() {
        Status = getIntent().getStringExtra("type");
        shouLog("Status", Status);
        if (Status.equals("2")) {
            accountId = getIntent().getStringExtra("accoundId");
            posDept();
        }
    }

    //获取后台类别数据
    private void posData() {
        RequestParams params = new RequestParams();
        params.put("userId", getUserId());
        HttpRequest.getBizTerminalRateList(params, "", new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    JSONObject data = new JSONObject(result.getJSONObject("data").toString());
                    List<FillBean> rateT1list = gson.fromJson(data.getJSONArray("rateT0list").toString(),
                            new TypeToken<List<FillBean>>() {
                            }.getType());
                    List<FillBean> rateT0list = gson.fromJson(data.getJSONArray("settlementQrT0list").toString(),
                            new TypeToken<List<FillBean>>() {
                            }.getType());
                    //结算费率T1
                    initReason1(rateT1list, js_flt1_tv);
                    //结算费率T0
                    initReason2(rateT0list, js_flt0_tv);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

    //获取类目
    private void newPosData() {
        RequestParams params = new RequestParams();
        params.put("userId", getUserId());
        HttpRequest.getEchoServer(params, "", new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    List<HomeNewFilBean> homeNewFilBeans = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<HomeNewFilBean>>() {
                            }.getType());

                    //24
                    Tv1.setText(homeNewFilBeans.get(13).getServerName());
                    Tvv1.setText("(0~" + homeNewFilBeans.get(13).getServerMoney() + ")");
                    sNum1 = Integer.parseInt(homeNewFilBeans.get(13).getServerMoney());
                    //36
                    Tv2.setText(homeNewFilBeans.get(0).getServerName());
                    Tvv2.setText("(0~" + homeNewFilBeans.get(0).getServerMoney() + ")");
                    sNum2 = Integer.parseInt(homeNewFilBeans.get(0).getServerMoney());
                    //48
                    Tv3.setText(homeNewFilBeans.get(10).getServerName());
                    Tvv3.setText("(0~" + homeNewFilBeans.get(10).getServerMoney() + ")");
                    sNum3 = Integer.parseInt(homeNewFilBeans.get(10).getServerMoney());
                    //49
                    Tv4.setText(homeNewFilBeans.get(1).getServerName());
                    Tvv4.setText("(0~" + homeNewFilBeans.get(1).getServerMoney() + ")");
                    sNum4 = Integer.parseInt(homeNewFilBeans.get(1).getServerMoney());
                    //60
                    Tv5.setText(homeNewFilBeans.get(8).getServerName());
                    Tvv5.setText("(0~" + homeNewFilBeans.get(8).getServerMoney() + ")");
                    sNum5 = Integer.parseInt(homeNewFilBeans.get(8).getServerMoney());
                    //99
                    Tv6.setText(homeNewFilBeans.get(2).getServerName());
                    Tvv6.setText("(0~" + homeNewFilBeans.get(2).getServerMoney() + ")");
                    sNum6 = Integer.parseInt(homeNewFilBeans.get(2).getServerMoney());
                    //199
                    Tv7.setText(homeNewFilBeans.get(9).getServerName());
                    Tvv7.setText("(0~" + homeNewFilBeans.get(9).getServerMoney() + ")");
                    sNum7 = Integer.parseInt(homeNewFilBeans.get(9).getServerMoney());
                    //299
                    Tv8.setText(homeNewFilBeans.get(3).getServerName());
                    Tvv8.setText("(0~" + homeNewFilBeans.get(3).getServerMoney() + ")");
                    sNum8 = Integer.parseInt(homeNewFilBeans.get(3).getServerMoney());
                    //399
                    Tv9.setText(homeNewFilBeans.get(12).getServerName());
                    Tvv9.setText("(0~" + homeNewFilBeans.get(12).getServerMoney() + ")");
                    sNum9 = Integer.parseInt(homeNewFilBeans.get(12).getServerMoney());
                    //24
                    Tv10.setText(homeNewFilBeans.get(4).getServerName());
                    Tvv10.setText("(0~" + homeNewFilBeans.get(4).getServerMoney() + ")");
                    sNum10 = Integer.parseInt(homeNewFilBeans.get(4).getServerMoney());
                    //36
                    Tv11.setText(homeNewFilBeans.get(5).getServerName());
                    Tvv11.setText("(0~" + homeNewFilBeans.get(5).getServerMoney() + ")");
                    sNum11 = Integer.parseInt(homeNewFilBeans.get(5).getServerMoney());
                    //48
                    Tv12.setText(homeNewFilBeans.get(6).getServerName());
                    Tvv12.setText("(0~" + homeNewFilBeans.get(6).getServerMoney() + ")");
                    sNum12 = Integer.parseInt(homeNewFilBeans.get(6).getServerMoney());
                    //60
                    Tv13.setText(homeNewFilBeans.get(11).getServerName());
                    Tvv13.setText("(0~" + homeNewFilBeans.get(11).getServerMoney() + ")");
                    sNum13 = Integer.parseInt(homeNewFilBeans.get(11).getServerMoney());
                    //99
                    Tv14.setText(homeNewFilBeans.get(7).getServerName());
                    Tvv14.setText("(0~" + homeNewFilBeans.get(7).getServerMoney() + ")");
                    sNum14 = Integer.parseInt(homeNewFilBeans.get(7).getServerMoney());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }


    //获取已填写的类别数据
    private void posDept() {
        RequestParams params = new RequestParams();
        params.put("accountId", accountId);
        HttpRequest.getBizTerminalRateLists(params, "", new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    JSONObject data = new JSONObject(result.getJSONObject("data").toString());
                    js_flt1_tv.setText(data.getJSONObject("rateT0").getString("name"));
                    type1 = data.getJSONObject("rateT0").getString("id");
                    js_flt0_tv.setText(data.getJSONObject("qrsettleRate").getString("name"));
                    type2 = data.getJSONObject("qrsettleRate").getString("id");
                    ed1.setText(data.getString("serverTwoFour"));
                    ed2.setText(data.getString("serverThirtySix"));
                    ed3.setText(data.getString("serverFortyEight"));
                    ed4.setText(data.getString("serverFortyNine"));
                    ed5.setText(data.getString("serverSixty"));
                    ed6.setText(data.getString("serverNinetyNine"));
                    ed7.setText(data.getString("serverOneNinetyNine"));
                    ed8.setText(data.getString("serverTwoNinetyNine"));
                    ed9.setText(data.getString("serverThreeNinetyNine"));
                    ed10.setText(data.getString("flowTwoFour"));
                    ed11.setText(data.getString("flowThirtySix"));
                    ed12.setText(data.getString("flowFortyEight"));
                    ed13.setText(data.getString("flowSixty"));
                    ed14.setText(data.getString("flowNinetyNine"));
                    serverSwitch = data.getString("serverSwitch");
                    if (data.getString("serverSwitch").equals("0")) {
                        mBtnSwitch.setChecked(false);
                    } else {
                        mBtnSwitch.setChecked(true);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }


    //提交数据
    private void subMit() {
        RequestParams params = new RequestParams();
        params.put("rateT0", type1);
        params.put("qrsettleRate", type2);
        params.put("serverTwoFour", ed1.getText().toString().trim());
        params.put("serverThirtySix", ed2.getText().toString().trim());
        params.put("serverFortyEight", ed3.getText().toString().trim());
        params.put("serverFortyNine", ed4.getText().toString().trim());
        params.put("serverSixty", ed5.getText().toString().trim());
        params.put("serverNinetyNine", ed6.getText().toString().trim());
        params.put("serverOneNinetyNine", ed7.getText().toString().trim());
        params.put("serverTwoNinetyNine", ed8.getText().toString().trim());
        params.put("serverThreeNinetyNine", ed9.getText().toString().trim());
        params.put("flowTwoFour", ed10.getText().toString().trim());
        params.put("flowThirtySix", ed11.getText().toString().trim());
        params.put("flowFortyEight", ed12.getText().toString().trim());
        params.put("flowSixty", ed13.getText().toString().trim());
        params.put("flowNinetyNine", ed14.getText().toString().trim());
        params.put("serverSwitch", serverSwitch);
        HttpRequest.postOpenAccount(params, "", new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    if (result.getString("code").equals("200")) {
                        showToast(3, "添加成功");
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("id", result.getString("data"));
                        setResult(5, resultIntent);
                        finish();
                    } else {
                        showToast(3, "添加失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

    //修改数据
    private void EditData() {
        RequestParams params = new RequestParams();
        params.put("accountId", accountId);
        params.put("rateT0", type1);
        params.put("qrsettleRate", type2);
        params.put("serverTwoFour", ed1.getText().toString().trim());
        params.put("serverThirtySix", ed2.getText().toString().trim());
        params.put("serverFortyEight", ed3.getText().toString().trim());
        params.put("serverFortyNine", ed4.getText().toString().trim());
        params.put("serverSixty", ed5.getText().toString().trim());
        params.put("serverNinetyNine", ed6.getText().toString().trim());
        params.put("serverOneNinetyNine", ed7.getText().toString().trim());
        params.put("serverTwoNinetyNine", ed8.getText().toString().trim());
        params.put("serverThreeNinetyNine", ed9.getText().toString().trim());
        params.put("flowTwoFour", ed10.getText().toString().trim());
        params.put("flowThirtySix", ed11.getText().toString().trim());
        params.put("flowFortyEight", ed12.getText().toString().trim());
        params.put("flowSixty", ed13.getText().toString().trim());
        params.put("flowNinetyNine", ed14.getText().toString().trim());
        params.put("serverSwitch", serverSwitch);
        HttpRequest.putOpenAccount(params, "", new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    if (result.getString("code").equals("200")) {
                        showToast(3, "修改成功");
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("id", result.getString("accountId"));
                        setResult(5, resultIntent);
                        finish();
                    } else {
                        showToast(3, "修改失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }


    //结算费率T1
    private void initReason1(List<FillBean> mList, TextView tv) {
        reasonPicker1 = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //兑换对象赋值
                tv.setText(mList.get(options1).getName());
                type1 = mList.get(options1).getId();
            }
        }).setTitleText("请选择商户类型").setContentTextSize(17).setTitleSize(17).setSubCalSize(16).build();
        reasonPicker1.setPicker(mList);
    }

    //结算费率T0
    private void initReason2(List<FillBean> mList, TextView tv) {
        reasonPicker2 = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //兑换对象赋值
                tv.setText(mList.get(options1).getName());
                type2 = mList.get(options1).getId();
            }
        }).setTitleText("请选择商户类型").setContentTextSize(17).setTitleSize(17).setSubCalSize(16).build();
        reasonPicker2.setPicker(mList);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.js_flt1_relative:
                reasonPicker1.show();
                break;
            case R.id.js_flt0_relative:
                reasonPicker2.show();
                break;
            case R.id.submit_btn:
                if (TextUtils.isEmpty(type1)) {
                    showToast(3, "选择信用卡结算");
                    return;
                }
                if (TextUtils.isEmpty(type2)) {
                    showToast(3, "选择扫码结算");
                    return;
                }

                if (sNum1 < Integer.parseInt(ed1.getText().toString().trim())) {
                    ed1.setError("不能大于基本值");
                    return;
                }
                if (sNum1 < Integer.parseInt(ed1.getText().toString().trim())) {
                    ed1.setError("不能大于基本值");
                    return;
                }
                if (sNum2 < Integer.parseInt(ed2.getText().toString().trim())) {
                    ed2.setError("不能大于基本值");
                    return;
                }
                if (sNum3 < Integer.parseInt(ed3.getText().toString().trim())) {
                    ed3.setError("不能大于基本值");
                    return;
                }
                if (sNum4 < Integer.parseInt(ed4.getText().toString().trim())) {
                    ed4.setError("不能大于基本值");
                    return;
                }
                if (sNum5 < Integer.parseInt(ed5.getText().toString().trim())) {
                    ed5.setError("不能大于基本值");
                    return;
                }
                if (sNum6 < Integer.parseInt(ed6.getText().toString().trim())) {
                    ed6.setError("不能大于基本值");
                    return;
                }
                if (sNum7 < Integer.parseInt(ed7.getText().toString().trim())) {
                    ed7.setError("不能大于基本值");
                    return;
                }
                if (sNum8 < Integer.parseInt(ed8.getText().toString().trim())) {
                    ed8.setError("不能大于基本值");
                    return;
                }
                if (sNum9 < Integer.parseInt(ed9.getText().toString().trim())) {
                    ed9.setError("不能大于基本值");
                    return;
                }
                if (sNum10 < Integer.parseInt(ed10.getText().toString().trim())) {
                    ed10.setError("不能大于基本值");
                    return;
                }
                if (sNum11 < Integer.parseInt(ed11.getText().toString().trim())) {
                    ed11.setError("不能大于基本值");
                    return;
                }
                if (sNum12 < Integer.parseInt(ed12.getText().toString().trim())) {
                    ed12.setError("不能大于基本值");
                    return;
                }
                if (sNum13 < Integer.parseInt(ed13.getText().toString().trim())) {
                    ed13.setError("不能大于基本值");
                    return;
                }
                if (sNum14 < Integer.parseInt(ed14.getText().toString().trim())) {
                    ed14.setError("不能大于基本值");
                    return;
                }

                if (Status.equals("1")) {
                    subMit();
                } else {
                    EditData();
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

}

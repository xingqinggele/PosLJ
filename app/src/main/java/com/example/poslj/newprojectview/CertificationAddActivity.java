package com.example.poslj.newprojectview;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.poslj.R;
import com.example.poslj.base.BaseActivity;
import com.example.poslj.net.HttpRequest;
import com.example.poslj.net.OkHttpException;
import com.example.poslj.net.RequestParams;
import com.example.poslj.net.ResponseCallback;
import com.example.poslj.newprojectview.bean.CerAreaBean;
import com.example.poslj.newprojectview.bean.CerCityBean;
import com.example.poslj.newprojectview.bean.CerProvinceBean;
import com.example.poslj.newprojectview.model.CerProvinceView;
import com.example.poslj.utils.CustomConfigUtil;
import com.example.poslj.utils.TimeUtils;
import com.example.poslj.views.MyDialog1;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tencent.ocr.sdk.common.ISdkOcrEntityResultListener;
import com.tencent.ocr.sdk.common.OcrModeType;
import com.tencent.ocr.sdk.common.OcrSDKConfig;
import com.tencent.ocr.sdk.common.OcrSDKKit;
import com.tencent.ocr.sdk.common.OcrType;
import com.tencent.ocr.sdk.entity.BankCardOcrResult;
import com.tencent.ocr.sdk.entity.IdCardOcrResult;
import com.tencent.ocr.sdk.entity.OcrProcessResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;


/**
 * 作者: qgl
 * 创建日期：2023/4/27
 * 描述:添加银行卡
 */
public class CertificationAddActivity extends BaseActivity implements View.OnClickListener, CerProvinceView.ProvinceListener {
    private ImageView scan_bank_btn;
    private LinearLayout iv_back;
    private EditText cer_bank_num;
    private EditText cer_phone;
    private EditText cer_cvn;
    private TextView province_tv;
    private Button merchant_detail_submit;
    //获取省市区方法
    private CerProvinceView provinceView;
    private String provinceCode = "";
    private String cityCode = "";
    private String areaCode = "";
    private String merchCode;
    private TextView cer_years;
    private String Expired = "";//有效期
    private ImageView image1;
    private ImageView image2;
    private EditText cer_id_name;
    private EditText cer_id_num;
    private ImageView scan_id_btn;
    public static CertificationAddActivity instance  = null;

    @Override
    protected int getLayoutId() {
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.certification_add_activity;
    }

    @Override
    protected void initView() {
        instance  = this;
        //初始化选择省市区
        provinceView = new CerProvinceView(this, this);
        scan_bank_btn = findViewById(R.id.scan_bank_btn);
        iv_back = findViewById(R.id.iv_back);
        cer_bank_num = findViewById(R.id.cer_bank_num);
        province_tv = findViewById(R.id.province_tv);
        merchant_detail_submit = findViewById(R.id.merchant_detail_submit);
        cer_phone = findViewById(R.id.cer_phone);
        cer_cvn = findViewById(R.id.cer_cvn);
        cer_years = findViewById(R.id.cer_years);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        cer_id_name = findViewById(R.id.cer_id_name);
        cer_id_num = findViewById(R.id.cer_id_num);
        scan_id_btn = findViewById(R.id.scan_id_btn);
    }

    @Override
    protected void initListener() {
        scan_bank_btn.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        province_tv.setOnClickListener(this);
        cer_years.setOnClickListener(this);
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        merchant_detail_submit.setOnClickListener(this);
        scan_id_btn.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        merchCode = getIntent().getStringExtra("merchCode");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.scan_bank_btn:
                initSdk(getSecretId(), getSecretKey());
                OcrSDKKit.getInstance().startProcessOcrResultEntity(CertificationAddActivity.this, OcrType.BankCardOCR,
                        CustomConfigUtil.getInstance().getCustomConfigUi(), BankCardOcrResult.class, new ISdkOcrEntityResultListener<BankCardOcrResult>() {
                            @Override
                            public void onProcessSucceed(BankCardOcrResult bankCardOcrResult, OcrProcessResult ocrProcessResult) {
                                //回显银行卡信息
                                getBank_information(bankCardOcrResult);
                            }

                            @Override
                            public void onProcessFailed(String errorCode, String message, OcrProcessResult ocrProcessResult) {
                                popTip(errorCode, message);
                                Log.e("requestId", ocrProcessResult.toString());
                            }

                        });
                break;
            case R.id.province_tv:
                provinceView.showCityPicker();
                break;
            case R.id.merchant_detail_submit:
                if (TextUtils.isEmpty(cer_id_name.getText().toString().trim())) {
                    showToast(3, "请输入姓名！");
                    return;
                }
                if (TextUtils.isEmpty(cer_id_num.getText().toString().trim())) {
                    showToast(3, "请输入身份证号码！");
                    return;
                }
                if (TextUtils.isEmpty(cer_bank_num.getText().toString().trim())) {
                    showToast(3, "请输入卡号！");
                    return;
                }
                if (cer_phone.getText().toString().trim().length() != 11) {
                    showToast(3, "请输入正确手机号！");
                    return;
                }
                if (TextUtils.isEmpty(cer_cvn.getText().toString().trim())) {
                    showToast(3, "请输入CVN号！");
                    return;
                }
                if (TextUtils.isEmpty(Expired)) {
                    showToast(3, "请输入有效期！");
                    return;
                }
                if (TextUtils.isEmpty(provinceCode)) {
                    showToast(3, "请选择省市区！");
                    return;
                }
                postSubmint();
                Intent intent = new Intent(this, CertificationSubActivity.class);
                intent.putExtra("merchCode", merchCode);
                intent.putExtra("userName", cer_id_name.getText().toString().trim());
                intent.putExtra("certificatesNo", cer_id_num.getText().toString().trim());
                intent.putExtra("BankCardNo", cer_bank_num.getText().toString().trim());
                intent.putExtra("PhoneNum", cer_phone.getText().toString().trim());
                intent.putExtra("Cvn", cer_cvn.getText().toString().trim());
                intent.putExtra("Expired", Expired);
                intent.putExtra("provinceCode", provinceCode);
                intent.putExtra("cityCode", cityCode);
                intent.putExtra("areaCode", areaCode);
                startActivity(intent);
                break;
            case R.id.cer_years:
                selectTime(cer_years);
                break;
            case R.id.image1:
                showDialog("1");
                break;
            case R.id.image2:
                showDialog("2");
                break;
            case R.id.scan_id_btn:
                getIDCardIn();
                break;
        }
    }

    private void postSubmint() {
    }

    /**
     * 腾讯银行卡识别初始化
     */
    private void initSdk(String secretId, String secretKey) {
        // 启动参数配置
        OcrType ocrType = OcrType.BankCardOCR; // 设置默认的业务识别，银行卡
        OcrSDKConfig configBuilder = OcrSDKConfig.newBuilder(secretId, secretKey, null)
                .setOcrType(ocrType)
                .setModeType(OcrModeType.OCR_DETECT_MANUAL)
                .build();
        // 初始化SDK
        OcrSDKKit.getInstance().initWithConfig(this.getApplicationContext(), configBuilder);
    }

    /**
     * 获取银行卡信息
     *
     * @param response
     */
    public void getBank_information(BankCardOcrResult response) {
        if (!response.getCardNo().isEmpty()) {
            cer_bank_num.setText(response.getCardNo());
        }
    }


    //获取省市区接口
    @Override
    public void postProvince() {
        RequestParams params = new RequestParams();
        HttpRequest.posGDBProvince(params,getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    List<CerProvinceBean> titleList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<CerProvinceBean>>() {
                            }.getType());
                    provinceView.setTitleData(titleList);

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

    /**
     * 市
     *
     * @param code
     */
    @Override
    public void postCity(String code) {
        RequestParams params = new RequestParams();
        params.put("code", code);
        HttpRequest.posGDBCity(params,getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());

                    List<CerCityBean> titleList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<CerCityBean>>() {
                            }.getType());
                    provinceView.setTwoLabelData(titleList);

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

    /**
     * 区
     *
     * @param code
     */
    @Override
    public void postArea(String code) {
        RequestParams params = new RequestParams();
        params.put("code", code);
        HttpRequest.posGDBArea(params,getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());

                    List<CerAreaBean> titleList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<CerAreaBean>>() {
                            }.getType());
                    provinceView.setAreaLabelData(titleList);
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

    @Override
    public void setSelectAdrCallback(String provinceName, String cityName, String areaName, String provinceCode, String cityCode, String areaCode) {
        province_tv.setText(provinceName + "-" + cityName + "-" + areaName);
        this.provinceCode = provinceCode;
        this.cityCode = cityCode;
        this.areaCode = areaCode;
    }

    /**
     * 选择时间
     */
    private void selectTime(TextView textView) {
        TimePickerView pvTime = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {

                Expired = TimeUtils.getCerTime(date);
                textView.setText(TimeUtils.getCerTime(date));
            }
        }).setType(new boolean[]{true, true, false, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .build();
        pvTime.show();
    }


    /***
     * 筛选
     */
    public void showDialog(String type) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cer_dialog, null);
        ImageView dial_img = view.findViewById(R.id.dial_img);
        if (type.equals("1")){
            dial_img.setImageResource(R.mipmap.yreascer);
        }else {
            dial_img.setImageResource(R.mipmap.cvn);
        }
        Dialog dialog = new MyDialog1(CertificationAddActivity.this, true, true, (float) 1).setNewView(view);
        dialog.show();

    }

    //身份证正面识别
    private void getIDCardIn() {
        initSdk(getSecretId(), getSecretKey());
        //弹出界面
        OcrSDKKit.getInstance().startProcessOcrResultEntity(this, OcrType.IDCardOCR_FRONT, null, IdCardOcrResult.class,
                new ISdkOcrEntityResultListener<IdCardOcrResult>() {
                    @Override
                    public void onProcessSucceed(IdCardOcrResult idCardOcrResult, OcrProcessResult ocrProcessResult) {
                        Log.e("response", idCardOcrResult.toString());
                        setResultListData(idCardOcrResult);
                    }

                    @Override
                    public void onProcessFailed(String errorCode, String message, OcrProcessResult ocrProcessResult) {
                        popTip(errorCode, message);
                        Log.e("requestId", ocrProcessResult.toString());
                    }

                });
    }

    // 配置识别出来的数据
    private void setResultListData(IdCardOcrResult isData) {
        if (isData != null) {
            cer_id_name.setText(isData.getName());
            cer_id_num.setText(isData.getIdNum());
        }
    }
}
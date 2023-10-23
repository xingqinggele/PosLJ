package com.example.poslj.newprojectview;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.poslj.R;
import com.example.poslj.base.BaseActivity;
import com.example.poslj.net.HttpRequest;
import com.example.poslj.net.OkHttpException;
import com.example.poslj.net.RequestParams;
import com.example.poslj.net.ResponseCallback;
import com.example.poslj.utils.CountDownTimerUtils;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.poslj.utils.NumberUtil.isPhoneEnc;

/**
 * 作者: qgl
 * 创建日期：2023/4/28
 * 描述:认证银行卡短信验证接口
 */
public class CertificationSubActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout iv_back;
    private EditText cer_phone;
    private EditText ver_code;
    private TextView ver_code_btn;
    private Button cer_submit_btn;
    private String bankCardNo;
    private String phoneNum;
    private String cvn;
    private String expired;
    private String merchCode;
    private String provinceCode = "";
    private String cityCode = "";
    private String areaCode = "";
    private String mchtOrderNo;
    private String userName;
    private String certificatesNo;
    @Override
    protected int getLayoutId() {
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.certification_sub_activity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        cer_phone = findViewById(R.id.cer_phone);
        ver_code = findViewById(R.id.ver_code);
        ver_code_btn = findViewById(R.id.ver_code_btn);
        cer_submit_btn = findViewById(R.id.cer_submit_btn);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        cer_submit_btn.setOnClickListener(this);
        ver_code_btn.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        merchCode = getIntent().getStringExtra("merchCode");
        userName = getIntent().getStringExtra("userName");
        certificatesNo = getIntent().getStringExtra("certificatesNo");
        bankCardNo = getIntent().getStringExtra("BankCardNo");
        phoneNum = getIntent().getStringExtra("PhoneNum");
        cer_phone.setText(isPhoneEnc(phoneNum));
        cvn = getIntent().getStringExtra("Cvn");
        expired = getIntent().getStringExtra("Expired");
        provinceCode = getIntent().getStringExtra("provinceCode");
        cityCode = getIntent().getStringExtra("cityCode");
        areaCode = getIntent().getStringExtra("areaCode");

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.ver_code_btn:
                subMitData(ver_code_btn);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.cer_submit_btn:
                if (TextUtils.isEmpty(ver_code.getText().toString().trim())){
                    showToast(3, "请输入验证码！");
                    return;
                }
                subCer(ver_code.getText().toString().trim());

                break;
        }
    }

    /**
     * 提交数据获取验证码
     * @param tv
     */
    private void subMitData(TextView tv){
        loadDialog.setTitleStr("加载中...");
        loadDialog.show();
        RequestParams params = new RequestParams();
        params.put("merchantCode", merchCode);
        params.put("userName", userName);
        params.put("certificatesNo", certificatesNo);
        params.put("bankCardNo", bankCardNo);
        params.put("phoneNum", phoneNum);
        params.put("cvn", cvn);
        params.put("expired", expired);
        params.put("province", provinceCode);
        params.put("city", cityCode);
        params.put("area", areaCode);
        HttpRequest.posInfoForGDB(params,getToken(),new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                loadDialog.dismiss();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    JSONObject data = result.getJSONObject("data");
                    mchtOrderNo = data.getString("mchtOrderNo");
                    showToast(3, "短信验证码发送成功");
                    // 开始倒计时 60秒，间隔1秒
                    CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(tv, 60000, 1000);
                    mCountDownTimerUtils.start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(OkHttpException failuer) {
                loadDialog.dismiss();
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

    /**
     * 验证码提交 正式提交认证
     * @param code
     */
    private void subCer(String code){
        loadDialog.setTitleStr("加载中...");
        loadDialog.show();
        RequestParams params = new RequestParams();
        params.put("smsCode", code);
        params.put("bankCardNo", bankCardNo);
        params.put("mchtOrderNo", mchtOrderNo);
        HttpRequest.smsCodeConfirm(params,getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    loadDialog.dismiss();
                    JSONObject result = new JSONObject(responseObj.toString());
                    showToast(3,result.getString("msg"));
                    if (("200").equals(result.getString("code"))){
                        CertificationAddActivity.instance.finish();
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(OkHttpException failuer) {
                loadDialog.dismiss();
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }
}
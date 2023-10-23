package com.example.poslj.newprojectview.editMerchats;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.poslj.R;
import com.example.poslj.base.BaseActivity;
import com.example.poslj.cos.CosServiceFactory;
import com.example.poslj.net.HttpRequest;
import com.example.poslj.net.OkHttpException;
import com.example.poslj.net.RequestParams;
import com.example.poslj.net.ResponseCallback;
import com.example.poslj.newprojectview.adapter.BankNameAdapter;
import com.example.poslj.newprojectview.adapter.ZhiBankNameAdapter;
import com.example.poslj.newprojectview.bean.BankNameBean;
import com.example.poslj.newprojectview.bean.NewCityBean;
import com.example.poslj.newprojectview.bean.NewProvinceBean;
import com.example.poslj.newprojectview.model.EditProvinceView;
import com.example.poslj.utils.CustomConfigUtil;
import com.example.poslj.utils.ImageConvertUtil;
import com.example.poslj.utils.TimeUtils;
import com.example.poslj.utils.Utility;
import com.example.poslj.views.MyListView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.cos.xml.transfer.TransferState;
import com.tencent.cos.xml.transfer.TransferStateListener;
import com.tencent.ocr.sdk.common.ISdkOcrEntityResultListener;
import com.tencent.ocr.sdk.common.OcrModeType;
import com.tencent.ocr.sdk.common.OcrSDKConfig;
import com.tencent.ocr.sdk.common.OcrSDKKit;
import com.tencent.ocr.sdk.common.OcrType;
import com.tencent.ocr.sdk.entity.BankCardOcrResult;
import com.tencent.ocr.sdk.entity.OcrProcessResult;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 作者: qgl
 * 创建日期：2022/8/27
 * 描述:新修改报件
 */
public class EditNewMerchantsActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener{
    private RelativeLayout province_relative;
    private String merchantCode;
    private TextView province_tv;
    private SimpleDraweeView id_card_is;
    private EditText b_number;
    private EditText quote_xy_card_name;
    private EditText quote_xy_phone;
    private EditText bank_code_ed;
    private EditText bankSite_tv;
    private List<BankNameBean> bankNameBeanList = new ArrayList<>();
    private BankNameAdapter bankNameAdapter;
    private MyListView addListView;
    private MyListView zhListView;
    private boolean isBank = true;
    private boolean isBankSite = true;
    private List<BankNameBean> zhiBankList = new ArrayList<>();
    private ZhiBankNameAdapter zhiBankNameAdapter;
    //银行编码
    private String bankCode = "";
    //支行名称
    private String bankSite = "";
    //开户支行联行号
    private String bankSiteCode = "";
    //开户省
    private String BankProvince = "";
    //开户市
    private String BankCity = "";
    private Button submit_bt;
    private String url4 = "";
    private COSXMLUploadTask cosxmlTask;
    private CosXmlService cosXmlService;
    private TransferManager transferManager;
    private int BankCardIn = 1006;
    private PopupWindow popWindow;
    private View popView;
    //银行卡正面图片
    private Bitmap retBitmap1;
    private boolean isEdit = false;
    private String author = "Android";
    private EditProvinceView provinceView;
    private LinearLayout iv_back;
    @Override
    protected int getLayoutId() {
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.edit_new_merchants_activity;
    }

    @Override
    protected void initView() {
        //初始化存储桶控件
        cosXmlService = CosServiceFactory.getCosXmlService(this, "ap-beijing", getSecretId(), getSecretKey(), true);
        TransferConfig transferConfig = new TransferConfig.Builder().build();
        transferManager = new TransferManager(cosXmlService, transferConfig);
        //初始化选择省市区
        provinceView = new EditProvinceView(this, EditNewMerchantsActivity.this);
        id_card_is = findViewById(R.id.id_card_is);
        b_number = findViewById(R.id.b_number);
        quote_xy_card_name = findViewById(R.id.quote_xy_card_name);
        quote_xy_phone = findViewById(R.id.quote_xy_phone);
        bank_code_ed = findViewById(R.id.bank_code_ed);
        bankSite_tv = findViewById(R.id.bankSite_tv);
        addListView = findViewById(R.id.addListView);
        zhListView = findViewById(R.id.zhListView);
        submit_bt = findViewById(R.id.submit_bt);
        province_tv = findViewById(R.id.province_tv);
        province_relative = findViewById(R.id.province_relative);
        iv_back = findViewById(R.id.iv_back);

    }

    @Override
    protected void initListener() {
        submit_bt.setOnClickListener(this);
        id_card_is.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        province_relative.setOnClickListener(this);
        bank_code_ed.addTextChangedListener(textWatcher);
        bankSite_tv.addTextChangedListener(zhiTextWatcher);
        addListView.setOnItemClickListener(this);
        zhListView.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        merchantCode = getIntent().getStringExtra("sid");
        posMerchantInfo(merchantCode);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.submit_bt:
                if (TextUtils.isEmpty(url4)) {
                    showToast(3, "银行卡正面照片");
                    return;
                }
                if (TextUtils.isEmpty(b_number.getText().toString().trim())) {
                    showToast(3, "银行卡号");
                    return;
                }
                if (TextUtils.isEmpty(quote_xy_card_name.getText().toString().trim())) {
                    showToast(3, "开户名");
                    return;
                }
                if (TextUtils.isEmpty(quote_xy_phone.getText().toString().trim())) {
                    showToast(3, "开户手机号");
                    return;
                }
                if (TextUtils.isEmpty(BankProvince)) {
                    showToast(3, "请选择省");
                    return;
                }
                if (TextUtils.isEmpty(BankCity)) {
                    showToast(3, "请选择市");
                    return;
                }
                if (TextUtils.isEmpty(bank_code_ed.getText().toString())) {
                    showToast(3, "银行编码");
                    return;
                }
                if (TextUtils.isEmpty(bankSite)) {
                    showToast(3, "请选择正确开户支行网点");
                    return;
                }
                posData();
                break;
            case R.id.id_card_is:
                showEditPhotoWindow(BankCardIn);
                break;
            case R.id.province_relative:
                provinceView.showCityPicker();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void showEditPhotoWindow(int bankCardIn) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        popView = layoutInflater.inflate(R.layout.popwindow_main, (ViewGroup) null);
        popWindow = new PopupWindow(popView, -1, -2);
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setBackgroundDrawable(new ColorDrawable());
        popWindow.showAtLocation(popView, 80, 0, 0);
        popWindow.setTouchable(true);
        popWindow.setOutsideTouchable(false);
        popWindow.setFocusable(true);
        TextView tv_select_pic = popView.findViewById(R.id.tv_photo);
        TextView tv_pai_pic = popView.findViewById(R.id.tv_photograph);
        TextView tv_cancl = popView.findViewById(R.id.tv_cancle);
        LinearLayout layout = popView.findViewById(R.id.dialog_ll);
        tv_select_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popWindow.isShowing()) {
                    popWindow.dismiss();
                }
                initSdk(getSecretId(), getSecretKey());
                OcrSDKKit.getInstance().startProcessOcrResultEntity(EditNewMerchantsActivity.this, OcrType.BankCardOCR,
                        CustomConfigUtil.getInstance().getCustomConfigUi(), BankCardOcrResult.class,new ISdkOcrEntityResultListener<BankCardOcrResult>() {
                            @Override
                            public void onProcessSucceed(BankCardOcrResult bankCardOcrResult, OcrProcessResult ocrProcessResult) {
                                //回显银行卡信息
                                getBank_information(bankCardOcrResult, ocrProcessResult.imageBase64Str);
                            }

                            @Override
                            public void onProcessFailed(String errorCode, String message, OcrProcessResult ocrProcessResult) {
                                popTip(errorCode, message);
                                Log.e("requestId", ocrProcessResult.toString());
                            }


                        });
            }
        });
        tv_pai_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popWindow.isShowing()) {
                    popWindow.dismiss();
                }
                PictureSelector
                        .create(EditNewMerchantsActivity.this, bankCardIn)
                        .selectPicture(false);
            }
        });
        tv_cancl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popWindow.isShowing()) {
                    popWindow.dismiss();
                }
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popWindow.isShowing()) {
                    popWindow.dismiss();
                }
            }
        });

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
     * @param srcBase64Image
     */
    public void getBank_information(BankCardOcrResult response, String srcBase64Image) {
        try {
            if (!srcBase64Image.isEmpty()) {
                retBitmap1 = ImageConvertUtil.base64ToBitmap(srcBase64Image);
            }
            if (retBitmap1 != null) {
                id_card_is.setImageBitmap(retBitmap1);
                // url4 = ImageConvertUtil.getFile(retBitmap1).getCanonicalPath();
                //isEdit = true;
                upload(ImageConvertUtil.getFile(retBitmap1).getCanonicalPath(),"baojian" + "/" + author + "/" + TimeUtils.getNowTime("day"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!response.getCardNo().isEmpty()) {
            b_number.setText(response.getCardNo());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OcrSDKKit.getInstance().release();

    }



    /**
     * 获取详情信息
     * @param merchantCode
     */
    private void posMerchantInfo(String merchantCode){
        RequestParams params = new RequestParams();
        params.put("merchantCode", merchantCode);
        HttpRequest.posMerchantInfo(params, getToken(), new ResponseCallback() {
            //成功回调
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString()).getJSONObject("data");
                    setValue(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //失败回调
            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

    /**
     * 回显数据
     * @param result
     */
    private void setValue(JSONObject result) {
        try {
            id_card_is.setImageURI(result.getString("bankCardPic"));
            b_number.setText(result.getString("bankCardAccount"));
            quote_xy_card_name.setText(result.getString("bankCardHolder"));
            quote_xy_phone.setText(result.getString("bankCardPhone"));
            url4 = result.getString("bankCardPic");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //银行编码监听
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!TextUtils.isEmpty(bank_code_ed.getText().toString().trim()) && isBank)
                getBanks(bank_code_ed.getText().toString().trim());
        }
    };

    private void getBanks(String bankName) {
        if (TextUtils.isEmpty(bankName)) {
            return;
        }
        RequestParams params = new RequestParams();
        params.put("page", "1");
        params.put("pageSize", "100");
        params.put("bankName", bankName);
        HttpRequest.getBanks(params, getToken(),new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    JSONObject data = result.getJSONObject("data");

                    int totalCount = result.getJSONObject("data").getInt("totalCount");
                    if (totalCount > 0) {
                        bankNameBeanList = gson.fromJson(data.getJSONArray("list").toString(),
                                new TypeToken<List<BankNameBean>>() {
                                }.getType());
                    }
                    bankNameAdapter = new BankNameAdapter(EditNewMerchantsActivity.this, bankNameBeanList);
                    bankNameAdapter.notifyDataSetChanged();
                    addListView.setAdapter(bankNameAdapter);

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

    //银行支行
    TextWatcher zhiTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!TextUtils.isEmpty(bankSite_tv.getText().toString().trim()) && isBankSite)
                getBranchs(bankSite_tv.getText().toString().trim());
        }
    };

    private void getBranchs(String bankSiteName) {
        RequestParams params = new RequestParams();
        params.put("page", "1");
        params.put("pageSize", "100");
        params.put("provinceCode", BankProvince);
        params.put("cityCode", BankCity);
        params.put("bankCode", bankCode);
        params.put("bankSiteName", bankSiteName);
        HttpRequest.getBranchs(params, getToken(),new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    JSONObject data = result.getJSONObject("data");
                    int totalCount = result.getJSONObject("data").getInt("totalCount");
                    if (totalCount > 0) {
                        zhiBankList = gson.fromJson(data.getJSONArray("list").toString(),
                                new TypeToken<List<BankNameBean>>() {
                                }.getType());
                    }
                    zhiBankNameAdapter = new ZhiBankNameAdapter(EditNewMerchantsActivity.this, zhiBankList);
                    zhiBankNameAdapter.notifyDataSetChanged();
                    zhListView.setAdapter(zhiBankNameAdapter);

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


    //上传图片
    private void upload(String file_url, String folderName) {
        if (TextUtils.isEmpty(file_url)) {
            return;
        }
        if (cosxmlTask == null) {
            File file = new File(file_url);
            String cosPath;
            if (TextUtils.isEmpty(folderName)) {
                cosPath = file.getName();
            } else {
                cosPath = folderName + File.separator + file.getName();
            }
            cosxmlTask = transferManager.upload(getBucketName(), cosPath, file_url, null);
            Log.e("参数-------》", getBucketName() + "----" + cosPath + "---" + file_url);
            cosxmlTask.setTransferStateListener(new TransferStateListener() {
                @Override
                public void onStateChanged(final TransferState state) {
                    // refreshUploadState(state);
                }
            });
            cosxmlTask.setCosXmlProgressListener(new CosXmlProgressListener() {
                @Override
                public void onProgress(final long complete, final long target) {
                    // refreshUploadProgress(complete, target);
                }
            });

            cosxmlTask.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                    COSXMLUploadTask.COSXMLUploadTaskResult cOSXMLUploadTaskResult = (COSXMLUploadTask.COSXMLUploadTaskResult) result;
                    isEdit = false;
                    cosxmlTask = null;
                    setResult(RESULT_OK);
                    url4 = cOSXMLUploadTaskResult.accessUrl;
                    shouLog("--------->",url4);
                    //posData();
                }

                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                    if (cosxmlTask.getTaskState() != TransferState.PAUSED) {
                        cosxmlTask = null;
                        Log.e("1111", "上传失败");
                    }
                    exception.printStackTrace();
                    serviceException.printStackTrace();
                }
            });

        }
    }

    //发送修改数据
    private void posData() {
        loadDialog.show();
        RequestParams params = new RequestParams();
        //商户号
        params.put("merchantCode", merchantCode);
        //银行账号
        params.put("bankCardAccount", b_number.getText().toString().trim());
        //开户名
        params.put("bankCardHolder", quote_xy_card_name.getText().toString().trim());
        //银行卡预留手机号
        params.put("bankCardPhone", quote_xy_phone.getText().toString().trim());
        //银行卡照片
        params.put("bankCardPic", url4);
        //开户银行省份
        params.put("bankProvince", BankProvince);
        //开户银行城市
        params.put("bankCity", BankCity);
        //银行编码
        params.put("bankCode", bankCode);
        //开户行网点
        params.put("bankSite", bankSite);
        //开户支行联行号
        params.put("unionpayCode", bankSiteCode);
        HttpRequest.posUpdateSettleAccount(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                if (loadDialog.isShowing()){
                    loadDialog.dismiss();
                }
                showToast(3,"修改成功，等待审核！");
                finish();
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                loadDialog.dismiss();
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

    /**
     * 请求省市区
     *
     * @param areaLevel  级别 1、2、3
     * @param parentCode 父类的code
     * @param type       请求的界别 1、2、3
     */
    public void postProvince(String areaLevel, String parentCode, int type) {
        RequestParams params = new RequestParams();
        params.put("areaLevel", areaLevel);
        params.put("parentCode", parentCode);
        HttpRequest.getArea(params,getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    if (type == 0) {
                        List<NewProvinceBean> titleList = gson.fromJson(result.getJSONArray("data").toString(),
                                new TypeToken<List<NewProvinceBean>>() {
                                }.getType());
                        provinceView.setTitleData(titleList);
                    } else if (type == 1) {
                        List<NewCityBean> titleList = gson.fromJson(result.getJSONArray("data").toString(),
                                new TypeToken<List<NewCityBean>>() {
                                }.getType());
                        provinceView.setTwoLabelData(titleList);
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

    /**
     * 省市区回调接口
     *
     * @param provinceTv   选择的省市区text
     * @param provinceCode
     * @param cityCode
     */
    public void setSelectAdrCallback(String provinceTv, String provinceCode, String cityCode) {
        province_tv.setText(provinceTv);
        BankProvince = provinceCode;
        BankCity = cityCode;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.addListView:
                isBank = false;
                bank_code_ed.setText(bankNameBeanList.get(i).getName());
                bankCode = bankNameBeanList.get(i).getCode();
                bankNameAdapter.clear();
                isBank = true;
                break;
            case R.id.zhListView:
                isBankSite = false;
                bankSite_tv.setText(zhiBankList.get(i).getName());
                bankSite = zhiBankList.get(i).getName();
                bankSiteCode = zhiBankList.get(i).getCode();
                zhiBankNameAdapter.clear();
                isBankSite = true;
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BankCardIn) {
            if (data != null){
                PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
                Luban.with(this)
                        .load(pictureBean.getPath())
                        .ignoreBy(100)
                        .setTargetDir(Utility.getPath())
                        .filter(new CompressionPredicate() {
                            @Override
                            public boolean apply(String path) {
                                return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                            }
                        })
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(File file) {
                                // url4 = file.getPath();
                                id_card_is.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                                //isEdit = true;
                                upload(file.getPath(),"baojian" + "/" + author + "/" + TimeUtils.getNowTime("day"));

                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        }).launch();
            }

        }
    }
}
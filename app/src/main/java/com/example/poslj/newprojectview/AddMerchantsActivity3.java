package com.example.poslj.newprojectview;

import android.annotation.SuppressLint;
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

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.poslj.R;
import com.example.poslj.base.BaseActivity;
import com.example.poslj.bean.BankCardInfo;
import com.example.poslj.cos.CosServiceFactory;
import com.example.poslj.homefragment.homemerchants.homenewmerchants.merchantstype.bean.SmallinformationBean;
import com.example.poslj.net.HttpRequest;
import com.example.poslj.net.OkHttpException;
import com.example.poslj.net.RequestParams;
import com.example.poslj.net.ResponseCallback;
import com.example.poslj.newprojectview.adapter.BankNameAdapter;
import com.example.poslj.newprojectview.adapter.ZhiBankNameAdapter;
import com.example.poslj.newprojectview.bean.BankNameBean;
import com.example.poslj.newprojectview.bean.NewCityBean;
import com.example.poslj.newprojectview.bean.NewProvinceBean;
import com.example.poslj.newprojectview.model.ProvinceView;
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
import com.tencent.ocr.sdk.common.ISDKKitResultListener;
import com.tencent.ocr.sdk.common.OcrModeType;
import com.tencent.ocr.sdk.common.OcrSDKConfig;
import com.tencent.ocr.sdk.common.OcrSDKKit;
import com.tencent.ocr.sdk.common.OcrType;
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
 * ??????: qgl
 * ???????????????2021/8/17
 * ??????: posP????????????3
 */
public class AddMerchantsActivity3 extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private LinearLayout iv_back;
    // ????????????
    public static AddMerchantsActivity3 instance = null;
    //???????????????
    private SimpleDraweeView id_card_is;
    //?????????????????????
    private Bitmap retBitmap1;
    //???????????????????????????
    private String bUrl1 = "";
    private String url4 = "";
    //????????????
    private EditText bNumber;
    //??????????????????
    private Button submit_bt;
    //?????????
    private EditText quote_xy_card_name;
    //?????????????????????
    private EditText quote_xy_phone;
    //????????????
    private String bankCode = "";
    //????????????
    private String bankSite = "";
    //?????????????????????
    private String bankSiteCode = "";
    //?????????
    private String BankProvince = "";
    //?????????
    private String BankCity = "";
    //????????????
    private EditText bankSite_tv;
    /************* ????????? ****************/

    //???????????????
    private String region = "ap-beijing";
    private String folderName = "";
    private String author = "Android";
    private CosXmlService cosXmlService;
    private TransferManager transferManager;
    private COSXMLUploadTask cosxmlTask;
    private int BankCardIn = 1006;

    /************* ????????? ****************/

    /************* ??????  ****************/

    private PopupWindow popWindow;
    private View popView;
    private RelativeLayout province_relative;
    private ProvinceView provinceView;
    private TextView province_tv;
    private EditText bank_code_ed;
    private MyListView addListView;
    private MyListView zhListView;
    private List<BankNameBean> bankNameBeanList = new ArrayList<>();
    private List<BankNameBean> zhiBankList = new ArrayList<>();
    private BankNameAdapter bankNameAdapter;
    private ZhiBankNameAdapter zhiBankNameAdapter;

    /************* ??????  ****************/



    /**************  ????????????  ****************/
    private String applicant;
    private String contactPhoneNo;
    private String clientServicePhoneNo;
    private String merchantShortHand;
    private String rateId;
    private String PosCode;
    private String provinceNo;
    private String cityNo;
    private String areaNo;
    private String address;
    private String certificateEndDate;
    private String certificateStartDate;
    private String certificateNo;
    private String certificatesBackPic;
    private String url1 ;
    private String url2 ;
    private String url3 ;
    private String certificatesFrontPic;
    private String idcardHandPic;
    private String legalPersonName;

    /**************  ????????????  ****************/
    private boolean isBank = true;
    private boolean isBankSite = true;
    //??????
    private String Longitude = "";
    //??????
    private String Latitude = "";
    private String provinceName;  // ?????????
    private String cityName;  // ?????????
    private String areaName;  // ?????????
    @Override
    protected int getLayoutId() {
        //?????????????????????
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.addmerchants_activity_03;
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void initView() {
        instance = this;
        // ????????????????????????
        cosXmlService = CosServiceFactory.getCosXmlService(this, region, getSecretId(), getSecretKey(), true);
        TransferConfig transferConfig = new TransferConfig.Builder().build();
        transferManager = new TransferManager(cosXmlService, transferConfig);
        //????????????????????????
        provinceView = new ProvinceView(this, AddMerchantsActivity3.this);
        id_card_is = findViewById(R.id.id_card_is);
        bNumber = findViewById(R.id.b_number);
        submit_bt = findViewById(R.id.submit_bt);
        iv_back = findViewById(R.id.iv_back);
        province_relative = findViewById(R.id.province_relative);
        province_tv = findViewById(R.id.province_tv);
        bank_code_ed = findViewById(R.id.bank_code_ed);
        addListView = findViewById(R.id.addListView);
        zhListView = findViewById(R.id.zhListView);
        bankSite_tv = findViewById(R.id.bankSite_tv);
        quote_xy_card_name = findViewById(R.id.quote_xy_card_name);
        quote_xy_phone = findViewById(R.id.quote_xy_phone);

    }




    @Override
    protected void initListener() {
        id_card_is.setOnClickListener(this);
        submit_bt.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        province_relative.setOnClickListener(this);
        bank_code_ed.addTextChangedListener(textWatcher);
        bankSite_tv.addTextChangedListener(zhiTextWatcher);
        addListView.setOnItemClickListener(this);
        zhListView.setOnItemClickListener(this);

    }

    @Override
    protected void initData() {
        applicant = getIntent().getStringExtra("quote_contact_name");
        merchantShortHand = getIntent().getStringExtra("quote_shop_jname");
        contactPhoneNo = getIntent().getStringExtra("quote_service_phone");
        rateId = getIntent().getStringExtra("rateId");
        PosCode = getIntent().getStringExtra("PosCode");
        address = getIntent().getStringExtra("quote_address");
        clientServicePhoneNo = getIntent().getStringExtra("quote_phone");
        provinceNo = getIntent().getStringExtra("province");
        cityNo = getIntent().getStringExtra("city");
        areaNo = getIntent().getStringExtra("area");
        url1 = getIntent().getStringExtra("IdUrl1");
        url2 = getIntent().getStringExtra("IdUrl2");
        url3 = getIntent().getStringExtra("IdUrl3");
        legalPersonName = getIntent().getStringExtra("fName");
        certificateNo = getIntent().getStringExtra("fNumber");
        certificateStartDate = getIntent().getStringExtra("startTime");
        certificateEndDate = getIntent().getStringExtra("endTime");

        Longitude = getIntent().getStringExtra("Longitude");
        Latitude = getIntent().getStringExtra("Latitude");
        provinceName = getIntent().getStringExtra("provinceName");
        cityName = getIntent().getStringExtra("cityName");
        areaName = getIntent().getStringExtra("areaName");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_card_is:
                showEditPhotoWindow(BankCardIn);

                break;
            case R.id.id_card_the:
                PictureSelector
                        .create(AddMerchantsActivity3.this, PictureSelector.SELECT_REQUEST_CODE)
                        .selectPicture(false);
                break;
            case R.id.submit_bt:
                if (TextUtils.isEmpty(url4)) {
                    showToast(3, "?????????????????????");
                    return;
                }
                if (TextUtils.isEmpty(bNumber.getText().toString().trim())) {
                    showToast(3, "????????????");
                    return;
                }
                if (TextUtils.isEmpty(quote_xy_card_name.getText().toString().trim())) {
                    showToast(3, "?????????");
                    return;
                }
                if (TextUtils.isEmpty(quote_xy_phone.getText().toString().trim())) {
                    showToast(3, "???????????????");
                    return;
                }
                if (TextUtils.isEmpty(BankProvince)) {
                    showToast(3, "????????????");
                    return;
                }
                if (TextUtils.isEmpty(BankCity)) {
                    showToast(3, "????????????");
                    return;
                }
                if (TextUtils.isEmpty(bankCode)) {
                    showToast(3, "???????????????????????????");
                    return;
                }

                if (TextUtils.isEmpty(bankSite)) {
                    showToast(3, "?????????????????????????????????");
                    return;
                }

                loadDialog.show();
                folderName = "baojian" + "/" + author + "/" + certificateNo + "/" + TimeUtils.getNowTime("day");
                List<SmallinformationBean> beans = new ArrayList<>();
                // ???????????????
                SmallinformationBean bean1 = new SmallinformationBean();
                bean1.setName("1");
                bean1.setUrl(url1);
                beans.add(bean1);
                SmallinformationBean bean2 = new SmallinformationBean();
                bean2.setName("2");
                bean2.setUrl(url2);
                beans.add(bean2);
                SmallinformationBean bean3 = new SmallinformationBean();
                bean3.setName("3");
                bean3.setUrl(url3);
                beans.add(bean3);
                SmallinformationBean bean4 = new SmallinformationBean();
                bean4.setName("4");
                bean4.setUrl(url4);
                beans.add(bean4);
                if (beans.size() < 1) {
                    posData();
                } else {
                    Log.e("------???", "??????????????????");
                    for (int i = 0; i < beans.size(); i++) {
                        upload(beans, folderName);
                    }
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.province_relative:
                provinceView.showCityPicker();
                break;

        }
    }


    /**
     * ????????????,????????????
     */
    private void upload(List<SmallinformationBean> list, String newfolderName) {
        int i = 0;
        if (TextUtils.isEmpty(list.get(i).getUrl())) {
            list.remove(i);
            return;
        }
        if (cosxmlTask == null) {
            File file = new File(list.get(i).getUrl());
            String cosPath;
            if (TextUtils.isEmpty(newfolderName)) {
                cosPath = file.getName();
            } else {
                cosPath = newfolderName + File.separator + file.getName();
            }
            cosxmlTask = transferManager.upload(getBucketName(), cosPath, list.get(i).getUrl(), null);
            Log.e("??????-------???", getBucketName() + "----" + cosPath + "---" + list.get(i).getUrl());

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
                    cosxmlTask = null;
                    Log.e("1111", "??????");
                    if (list.get(i).getName().equals("1")) {
                        certificatesBackPic = cOSXMLUploadTaskResult.accessUrl;
                        Log.e("???????????????", cOSXMLUploadTaskResult.accessUrl);
                    } else if (list.get(i).getName().equals("2")) {
                        certificatesFrontPic = cOSXMLUploadTaskResult.accessUrl;
                        Log.e("???????????????", cOSXMLUploadTaskResult.accessUrl);
                    } else if (list.get(i).getName().equals("3")) {
                        idcardHandPic = cOSXMLUploadTaskResult.accessUrl;
                        Log.e("???????????????", cOSXMLUploadTaskResult.accessUrl);
                    } else if (list.get(i).getName().equals("4")) {
                        bUrl1 = cOSXMLUploadTaskResult.accessUrl;
                        Log.e("???????????????", cOSXMLUploadTaskResult.accessUrl);
                    }
                    setResult(RESULT_OK);
                    list.remove(i);
                    if (list.size() < 1) {
                        Log.e("----------???", "???????????????????????????");
                        posData();
                    } else {
                        upload(list, newfolderName);
                    }
                }

                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                    if (cosxmlTask.getTaskState() != TransferState.PAUSED) {
                        cosxmlTask = null;
                        Log.e("1111", "????????????");
                        loadDialog.dismiss();
                        showToast(2, "??????????????????????????????????????????");
                    }
                    exception.printStackTrace();
                    serviceException.printStackTrace();
                }
            });

        }
    }


    /**
     * ??????
     */
    private void posData() {
        RequestParams params = new RequestParams();
        //?????????
        params.put("applicant", applicant);
        //????????????
        params.put("contactPhoneNo", contactPhoneNo);
        //????????????
        params.put("clientServicePhoneNo", clientServicePhoneNo);
        //??????ID
        params.put("feeId", rateId);
        //SN
        params.put("posCode", PosCode);
        //????????????
        params.put("merchantShortHand", merchantShortHand);
        //????????????
        params.put("provinceNo", provinceNo);
        //????????????
        params.put("cityNo", cityNo);
        //????????????
        params.put("areaNo", areaNo);
        //????????????
        params.put("address", address);
        //????????????????????????????????????1949-10-01???;
        params.put("certificateEndDate", certificateEndDate);
        //??????????????????
        params.put("certificateNo", certificateNo);
        //????????????????????????????????????1949-10-01???
        params.put("certificateStartDate", certificateStartDate);
        //???????????????
        params.put("certificatesBackPic", certificatesFrontPic);
        //???????????????
        params.put("certificatesFrontPic",certificatesBackPic);
        //?????????????????????
        params.put("idcardHandPic", idcardHandPic);
        //????????????
        params.put("legalPersonName", legalPersonName);
        //????????????
        params.put("bankCardAccount", bNumber.getText().toString().trim());
        //?????????
        params.put("bankCardHolder", quote_xy_card_name.getText().toString().trim());
        //????????????????????????
        params.put("bankCardPhone", quote_xy_phone.getText().toString().trim());
        //???????????????
        params.put("bankCardPic", bUrl1);
        //??????????????????
        params.put("bankProvince", BankProvince);
        //??????????????????
        params.put("bankCity", BankCity);
        //????????????
        params.put("bankCode", bankCode);
        //???????????????
        params.put("bankSite", bankSite);
        //?????????????????????
        params.put("unionpayCode", bankSiteCode);
        //?????????
        params.put("phone", getUserName());
        //????????????????????????
        params.put("activeAddress", address);
        //??????????????????????????????
        params.put("activeLatitude", Latitude);
        //??????????????????????????????
        params.put("activeLongitude", Longitude);
        //?????????
        params.put("provinceName", provinceName);
        //?????????
        params.put("cityName", cityName);
        //?????????
        params.put("areaName", areaName);
        shouLog("????????????-------------->", params.toString());
        HttpRequest.getPosPOperation(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                loadDialog.dismiss();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                     showToast(2, result.getString("msg") + "");
                     AddMerchantsActivity1.instance.finish();
                     AddMerchantsActivity2.instance.finish();
                     finish();
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

    /************************************** ?????????????????? ***********************************************************************/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BankCardIn) {
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
                            url4 = file.getPath();
                            id_card_is.setImageBitmap(BitmapFactory.decodeFile(url4));
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    }).launch();
        }
    }

    /************************************** ?????????????????? ***********************************************************************/

    /**
     * ??????????????????????????????
     */
    private void initSdk(String secretId, String secretKey) {
        // ??????????????????
        OcrType ocrType = OcrType.BankCardOCR; // ???????????????????????????????????????
        OcrSDKConfig configBuilder = OcrSDKConfig.newBuilder(secretId, secretKey, null)
                .OcrType(ocrType)
                .ModeType(OcrModeType.OCR_DETECT_MANUAL)
                .build();
        // ?????????SDK
        OcrSDKKit.getInstance().initWithConfig(this.getApplicationContext(), configBuilder);
    }

    /**
     * ?????????????????????
     *
     * @param response
     * @param srcBase64Image
     */
    public void getBank_information(String response, String srcBase64Image) {
        try {
            if (!srcBase64Image.isEmpty()) {
                retBitmap1 = ImageConvertUtil.base64ToBitmap(srcBase64Image);
            }
            if (retBitmap1 != null) {
                id_card_is.setImageBitmap(retBitmap1);
                url4 = ImageConvertUtil.getFile(retBitmap1).getCanonicalPath();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!response.isEmpty()) {
            final BankCardInfo bankCardInfo = new Gson().fromJson(response, BankCardInfo.class);
            bNumber.setText(bankCardInfo.getCardNo());
        }
    }

    /**
     * ?????????
     *
     * @param value
     */
    private void showEditPhotoWindow(int value) {
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
                OcrSDKKit.getInstance().startProcessOcr(AddMerchantsActivity3.this, OcrType.BankCardOCR,
                        CustomConfigUtil.getInstance().getCustomConfigUi(), new ISDKKitResultListener() {
                            @Override
                            public void onProcessSucceed(String response, String srcBase64Image, String requestId) {
                                //?????????????????????
                                getBank_information(response, srcBase64Image);
                            }

                            @Override
                            public void onProcessFailed(String errorCode, String message, String requestId) {
                                popTip(errorCode, message);
                                Log.e("requestId", requestId);
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
                        .create(AddMerchantsActivity3.this, value)
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
     * ???????????????
     *
     * @param areaLevel  ?????? 1???2???3
     * @param parentCode ?????????code
     * @param type       ??????????????? 1???2???3
     */
    public void postProvince(String areaLevel, String parentCode, int type) {
        RequestParams params = new RequestParams();
        params.put("areaLevel", areaLevel);
        params.put("parentCode", parentCode);
        HttpRequest.getArea(params,getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //???????????????????????????
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
     * ?????????????????????
     *
     * @param provinceTv   ??????????????????text
     * @param provinceCode
     * @param cityCode
     */
    public void setSelectAdrCallback(String provinceTv, String provinceCode, String cityCode) {
        province_tv.setText(provinceTv);
        BankProvince = provinceCode;
        BankCity = cityCode;
    }

    private void getBanks(String bankName) {
        if (TextUtils.isEmpty(bankName)) {
            return;
        }
        RequestParams params = new RequestParams();
        params.put("page", "1");
        params.put("pageSize", "100");
        params.put("bankName", bankName);
        HttpRequest.getBanks(params,getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //???????????????????????????
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

                    bankNameAdapter = new BankNameAdapter(AddMerchantsActivity3.this, bankNameBeanList);
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

    //??????????????????
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

    //????????????
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
                //???????????????????????????
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
                    zhiBankNameAdapter = new ZhiBankNameAdapter(AddMerchantsActivity3.this, zhiBankList);
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


}

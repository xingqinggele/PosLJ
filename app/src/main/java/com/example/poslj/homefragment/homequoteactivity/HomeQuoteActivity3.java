package com.example.poslj.homefragment.homequoteactivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.poslj.R;
import com.example.poslj.base.BaseActivity;
import com.example.poslj.bean.BankCardInfo;
import com.example.poslj.cos.CosServiceFactory;
import com.example.poslj.homefragment.homemerchants.homenewmerchants.merchantstype.bean.SmallinformationBean;
import com.example.poslj.net.HttpRequest;
import com.example.poslj.net.OkHttpException;
import com.example.poslj.net.RequestParams;
import com.example.poslj.net.ResponseCallback;
import com.example.poslj.utils.CustomConfigUtil;
import com.example.poslj.utils.ImageConvertUtil;
import com.example.poslj.utils.TimeUtils;
import com.example.poslj.utils.Utility;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
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
 * ??????: ????????????3
 */
public class HomeQuoteActivity3 extends BaseActivity implements View.OnClickListener {
    //???????????????
    private SimpleDraweeView id_card_is;
    //?????????????????????
    private Bitmap retBitmap1;
    //???????????????????????????
    private String bUrl1 = "";
    //???????????????
    private SimpleDraweeView id_card_the;
    //???????????????????????????
    private String bUrl2 = "";
    //????????????
    private EditText bNumber;
    /*******?????????????????????************/
    private String snCode = ""; //??????SN???
    private String uPhone = ""; //????????????
    private String fType = ""; //????????????
    private String province = ""; //??????????????????
    private String city = ""; //??????????????????
    private String area = ""; //??????????????????
    /*******?????????????????????************/
    /*******?????????????????????************/
    private String IdUrl1 = ""; //?????????????????????
    private String IdUrl2 = ""; //?????????????????????
    private String IdUrl3 = ""; //?????????????????????
    private String fName = ""; //????????????
    private String fNumber = ""; //?????????????????????
    private String startTime = ""; //????????????????????????
    private String endTime = ""; //????????????????????????
    /*******?????????????????????************/
    //??????????????????
    private Button submit_bt;
    private String region = "ap-beijing"; // ???????????????
    private String folderName = "";
    private CosXmlService cosXmlService;
    private TransferManager transferManager;
    private COSXMLUploadTask cosxmlTask;
    /****************??????????????????**************/
    private LinearLayout iv_back;
    // ???????????? 1 ?????? 2 ??????
    private String type = "1";
    //?????????????????????ID
    private String Hid = "";
    //????????????
    private String BankNum;
    //???????????????ID
    private String merchantNo;
    //????????????
    private String IdUrl1isActive="1";
    private String IdUrl2isActive="1";
    private String IdUrl3isActive="1";
    private String IdUrl4isActive="1";
    private String IdUrl5isActive="1";

    //?????? 1 ?????? 2 ??????
    private String bj_type = "";

    private PopupWindow popWindow;
    private View popView;
    private int BankCardIn = 1006;

    // ????????????
    public static HomeQuoteActivity3 instance = null;
    @Override
    protected int getLayoutId() {
        //?????????????????????
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.homequote_activity_03;
    }

    @Override
    protected void initView() {
        instance = this;
        // ????????????????????????
        cosXmlService = CosServiceFactory.getCosXmlService(this, region, getSecretId(), getSecretKey(), true);
        TransferConfig transferConfig = new TransferConfig.Builder().build();
        transferManager = new TransferManager(cosXmlService, transferConfig);
        id_card_is = findViewById(R.id.id_card_is);
        bNumber = findViewById(R.id.b_number);
        id_card_the = findViewById(R.id.id_card_the);
        submit_bt = findViewById(R.id.submit_bt);
        iv_back = findViewById(R.id.iv_back);
    }

    @Override
    protected void initListener() {
        id_card_is.setOnClickListener(this);
        id_card_the.setOnClickListener(this);
        submit_bt.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        snCode = getIntent().getStringExtra("snCode");
        uPhone = getIntent().getStringExtra("uPhone");
        fType = getIntent().getStringExtra("fType");
        province = getIntent().getStringExtra("province");
        city = getIntent().getStringExtra("city");
        area = getIntent().getStringExtra("area");
        type = getIntent().getStringExtra("type");
        bj_type = getIntent().getStringExtra("bj_type");
        IdUrl1 = getIntent().getStringExtra("IdUrl1");
        IdUrl2 = getIntent().getStringExtra("IdUrl2");
        IdUrl3 = getIntent().getStringExtra("IdUrl3");
        fName = getIntent().getStringExtra("fName");
        fNumber = getIntent().getStringExtra("fNumber");
        startTime = getIntent().getStringExtra("startTime");
        endTime = getIntent().getStringExtra("endTime");
        IdUrl1isActive = getIntent().getStringExtra("IdUrl1isActive");
        IdUrl2isActive = getIntent().getStringExtra("IdUrl2isActive");
        IdUrl3isActive = getIntent().getStringExtra("IdUrl3isActive");
        shouLog("????????????3?????????????????????--------","---------------");
        shouLog("snCode=",snCode);
        shouLog("uPhone=",uPhone);
        shouLog("fType=",fType);
        shouLog("province=",province);
        shouLog("city=",city);
        shouLog("area=",area);
        shouLog("type=",type);
        shouLog("bj_type=",bj_type);
        shouLog("????????????3?????????????????????--------","---------------");
        shouLog("????????????3?????????????????????--------","---------------");
        shouLog("IdUrl1=",IdUrl1);
        shouLog("IdUrl2=",IdUrl2);
        shouLog("IdUrl3=",IdUrl3);
        shouLog("fName=",fName);
        shouLog("fNumber=",fNumber);
        shouLog("startTime=",startTime);
        shouLog("endTime=",endTime);
        shouLog("IdUrl1isActive=",IdUrl1isActive);
        shouLog("IdUrl2isActive=",IdUrl2isActive);
        shouLog("IdUrl3isActive=",IdUrl3isActive);
        shouLog("????????????3?????????????????????--------","---------------");
        if (type.equals("2")){
            Hid = getIntent().getStringExtra("Hid");
            bUrl1 = getIntent().getStringExtra("ID_url4");
            bUrl2 = getIntent().getStringExtra("ID_url5");
            BankNum = getIntent().getStringExtra("BankNum");
            merchantNo = getIntent().getStringExtra("merchantNo");
            id_card_is.setImageURI(bUrl1);
            id_card_the.setImageURI(bUrl2);
            bNumber.setText(BankNum);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_card_is:
                showEditPhotoWindow(BankCardIn);
                break;
            case R.id.id_card_the:
                com.wildma.pictureselector.PictureSelector
                        .create(HomeQuoteActivity3.this, com.wildma.pictureselector.PictureSelector.SELECT_REQUEST_CODE)
                        .selectPicture(false);
                break;
            case R.id.submit_bt:
                if (TextUtils.isEmpty(bUrl1)) {
                    showToast(3, "?????????????????????");
                    return;
                }
                if (TextUtils.isEmpty(bUrl2)) {
                    showToast(3, "?????????????????????");
                    return;
                }
                if (TextUtils.isEmpty(bNumber.getText().toString().trim())) {
                    showToast(3, "????????????");
                    return;
                }
                loadDialog.show();
                folderName = "authentication" + "/" + fNumber + "/" + TimeUtils.getNowTime("day");
                List<SmallinformationBean> beans = new ArrayList<>();
                // ???????????????
                if (IdUrl1isActive.equals("2")){
                    SmallinformationBean bean1 = new SmallinformationBean();
                    bean1.setName("1");
                    bean1.setUrl(IdUrl1);
                    bean1.setAcitvie(IdUrl1isActive);
                    beans.add(bean1);
                }
                if (IdUrl2isActive.equals("2")){
                    SmallinformationBean bean2 = new SmallinformationBean();
                    bean2.setName("2");
                    bean2.setUrl(IdUrl2);
                    bean2.setAcitvie(IdUrl2isActive);
                    beans.add(bean2);
                }
                if (IdUrl3isActive.equals("2")){
                    SmallinformationBean bean3 = new SmallinformationBean();
                    bean3.setName("3");
                    bean3.setUrl(IdUrl3);
                    bean3.setAcitvie(IdUrl3isActive);
                    beans.add(bean3);
                }
                if (IdUrl4isActive.equals("2")){
                    SmallinformationBean bean4 = new SmallinformationBean();
                    bean4.setName("4");
                    bean4.setUrl(bUrl1);
                    bean4.setAcitvie(IdUrl4isActive);
                    beans.add(bean4);
                }
                if (IdUrl5isActive.equals("2")){
                    SmallinformationBean bean5 = new SmallinformationBean();
                    bean5.setName("5");
                    bean5.setUrl(bUrl2);
                    bean5.setAcitvie(IdUrl5isActive);
                    beans.add(bean5);
                }
                if (beans.size() < 1){
                    if (bj_type.equals("1") || bj_type.equals("3")){
                        EditData();
                    }else {
                        posData();
                    }
                }else {
                    Log.e("------???", "??????????????????");
                    for (int i = 0; i < beans.size(); i++) {
                        upload(beans, folderName);
                    }
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }


    /**
     * ????????????,????????????
     */
    private void upload(List<SmallinformationBean> list, String newfolderName) {
        int i = 0;
        if (TextUtils.isEmpty(list.get(i).getUrl())||list.get(i).getAcitvie().equals("1")) {
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
                        IdUrl1 = cOSXMLUploadTaskResult.accessUrl;
                        Log.e("???????????????", cOSXMLUploadTaskResult.accessUrl);
                    } else if (list.get(i).getName().equals("2")) {
                        IdUrl2 = cOSXMLUploadTaskResult.accessUrl;
                        Log.e("???????????????", cOSXMLUploadTaskResult.accessUrl);
                    } else if (list.get(i).getName().equals("3")){
                        IdUrl3 = cOSXMLUploadTaskResult.accessUrl;
                        Log.e("???????????????", cOSXMLUploadTaskResult.accessUrl);
                    }else if (list.get(i).getName().equals("4")){
                        bUrl1 = cOSXMLUploadTaskResult.accessUrl;
                        Log.e("???????????????", cOSXMLUploadTaskResult.accessUrl);
                    }else {
                        Log.e("???????????????", cOSXMLUploadTaskResult.accessUrl);
                        bUrl2 = cOSXMLUploadTaskResult.accessUrl;
                    }
                    setResult(RESULT_OK);
                    list.remove(i);
                    if (list.size() < 1) {
                        Log.e("----------???", "???????????????????????????");
                        if (bj_type.equals("1") || bj_type.equals("3")){
                            EditData();
                        }else {
                            posData();
                        }
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
                        showToast(2,"??????????????????????????????????????????");
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
    private void posData(){
        RequestParams params = new RequestParams();
        params.put("sn",snCode);
        params.put("phone",uPhone);
        params.put("applicant",fName);
        params.put("provinceno",province);
        params.put("cityno",city);
        params.put("areano",area);
        params.put("certificateno",fNumber);
        params.put("certificatestartdate",startTime);
        params.put("certificateenddate",endTime);
        params.put("bankcardaccount",bNumber.getText().toString().trim());
        params.put("feeChlId",fType);
        params.put("accountId",getUserName());
        params.put("idcardhand",IdUrl3);
        params.put("idcardfront",IdUrl1);
        params.put("idcardback",IdUrl2);
        params.put("bankcardfront",bUrl1);
        params.put("bankcardback",bUrl2);
        HttpRequest.getNewOperation(params, "",new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                loadDialog.dismiss();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    if (result.getString("data").equals("true")){
                        Intent intent = new Intent(HomeQuoteActivity3.this, QuoteSucceccActivity.class);
                        intent.putExtra("id",result.getString("id"));
                        startActivity(intent);
                    } else {
                        showToast(2,result.getString("retMsge")+"");
                        HomeQuoteActivity2.instance.finish();
                        HomeQuoteActivity1.instance.finish();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(OkHttpException failuer) {
                loadDialog.dismiss();
                HomeQuoteActivity2.instance.finish();
                HomeQuoteActivity1.instance.finish();
                finish();
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

    /**
     * ??????
     */
    private void EditData(){
        RequestParams params = new RequestParams();
        params.put("merchantNo",merchantNo);
        params.put("sn",snCode);
        params.put("accountId",getUserName());
        params.put("provinceno",province);
        params.put("cityno",city);
        params.put("areano",area);
        params.put("certificateno",fNumber);
        params.put("certificatestartdate",startTime);
        params.put("certificateenddate",endTime);
        params.put("bankcardaccount",bNumber.getText().toString().trim());
        params.put("feeChlId",fType);
        params.put("idcardhand",IdUrl3);
        params.put("idcardfront",IdUrl1);
        params.put("idcardback",IdUrl2);
        params.put("bankcardfront",bUrl1);
        params.put("bankcardback",bUrl2);
        HttpRequest.getUpdate(params, "",new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                loadDialog.dismiss();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    if (result.getString("data").equals("true")){
                        //??????
                        HomeQuoteActivity2.instance.finish();
                        HomeQuoteActivity1.instance.finish();
                        finish();
                    }else {
                        showToast(3,result.getString("msg"));
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
    /************************************** ?????????????????? ***********************************************************************/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
                IdUrl5isActive = "2";
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
                                bUrl2 = file.getPath();
                                id_card_the.setImageBitmap(BitmapFactory.decodeFile(bUrl2));
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        }).launch();
            }
        }else if(requestCode == BankCardIn){
            PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
            IdUrl4isActive = "2";
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
                            bUrl1 = file.getPath();
                            id_card_is.setImageBitmap(BitmapFactory.decodeFile(bUrl1));
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
                bUrl1 = ImageConvertUtil.getFile(retBitmap1).getCanonicalPath();
                IdUrl4isActive = "2";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!response.isEmpty()) {
            final BankCardInfo bankCardInfo = new Gson().fromJson(response, BankCardInfo.class);
            bNumber.setText(bankCardInfo.getCardNo());
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        OcrSDKKit.getInstance().release();
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
                OcrSDKKit.getInstance().startProcessOcr(HomeQuoteActivity3.this, OcrType.BankCardOCR,
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
                        .create(HomeQuoteActivity3.this, value)
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

}

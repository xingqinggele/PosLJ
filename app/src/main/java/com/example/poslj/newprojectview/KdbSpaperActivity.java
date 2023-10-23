package com.example.poslj.newprojectview;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.poslj.R;
import com.example.poslj.adapter.NewMerchantsGridViewAdapter;
import com.example.poslj.base.BaseActivity;
import com.example.poslj.net.HttpRequest;
import com.example.poslj.net.OkHttpException;
import com.example.poslj.net.RequestParams;
import com.example.poslj.net.ResponseCallback;
import com.example.poslj.newprojectview.adapter.ImageGridAdapter;
import com.example.poslj.newprojectview.bean.ImageGridBean;
import com.example.poslj.newprojectview.bean.NewCityBean;
import com.example.poslj.newprojectview.bean.NewDistrictBean;
import com.example.poslj.newprojectview.bean.NewProvinceBean;
import com.example.poslj.newprojectview.bean.NewRateBean;
import com.example.poslj.newprojectview.model.ProvinceModel;
import com.example.poslj.newprojectview.model.UploadImageView;
import com.example.poslj.utils.CustomConfigUtil;
import com.example.poslj.utils.ImageConvertUtil;
import com.example.poslj.utils.TimeUtils;
import com.example.poslj.utils.Utility;
import com.example.poslj.views.MyDialog1;
import com.example.poslj.views.MyDialog3;
import com.example.poslj.views.MyGridView;
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
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.example.poslj.utils.Utils.isLocServiceEnable;


/**
 * 作者: qgl
 * 创建日期：2023/6/19
 * 描述:开店宝进件
 */
public class KdbSpaperActivity extends BaseActivity implements View.OnClickListener, ProvinceModel.ProvinceListener, ImageGridAdapter.OnImageClickListener, UploadImageView.onResultListener {
    private EditText newspaper_posCode_ed;
    //法人姓名
    private EditText newspaper_idName_ed;
    //身份证号码
    private EditText newspaper_card_number_ed;
    //有效期开始
    private TextView newspaper_start_time;
    //有效期结束
    private TextView newspaper_end_time;
    //银行卡号码
    private EditText newspaper_bank_number_ed;
    //预留手机号
    private EditText newspaper_bank_phone_ed;
    //省市区
    private TextView newspaper_province_tv;
    //详细地址
    private EditText newspaper_address_ed;
    //返回键
    private LinearLayout iv_back;
    //选择的开始时间
    private String startTime = "";
    //选择的结束时间
    private String endTime = "";
    /**************  地图定位  ****************/
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private String Longitude = ""; //经
    private String Latitude = "";//纬度
    private String loctionEorr = "";
    /**************  地图定位  ****************/
    private ProvinceModel provinceModel;
    private PopupWindow popWindow;
    private View popView;
    private List<ImageGridBean> imageBeanList = new ArrayList<>();
    private ImageGridAdapter imageGridAdapter;
    private MyGridView newspaper_grid_view;
    private Button submit_bt;
    private Bitmap bitmap;
    private String provinceNo;
    private String provinceName;
    private String cityNo;
    private String cityName;
    private String areaNo;
    private String areaName;
    private UploadImageView uploadImageView;
    //费率ID
    private TextView feilv_tv;
    private String rateId = "";
    private List<NewRateBean> rateBeans = new ArrayList<>();
    //类型适配器Adapter
    private NewMerchantsGridViewAdapter madapter;
    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.kdb_spper_activity;
    }

    @Override
    protected void initView() {
        initSdk(getSecretId(), getSecretKey());
        uploadImageView = new UploadImageView(this, getSecretId(), getSecretKey(), getBucketName(), this);
        newspaper_idName_ed = findViewById(R.id.newspaper_idName_ed);
        newspaper_card_number_ed = findViewById(R.id.newspaper_card_number_ed);
        newspaper_start_time = findViewById(R.id.newspaper_start_time);
        newspaper_end_time = findViewById(R.id.newspaper_end_time);
        newspaper_bank_number_ed = findViewById(R.id.newspaper_bank_number_ed);
        newspaper_bank_phone_ed = findViewById(R.id.newspaper_bank_phone_ed);
        newspaper_province_tv = findViewById(R.id.newspaper_province_tv);
        newspaper_address_ed = findViewById(R.id.newspaper_address_ed);
        newspaper_grid_view = findViewById(R.id.newspaper_grid_view);
        newspaper_posCode_ed = findViewById(R.id.newspaper_posCode_ed);
        iv_back = findViewById(R.id.iv_back);
        submit_bt = findViewById(R.id.submit_bt);
        feilv_tv = findViewById(R.id.feilv_tv);

        if (isLocServiceEnable(this)) {
            //初始化定位
            initLocation();
            //开启定位
            startLocation();
        } else {
            isDialog("请您设置位置权限在进行报件！");
        }
        //初始化选择省市区
        provinceModel = new ProvinceModel(this, this);
        posRate();
    }

    @Override
    protected void initListener() {
        newspaper_start_time.setOnClickListener(this);
        newspaper_end_time.setOnClickListener(this);
        newspaper_province_tv.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        submit_bt.setOnClickListener(this);
        feilv_tv.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        for (int i = 0; i < 4; i++) {
            ImageGridBean imageBean = new ImageGridBean();
            imageBean.setId(i + "");
            switch (i) {
                case 0:
                    imageBean.setPlaceholderimage(R.mipmap.small_merchants_card1);
                    imageBean.setImagename("身份证正面");
                    break;
                case 1:
                    imageBean.setPlaceholderimage(R.mipmap.small_merchants_card2);
                    imageBean.setImagename("身份证反面");
                    break;
                case 2:
                    imageBean.setPlaceholderimage(R.mipmap.small_merchants_card3);
                    imageBean.setImagename("手持身份证");
                    break;
                case 3:
                    imageBean.setPlaceholderimage(R.mipmap.small_merchants_card4);
                    imageBean.setImagename("银行卡");
                    break;
            }
            imageBeanList.add(imageBean);
        }

        imageGridAdapter = new ImageGridAdapter(KdbSpaperActivity.this, imageBeanList);
        newspaper_grid_view.setAdapter(imageGridAdapter);
        imageGridAdapter.setOnImageClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.feilv_tv:
                showDialog(rateBeans);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.newspaper_start_time:
                selectTime(newspaper_start_time, 1);
                break;
            case R.id.newspaper_end_time:
                selectTime(newspaper_end_time, 2);
                break;
            case R.id.newspaper_province_tv:
                provinceModel.showCityPicker();
                break;
            case R.id.submit_bt:
                for (int i = 0; i < imageBeanList.size(); i++) {
                    if (TextUtils.isEmpty(imageBeanList.get(i).getNeturl())) {
                        showToast(3, "请上传" + imageBeanList.get(i).getImagename() + "照片!");
                        return;
                    }
                }
                if (TextUtils.isEmpty(newspaper_posCode_ed.getText().toString().trim())){
                    showToast(3, "请输入机具编号！");
                    return;
                }
                if (TextUtils.isEmpty(rateId)) {
                    showToast(3, "请选择费率！");
                    return;
                }
                if (TextUtils.isEmpty(newspaper_idName_ed.getText().toString().trim())) {
                    showToast(3, "姓名！");
                    return;
                }
                if (TextUtils.isEmpty(newspaper_card_number_ed.getText().toString().trim())) {
                    showToast(3, "身份证号！");
                    return;
                }
                if (TextUtils.isEmpty(startTime)) {
                    showToast(3, "开始时间！");
                    return;
                }
                if (TextUtils.isEmpty(endTime)) {
                    showToast(3, "到期时间！");
                    return;
                }
                if (TextUtils.isEmpty(newspaper_bank_number_ed.getText().toString().trim())) {
                    showToast(3, "银行卡号！");
                    return;
                }
                if (TextUtils.isEmpty(newspaper_bank_phone_ed.getText().toString().trim())) {
                    showToast(3, "手机号！");
                    return;
                }
                if (TextUtils.isEmpty(provinceNo)) {
                    showToast(3, "省市区！");
                    return;
                }
                if (TextUtils.isEmpty(newspaper_address_ed.getText().toString().trim())) {
                    showToast(3, "详细地址！");
                    return;
                }

                //设置按钮点击失效
                submit_bt.setEnabled(false);
                subMintData();
                break;

        }
    }


    /**
     * 点击识别按钮 处理方法
     *
     * @param type
     */
    private void showEditPhotoWindow(int type) {
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
                //身份证正面
                if (type == 0) {
                    getIDCardIn(type);
                } else if (type == 1) {
                    getIDCardOn(type);
                } else if (type == 3) {
                    getBankOcr(type);
                }
            }
        });
        tv_pai_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popWindow.isShowing()) {
                    popWindow.dismiss();
                }
                PictureSelector
                        .create(KdbSpaperActivity.this, type)
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

    //页面关闭
    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyLocation();
        OcrSDKKit.getInstance().release();
    }

    /**
     * 选择有效期
     *
     * @param textView
     * @param type
     */
    private void selectTime(TextView textView, int type) {
        TimePickerView pvTime = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //开始时间
                if (type == 1) {
                    startTime = TimeUtils.getNewTimes(date);
                }
                //结束时间
                else {
                    endTime = TimeUtils.getNewTimes(date);
                }
                textView.setText(TimeUtils.getNewTimes(date));
            }
        }).setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .build();
        pvTime.show();
    }

    /***************************************** OCR识别---------开始 ***************************************************************/
    /**
     * 腾讯卡片识别初始化
     */
    private void initSdk(String secretId, String secretKey) {
        // 启动参数配置
        OcrType ocrType = OcrType.IDCardOCR_FRONT; // 设置默认的业务识别，银行卡
        OcrSDKConfig configBuilder = OcrSDKConfig.newBuilder(secretId, secretKey, null)
                .setOcrType(ocrType)
                .setCropIdCard(true)
                .setModeType(OcrModeType.OCR_DETECT_MANUAL)
                .build();
        // 初始化SDK
        OcrSDKKit.getInstance().initWithConfig(this.getApplicationContext(), configBuilder);
    }

    //身份证正面识别
    private void getIDCardIn(int position) {
        //弹出界面
        OcrSDKKit.getInstance().startProcessOcrResultEntity(this, OcrType.IDCardOCR_FRONT, null, IdCardOcrResult.class, new ISdkOcrEntityResultListener<IdCardOcrResult>() {
            @Override
            public void onProcessSucceed(IdCardOcrResult idCardOcrResult, OcrProcessResult ocrProcessResult) {
                revealResult(idCardOcrResult, position);
            }

            @Override
            public void onProcessFailed(String errorCode, String message, OcrProcessResult ocrProcessResult) {
                popTip(errorCode, message);
                Log.e("正--requestId", ocrProcessResult.toString());

            }
        });

    }

    /**
     * 身份证反面
     *
     * @param position
     */
    private void getIDCardOn(int position) {
        OcrSDKKit.getInstance().startProcessOcrResultEntity(this, OcrType.IDCardOCR_BACK, null, IdCardOcrResult.class,
                new ISdkOcrEntityResultListener<IdCardOcrResult>() {
                    @Override
                    public void onProcessSucceed(IdCardOcrResult idCardOcrResult, OcrProcessResult ocrProcessResult) {
                        revealResult(idCardOcrResult, position);
                    }

                    @Override
                    public void onProcessFailed(String errorCode, String message, OcrProcessResult ocrProcessResult) {
                        popTip(errorCode, message);
                        Log.e("反--requestId", ocrProcessResult.toString());
                    }

                });
    }

    /**
     * 银行卡识别
     *
     * @param position
     */
    private void getBankOcr(int position) {
        OcrSDKKit.getInstance().startProcessOcrResultEntity(this, OcrType.BankCardOCR,
                CustomConfigUtil.getInstance().getCustomConfigUi(), BankCardOcrResult.class, new ISdkOcrEntityResultListener<BankCardOcrResult>() {
                    @Override
                    public void onProcessSucceed(BankCardOcrResult bankCardOcrResult, OcrProcessResult ocrProcessResult) {
                        //回显银行卡信息
                        revealBankResult(bankCardOcrResult, ocrProcessResult.imageBase64Str, position);
                    }

                    @Override
                    public void onProcessFailed(String errorCode, String message, OcrProcessResult ocrProcessResult) {
                        popTip(errorCode, message);
                        Log.e("银行卡--requestId", ocrProcessResult.toString());
                    }

                });
    }

    /**
     * ocr识别回显处理
     *
     * @param idCardOcrResult
     */
    private void revealResult(IdCardOcrResult idCardOcrResult, int position) {
        shouLog("--->", idCardOcrResult.toString());
        String json = idCardOcrResult.advancedInfo;
        try {
            JSONObject object = new JSONObject(json);
            String idCard = object.getString("IdCard");
            Bitmap bitmap = ImageConvertUtil.base64ToBitmap(idCard);
            imageBeanList.get(position).setUrl(ImageConvertUtil.getFile(bitmap).getCanonicalPath());
            imageGridAdapter.renewal();

            String folderName = "baojian/Android/ns" + TimeUtils.getNowTime("day");
            uploadImageView.upload(position, ImageConvertUtil.getFile(bitmap).getCanonicalPath(), folderName);


            if (position == 0) {
                OcrDialog("IN", idCardOcrResult);
            } else if (position == 1) {
                OcrDialog("ON", idCardOcrResult);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ocr银行卡回显处理
     *
     * @param response
     * @param srcBase64Image
     * @param position
     */
    private void revealBankResult(BankCardOcrResult response, String srcBase64Image, int position) {
        if (!srcBase64Image.isEmpty()) {
            bitmap = ImageConvertUtil.base64ToBitmap(srcBase64Image);
        }
        try {
            imageBeanList.get(position).setUrl(ImageConvertUtil.getFile(bitmap).getCanonicalPath());
            String folderName = "baojian/Android/ns" + TimeUtils.getNowTime("day");
            uploadImageView.upload(position, ImageConvertUtil.getFile(bitmap).getCanonicalPath(), folderName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageGridAdapter.renewal();
        BankOcrDialog(response.cardNo);
    }

    /***************************************** OCR识别---------结束 ***************************************************************/

    /***************************************** 高德获取当前位置信息---------开始 ***************************************************************/
    /**
     * 初始化定位
     */
    private void initLocation() {
        AMapLocationClient.updatePrivacyShow(this, true, true);
        AMapLocationClient.updatePrivacyAgree(this, true);
        //初始化client
        try {
            locationClient = new AMapLocationClient(this.getApplicationContext());
            locationOption = getDefaultOption();
            //设置定位参数
            locationClient.setLocationOption(locationOption);
            // 设置定位监听
            locationClient.setLocationListener(locationListener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void startLocation() {
        try {
            //等待框提示词
            loadDialog.setTitleStr("请稍后...");
            //开启等待框
            loadDialog.show();
            // 设置定位参数
            locationClient.setLocationOption(locationOption);
            // 启动定位
            locationClient.startLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 如果没有权限、弹框
     *
     * @param title
     */
    private void isDialog(String title) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.is_dialog_layout, null);
        TextView textView = view.findViewById(R.id.dialog_tv1);
        TextView dialog_determine = view.findViewById(R.id.dialog_determine);
        textView.setText(title);
        Dialog dialog = new MyDialog1(KdbSpaperActivity.this, true, true, (float) 0.7).setNewView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        dialog_determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提交
                dialog.dismiss();
                finish();
            }
        });
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                //关闭等待框
                loadDialog.dismiss();
                StringBuffer sb = new StringBuffer();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0) {
                    Longitude = location.getLongitude() + "";
                    Latitude = location.getLatitude() + "";
                    sb.append("定位成功" + "\n");
                    sb.append("定位类型: " + location.getLocationType() + "\n");
                    sb.append("经    度    : " + location.getLongitude() + "\n");
                    sb.append("纬    度    : " + location.getLatitude() + "\n");
                    sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
                    sb.append("提供者    : " + location.getProvider() + "\n");
                    sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                    sb.append("角    度    : " + location.getBearing() + "\n");
                    // 获取当前提供定位服务的卫星个数
                    sb.append("星    数    : " + location.getSatellites() + "\n");
                    sb.append("国    家    : " + location.getCountry() + "\n");
                    sb.append("省            : " + location.getProvince() + "\n");
                    sb.append("市            : " + location.getCity() + "\n");
                    sb.append("城市编码 : " + location.getCityCode() + "\n");
                    sb.append("区            : " + location.getDistrict() + "\n");
                    sb.append("区域 码   : " + location.getAdCode() + "\n");
                    sb.append("地    址    : " + location.getAddress() + "\n");
                    sb.append("兴趣点    : " + location.getPoiName() + "\n");
                    shouLog("地址信息1：", sb.toString());
                } else {
                    //定位失败
                    sb.append("定位失败" + "\n");
                    sb.append("错误码:" + location.getErrorCode() + "\n");
                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
                    loctionEorr = sb.toString();
                    shouLog("---》", sb.toString());

                }
                //停止定位
                stopLocation();
            } else {
                shouLog("---》", "失败了");
            }
        }
    };

    /**
     * 停止定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void stopLocation() {
        try {
            // 停止定位
            locationClient.stopLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 销毁定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    /***************************************** 高德获取当前位置信息---------结束 ***************************************************************/

    /***************************************** 获取省/市/区---------开始 ***************************************************************/
    /**
     * 请求地区接口
     *
     * @param areaLevel  级别 1、2、3
     * @param parentCode 父类的code
     * @param type       请求的界别 1、2、3
     */
    @Override
    public void postProvince(String areaLevel, String parentCode, int type) {
        RequestParams params = new RequestParams();
        params.put("areaLevel", areaLevel);
        params.put("parentCode", parentCode);
        HttpRequest.getArea(params, getToken(), new ResponseCallback() {
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
                        provinceModel.setTitleData(titleList);
                    } else if (type == 1) {
                        List<NewCityBean> titleList = gson.fromJson(result.getJSONArray("data").toString(),
                                new TypeToken<List<NewCityBean>>() {
                                }.getType());
                        provinceModel.setTwoLabelData(titleList);
                    } else if (type == 2) {
                        List<NewDistrictBean> titleList = gson.fromJson(result.getJSONArray("data").toString(),
                                new TypeToken<List<NewDistrictBean>>() {
                                }.getType());
                        provinceModel.setAreaLabelData(titleList);
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
     * 地址结果回调
     *
     * @param province
     * @param city
     * @param area
     * @param provinceCode
     * @param cityCode
     * @param areaCode
     */
    @Override
    public void setSelectAdrCallback(String province, String city, String area, String provinceCode, String cityCode, String areaCode) {
        newspaper_province_tv.setText(province + "-" + city + "-" + area);
        provinceNo = provinceCode;
        provinceName = province;
        cityNo = cityCode;
        cityName = city;
        areaNo = areaCode;
        areaName = area;

    }

    /***************************************** 获取省/市/区---------结束 ***************************************************************/

    /**
     * 点击图片
     *
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        //如果没有上传图片
        if (TextUtils.isEmpty(imageBeanList.get(position).getUrl())) {
            //去上传图片
            if (position == 2) {
                PictureSelector
                        .create(KdbSpaperActivity.this, position)
                        .selectPicture(false);
            } else {
                showEditPhotoWindow(position);
            }
        } else {
            //查看图片
            Intent intent = new Intent(this, PicImageActivity.class);
            intent.putExtra("pic", imageBeanList.get(position).getUrl());
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 0、身份证 1、反面 3、手持 4、银行卡
        if (requestCode == 0) {
            if (data != null) {
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
                                //数组添加数据
                                imageBeanList.get(0).setUrl(file.getPath());
                                //更新adapter
                                imageGridAdapter.renewal();
                                String folderName = "baojian/Android/ns" + TimeUtils.getNowTime("day");
                                uploadImageView.upload(0, file.getPath(), folderName);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        }).launch();
            }
        } else if (requestCode == 1) {
            if (data != null) {
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
                                //数组添加数据
                                imageBeanList.get(1).setUrl(file.getPath());
                                //更新adapter
                                imageGridAdapter.renewal();
                                String folderName = "baojian/Android/ns" + TimeUtils.getNowTime("day");
                                uploadImageView.upload(1, file.getPath(), folderName);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        }).launch();
            }
        } else if (requestCode == 2) {
            if (data != null) {
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
                                //数组添加数据
                                imageBeanList.get(2).setUrl(file.getPath());
                                //更新adapter
                                imageGridAdapter.renewal();
                                String folderName = "baojian/Android/ns" + TimeUtils.getNowTime("day");
                                uploadImageView.upload(2, file.getPath(), folderName);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        }).launch();
            }
        } else if (requestCode == 3) {
            if (data != null) {
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
                                //数组添加数据
                                imageBeanList.get(3).setUrl(file.getPath());
                                //更新adapter
                                imageGridAdapter.renewal();
                                String folderName = "baojian/Android/ns" + TimeUtils.getNowTime("day");
                                uploadImageView.upload(3, file.getPath(), folderName);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        }).launch();
            }
        }
    }

    /**
     * 图片存储后回调
     *
     * @param type
     * @param url
     */
    @Override
    public void onResult(int type, String url) {
        shouLog("存储图片返回值----->", type + url);
        imageBeanList.get(type).setNeturl(url);
    }

    /**
     * 识别后的dialog
     *
     * @param type
     * @param idCardOcrResult
     */
    private void OcrDialog(String type, IdCardOcrResult idCardOcrResult) {
        View view = null;
        EditText dialog_name = null;
        EditText dialog_number = null;
        EditText dialog_sTime = null;
        EditText dialog_eTime = null;
        if (type.equals("IN")) {
            view = LayoutInflater.from(mContext).inflate(R.layout.id_in_dialog_layout, null);
            dialog_name = view.findViewById(R.id.dialog_name);
            dialog_number = view.findViewById(R.id.dialog_number);
            dialog_name.setText(idCardOcrResult.getName());
            dialog_number.setText(idCardOcrResult.getIdNum());
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.id_on_dialog_layout, null);
            dialog_sTime = view.findViewById(R.id.dialog_sTime);
            dialog_eTime = view.findViewById(R.id.dialog_eTime);
            String st1 = idCardOcrResult.validDate.substring(0, idCardOcrResult.validDate.indexOf("-"));
            String st2 = idCardOcrResult.validDate.substring(st1.length() + 1);
            String s = st1.replace(".", "");
            String t = st2.replace(".", "");
            dialog_sTime.setText(s);
            dialog_eTime.setText(t);
        }
        Button confirm = view.findViewById(R.id.confirm);
        Button cancel = view.findViewById(R.id.cancel);
        Dialog dialog = new MyDialog1(KdbSpaperActivity.this, true, true, (float) 0.7).setNewView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        EditText finalDialog_name = dialog_name;
        EditText finalDialog_number = dialog_number;
        EditText finalDialog_sTime = dialog_sTime;
        EditText finalDialog_eTime = dialog_eTime;
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (type.equals("IN")) {
                    newspaper_idName_ed.setText(finalDialog_name.getText().toString().trim());
                    newspaper_card_number_ed.setText(finalDialog_number.getText().toString().trim());
                } else {
                    newspaper_start_time.setText(finalDialog_sTime.getText().toString().trim());
                    newspaper_end_time.setText(finalDialog_eTime.getText().toString().trim());
                    startTime = finalDialog_sTime.getText().toString().trim();
                    endTime = newspaper_end_time.getText().toString().trim();
                }
            }
        });

    }

    /**
     * 银行卡识别
     *
     * @param BankCardNum
     */
    private void BankOcrDialog(String BankCardNum) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.bank_dialog_layout, null);
        EditText dialog_bank_num = view.findViewById(R.id.dialog_bank_num);
        dialog_bank_num.setText(BankCardNum);
        Button confirm = view.findViewById(R.id.confirm);
        Button cancel = view.findViewById(R.id.cancel);
        Dialog dialog = new MyDialog1(KdbSpaperActivity.this, true, true, (float) 0.7).setNewView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                newspaper_bank_number_ed.setText(dialog_bank_num.getText().toString().trim());
            }
        });

    }

    /***
     * 选择费率
     */
    public void showDialog(List<NewRateBean> mList) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.home_quote_type_dialog, null);
        Button data_bill_dialog_btn = view.findViewById(R.id.data_bill_dialog_btn);
        MyGridView data_bill_dialog_grid = view.findViewById(R.id.data_bill_dialog_grid);
        madapter = new NewMerchantsGridViewAdapter(KdbSpaperActivity.this, mList);
        data_bill_dialog_grid.setAdapter(madapter);
        data_bill_dialog_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //把点击的position传递到adapter里面去
                madapter.changeState(i);
                feilv_tv.setText(mList.get(i).getFeeValue());
                rateId = mList.get(i).getFeeId();
            }
        });
        Dialog dialog = new MyDialog3(KdbSpaperActivity.this, true, true, (float) 1).setNewView(view);
        dialog.show();
        data_bill_dialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    //获取费率
    private void posRate() {
        RequestParams params = new RequestParams();
        HttpRequest.posEchoFeeId(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    rateBeans = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<NewRateBean>>() {
                            }.getType());
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
    private void subMintData() {
        loadDialog.show();
        RequestParams params = new RequestParams();
        //联系人
        params.put("applicant", newspaper_idName_ed.getText().toString().trim());
        //联系电话
        params.put("contactPhoneNo", newspaper_bank_phone_ed.getText().toString().trim());
        //客服电话
        params.put("clientServicePhoneNo", newspaper_bank_phone_ed.getText().toString().trim());
        //费率ID
        params.put("feeId", rateId);
        //SN号
        params.put("posCode", newspaper_posCode_ed.getText().toString().trim());
        //商户简写
        params.put("merchantShortHand", newspaper_idName_ed.getText().toString().trim());
        //省份代码
        params.put("provinceNo", provinceNo);
        //城市编号
        params.put("cityNo", cityNo);
        //区县代码
        params.put("areaNo", areaNo);
        //详细地址
        params.put("address", newspaper_address_ed.getText().toString().trim());
        //法人证件号码
        params.put("certificateNo", newspaper_card_number_ed.getText().toString().trim());
        //证件人脸面
        params.put("certificatesFrontPic", imageBeanList.get(0).getNeturl());
        //证件国徽面
        params.put("certificatesBackPic", imageBeanList.get(1).getNeturl());
        //证件有效期截止日期（例：1949-10-01）;
        params.put("certificateEndDate", endTime);
        //证件有效期开始日期（例：1949-10-01）
        params.put("certificateStartDate", startTime);
        //手持身份证照片
        params.put("idcardHandPic", imageBeanList.get(2).getNeturl());
        //法人姓名
        params.put("legalPersonName", newspaper_idName_ed.getText().toString().trim());
        //银行账号
        params.put("bankCardAccount", newspaper_bank_number_ed.getText().toString().trim());
        //开户名
        params.put("bankCardHolder", newspaper_idName_ed.getText().toString().trim());
        //银行卡预留手机号
        params.put("bankCardPhone", newspaper_bank_phone_ed.getText().toString().trim());
        //银行卡照片
        params.put("bankCardPic", imageBeanList.get(3).getNeturl());
        //开户银行省份
        params.put("bankProvince", provinceNo);
        //开户银行城市
        params.put("bankCity", cityNo);
        //手机号
        params.put("phone", newspaper_bank_phone_ed.getText().toString().trim());
        //商户活体检测地址
        params.put("activeAddress", newspaper_address_ed.getText().toString().trim());
        //商户活体检测地址纬度
        params.put("activeLatitude", Latitude);
        //商户活体检测地址经度
        params.put("activeLongitude", Longitude);
        //省名称
        params.put("provinceName", provinceName);
        //市名称
        params.put("cityName", cityName);
        //区名称
        params.put("areaName", areaName);
        shouLog("提交的值-------------->", params.toString());
        HttpRequest.getApplyMerchantKdbinfo(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                if (loadDialog.isShowing()) {
                    loadDialog.dismiss();
                }
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    if (result.getString("code").equals("200")){
                        showToast(2, result.getJSONObject("data").getString("msg")+"");
                        finish();
                    }else {
                        //设置按钮可以点击
                        submit_bt.setEnabled(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(OkHttpException failuer) {
                if (loadDialog.isShowing()) {
                    loadDialog.dismiss();
                }
                //设置按钮可以点击
                submit_bt.setEnabled(true);
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });


    }

}
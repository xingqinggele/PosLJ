package com.example.poslj.newprojectview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.poslj.R;
import com.example.poslj.adapter.NewMerchantsGridViewAdapter;
import com.example.poslj.base.BaseActivity;
import com.example.poslj.mefragment.setup.SetUpActivity;
import com.example.poslj.net.HttpRequest;
import com.example.poslj.net.OkHttpException;
import com.example.poslj.net.RequestParams;
import com.example.poslj.net.ResponseCallback;
import com.example.poslj.newprojectview.adapter.NewAreaAdapter;
import com.example.poslj.newprojectview.adapter.NewCityAdapter;
import com.example.poslj.newprojectview.adapter.NewProvinceAdapter;
import com.example.poslj.newprojectview.bean.NewCityBean;
import com.example.poslj.newprojectview.bean.NewDistrictBean;
import com.example.poslj.newprojectview.bean.NewProvinceBean;
import com.example.poslj.newprojectview.bean.NewRateBean;
import com.example.poslj.utils.DataCleanManager;
import com.example.poslj.utils.Utils;
import com.example.poslj.views.MyDialog;
import com.example.poslj.views.MyDialog1;
import com.example.poslj.views.MyGridView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tencent.ocr.sdk.common.OcrSDKKit;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.poslj.utils.Utils.isLocServiceEnable;

/**
 * ??????: qgl
 * ???????????????2021/8/17
 * ??????: posP????????????1
 */
public class AddMerchantsActivity1 extends BaseActivity implements View.OnClickListener {
    //???????????????????????????
    private RelativeLayout province_relative;
    //??????????????????????????????TextView
    private TextView province_tv;
    //?????????
    private EditText quote_contact_name;
    //????????????
    private EditText quote_posCode;
    //????????????
    private EditText quote_address;
    //?????????????????????
    private EditText quote_phone;
    //????????????
    private Button submit_bt;
    /**********--??????????????????JD ??????--**************/
    private ListView mCityListView;
    private TextView mProTv;
    private TextView mCityTv;
    private TextView mAreaTv;
    private ImageView mCloseImg;
    private PopupWindow popWindow;
    private View mSelectedLine;
    private View popView;
    private NewProvinceAdapter mProvinceAdapter;
    private NewCityAdapter mCityAdapter;
    private NewAreaAdapter mAreaAdapter;
    private List<NewProvinceBean> provinceList = new ArrayList<>();
    private List<NewCityBean> cityList = null;
    private List<NewDistrictBean> areaList = null;
    private int tabIndex = 0;
    private Context context;
    private String colorSelected = "#ff181c20";
    private String colorAlert = "#ffff4444";
    private String province = ""; //?????????
    private String city = ""; //?????????
    private String area = ""; //?????????
    /**********--??????????????????JD ??????--**************/
    //?????????
    private LinearLayout iv_back;
    //????????????
    public static AddMerchantsActivity1 instance = null;
    private List<NewRateBean> rateBeans = new ArrayList<>();
    //???????????????Adapter
    private NewMerchantsGridViewAdapter madapter;
    //??????ID
    private RelativeLayout feilv_relative;
    private TextView feilv_tv;
    private String rateId = "";

    /**************  ????????????  ****************/
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private String Longitude = ""; //???
    private String Latitude = "";//??????
    private String loctionEorr = "";
    /**************  ????????????  ****************/

    private String provinceName;  // ?????????
    private String cityName;  // ?????????
    private String areaName;  // ?????????
    @Override
    protected int getLayoutId() {
        //?????????????????????
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.addmerchants_activity_01;
    }

    @Override
    protected void initView() {
        instance = this;
        context = this;
        if (isLocServiceEnable(this)){
            //???????????????
            initLocation();
            //????????????
            startLocation();
        }else {
            isDialog("??????????????????????????????????????????");
        }
        iv_back = findViewById(R.id.iv_back);
        province_relative = findViewById(R.id.province_relative);
        province_tv = findViewById(R.id.province_tv);
        quote_phone = findViewById(R.id.quote_phone);
        submit_bt = findViewById(R.id.submit_bt);
        quote_contact_name = findViewById(R.id.quote_contact_name);
        quote_posCode = findViewById(R.id.quote_posCode);
        quote_address = findViewById(R.id.quote_address);
        feilv_relative = findViewById(R.id.feilv_relative);
        feilv_tv = findViewById(R.id.feilv_tv);
        posRate();
    }

    /**
     * ????????????
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void startLocation() {
        try {
            //??????????????????
            loadDialog.setTitleStr("???????????????...");
            //???????????????
            loadDialog.show();
            // ??????????????????
            locationClient.setLocationOption(locationOption);
            // ????????????
            locationClient.startLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * ???????????????
     */
    private void initLocation() {
        AMapLocationClient.updatePrivacyShow(this, true, true);
        AMapLocationClient.updatePrivacyAgree(this, true);
        //?????????client
        try {
            locationClient = new AMapLocationClient(this.getApplicationContext());
            locationOption = getDefaultOption();
            //??????????????????
            locationClient.setLocationOption(locationOption);
            // ??????????????????
            locationClient.setLocationListener(locationListener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ????????????
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void destroyLocation() {
        if (null != locationClient) {
            /**
             * ??????AMapLocationClient????????????Activity???????????????
             * ???Activity???onDestroy??????????????????AMapLocationClient???onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    /**
     * ????????????
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                //???????????????
                loadDialog.dismiss();
                StringBuffer sb = new StringBuffer();
                //errCode??????0????????????????????????????????????????????????????????????????????????????????????????????????
                if (location.getErrorCode() == 0) {
                    Longitude = location.getLongitude() + "";
                    Latitude = location.getLatitude() + "";
                    sb.append("????????????" + "\n");
                    sb.append("????????????: " + location.getLocationType() + "\n");
                    sb.append("???    ???    : " + location.getLongitude() + "\n");
                    sb.append("???    ???    : " + location.getLatitude() + "\n");
                    sb.append("???    ???    : " + location.getAccuracy() + "???" + "\n");
                    sb.append("?????????    : " + location.getProvider() + "\n");
                    sb.append("???    ???    : " + location.getSpeed() + "???/???" + "\n");
                    sb.append("???    ???    : " + location.getBearing() + "\n");
                    // ?????????????????????????????????????????????
                    sb.append("???    ???    : " + location.getSatellites() + "\n");
                    sb.append("???    ???    : " + location.getCountry() + "\n");
                    sb.append("???            : " + location.getProvince() + "\n");
                    sb.append("???            : " + location.getCity() + "\n");
                    sb.append("???????????? : " + location.getCityCode() + "\n");
                    sb.append("???            : " + location.getDistrict() + "\n");
                    sb.append("?????? ???   : " + location.getAdCode() + "\n");
                    sb.append("???    ???    : " + location.getAddress() + "\n");
                    sb.append("?????????    : " + location.getPoiName() + "\n");
                    shouLog("????????????1???", sb.toString());
                    quote_address.setText(location.getAddress());
                } else {
                    //????????????
                    sb.append("????????????" + "\n");
                    sb.append("?????????:" + location.getErrorCode() + "\n");
                    sb.append("????????????:" + location.getErrorInfo() + "\n");
                    sb.append("????????????:" + location.getLocationDetail() + "\n");
                    loctionEorr = sb.toString();
                    shouLog("---???",sb.toString());

                }
                //????????????
                stopLocation();
            }else {
                shouLog("---???","?????????");
            }
        }
    };

    /**
     * ????????????
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void stopLocation() {
        try {
            // ????????????
            locationClient.stopLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyLocation();
    }

    /**
     * ?????????????????????
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);//????????????????????????????????????????????????????????????????????????????????????????????????????????????
        mOption.setGpsFirst(true);//?????????????????????gps??????????????????????????????????????????????????????
        mOption.setHttpTimeOut(30000);//???????????????????????????????????????????????????30?????????????????????????????????
        mOption.setInterval(2000);//???????????????????????????????????????2???
        mOption.setNeedAddress(true);//????????????????????????????????????????????????????????????true
        mOption.setOnceLocation(true);//?????????????????????????????????????????????false
        mOption.setOnceLocationLatest(false);//???????????????????????????wifi??????????????????false.???????????????true,?????????????????????????????????????????????????????????
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//????????? ????????????????????????????????????HTTP??????HTTPS????????????HTTP
        mOption.setSensorEnable(false);//????????????????????????????????????????????????false
        mOption.setWifiScan(true); //???????????????????????????wifi??????????????????true??????????????????false??????????????????????????????????????????????????????????????????????????????????????????????????????
        mOption.setLocationCacheEnable(true); //???????????????????????????????????????????????????true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//??????????????????????????????????????????????????????????????????????????????????????????????????????
        return mOption;
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        province_relative.setOnClickListener(this);
        submit_bt.setOnClickListener(this);
        feilv_relative.setOnClickListener(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void initData() {


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.province_relative:
                showCityPicker();
                break;
            case R.id.submit_bt:
                Intent intent = new Intent(AddMerchantsActivity1.this, AddMerchantsActivity2.class);
                if (TextUtils.isEmpty(quote_posCode.getText().toString().trim())) {
                    showToast(3, "?????????SN");
                    return;
                }
                if (TextUtils.isEmpty(rateId)) {
                    showToast(3, "???????????????");
                    return;
                }
                if (TextUtils.isEmpty(quote_contact_name.getText().toString().trim())) {
                    showToast(3, "?????????");
                    return;
                }

                if (TextUtils.isEmpty(quote_phone.getText().toString().trim())) {
                    showToast(3, "????????????");
                    return;
                }

                if (TextUtils.isEmpty(province.trim())) {
                    showToast(3, "???????????????");
                    return;
                }
                if (TextUtils.isEmpty(quote_address.getText().toString().trim())) {
                    showToast(3, "????????????");
                    return;
                }
                if (Longitude.equals("") && Longitude == ""){
                    showToast(3, loctionEorr);
                    return;
                }
                intent.putExtra("quote_contact_name", quote_contact_name.getText().toString().trim());
                intent.putExtra("quote_shop_jname", quote_contact_name.getText().toString().trim());
                intent.putExtra("PosCode", quote_posCode.getText().toString().trim());
                intent.putExtra("rateId", rateId);
                intent.putExtra("quote_phone", quote_phone.getText().toString().trim());
                intent.putExtra("quote_service_phone", quote_phone.getText().toString().trim());
                intent.putExtra("quote_address", quote_address.getText().toString().trim());
                intent.putExtra("province", province);
                intent.putExtra("city", city);
                intent.putExtra("area", area);
                intent.putExtra("Longitude", Longitude);
                intent.putExtra("Latitude", Latitude);
                intent.putExtra("provinceName", provinceName);
                intent.putExtra("cityName", cityName);
                intent.putExtra("areaName", areaName);
                startActivity(intent);
                break;
            case R.id.feilv_relative:
                showDialog(rateBeans);
                break;
        }
    }

    //????????????
    private void posRate() {
        RequestParams params = new RequestParams();
        HttpRequest.posEchoFeeId(params,getToken(), new ResponseCallback() {
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

    /***
     * ????????????
     */
    public void showDialog(List<NewRateBean> mList) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.home_quote_type_dialog, null);
        Button data_bill_dialog_btn = view.findViewById(R.id.data_bill_dialog_btn);
        MyGridView data_bill_dialog_grid = view.findViewById(R.id.data_bill_dialog_grid);
        madapter = new NewMerchantsGridViewAdapter(AddMerchantsActivity1.this, mList);
        data_bill_dialog_grid.setAdapter(madapter);
        data_bill_dialog_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //????????????position?????????adapter?????????
                madapter.changeState(i);
                feilv_tv.setText(mList.get(i).getFeeValue());
                rateId = mList.get(i).getFeeId();
            }
        });
        Dialog dialog = new MyDialog(AddMerchantsActivity1.this, true, true, (float) 1).setNewView(view);
        dialog.show();
        data_bill_dialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    /**
     * ???????????????????????????
     * @param title
     */
    private void isDialog(String title){
        View view = LayoutInflater.from(mContext).inflate(R.layout.is_dialog_layout, null);
        TextView textView = view.findViewById(R.id.dialog_tv1);
        TextView dialog_determine = view.findViewById(R.id.dialog_determine);
        textView.setText(title);
        Dialog dialog = new MyDialog1(AddMerchantsActivity1.this, true, true, (float) 0.7).setNewView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);;
        dialog.show();
        dialog_determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //??????
                dialog.dismiss();
                finish();
            }
        });
    }
    /************************************** ???????????????????????????--????????????????????????????????????--****************************************/
    private Handler mHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                case 0:
                    provinceList = (List) msg.obj;
                    mProvinceAdapter.notifyDataSetChanged();
                    mCityListView.setAdapter(mProvinceAdapter);
                    break;
                case 1:
                    cityList = (List) msg.obj;
                    mCityAdapter.notifyDataSetChanged();
                    if (cityList != null && !cityList.isEmpty()) {
                        mCityListView.setAdapter(mCityAdapter);
                        tabIndex = 1;
                    }
                    break;
                case 2:
                    areaList = (List) msg.obj;
                    mAreaAdapter.notifyDataSetChanged();
                    if (areaList != null && !areaList.isEmpty()) {
                        mCityListView.setAdapter(mAreaAdapter);
                        tabIndex = 2;
                    }
            }
            updateTabsStyle(tabIndex);
            updateIndicator();
            return true;
        }
    });

    public void showCityPicker() {
        initJDCityPickerPop();
        if (!isShow()) {
            popWindow.showAtLocation(popView, 80, 0, 0);
        }

    }

    private void initJDCityPickerPop() {
        tabIndex = 0;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        popView = layoutInflater.inflate(R.layout.pop_jdcitypicker, (ViewGroup) null);
        mCityListView = (ListView) popView.findViewById(R.id.city_listview);
        mProTv = (TextView) popView.findViewById(R.id.province_tv);
        mCityTv = (TextView) popView.findViewById(R.id.city_tv);
        mAreaTv = (TextView) popView.findViewById(R.id.area_tv);
        mCloseImg = (ImageView) popView.findViewById(R.id.close_img);
        mSelectedLine = popView.findViewById(R.id.selected_line);
        popWindow = new PopupWindow(popView, -1, -2);
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setBackgroundDrawable(new ColorDrawable());
        popWindow.setTouchable(true);
        popWindow.setOutsideTouchable(false);
        popWindow.setFocusable(true);
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                Utils.setBackgroundAlpha(context, 1.0F);
            }
        });
        mCloseImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hidePop();
                Utils.setBackgroundAlpha(context, 1.0F);
            }
        });
        mProTv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tabIndex = 0;
                if (mProvinceAdapter != null) {
                    mCityListView.setAdapter(mProvinceAdapter);
                    if (mProvinceAdapter.getSelectedPosition() != -1) {
                        mCityListView.setSelection(mProvinceAdapter.getSelectedPosition());
                    }
                }

                updateTabVisible();
                updateIndicator();
            }
        });
        mCityTv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tabIndex = 1;
                if (mCityAdapter != null) {
                    mCityListView.setAdapter(mCityAdapter);
                    if (mCityAdapter.getSelectedPosition() != -1) {
                        mCityListView.setSelection(mCityAdapter.getSelectedPosition());
                    }
                }

                updateTabVisible();
                updateIndicator();
            }
        });
        mAreaTv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tabIndex = 2;
                if (mAreaAdapter != null) {
                    mCityListView.setAdapter(mAreaAdapter);
                    if (mAreaAdapter.getSelectedPosition() != -1) {
                        mCityListView.setSelection(mAreaAdapter.getSelectedPosition());
                    }
                }

                updateTabVisible();
                updateIndicator();
            }
        });
        mCityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedList(position);
            }
        });
        Utils.setBackgroundAlpha(context, 0.5F);
        updateIndicator();
        updateTabsStyle(-1);
        posCity();
    }

    @SuppressLint("WrongConstant")
    private void updateTabsStyle(int tabIndex) {
        switch (tabIndex) {
            case -1:
            case 0:
                mProTv.setTextColor(Color.parseColor(colorAlert));
                mProTv.setVisibility(0);
                mCityTv.setVisibility(8);
                mAreaTv.setVisibility(8);
                break;
            case 1:
                mProTv.setTextColor(Color.parseColor(colorSelected));
                mCityTv.setTextColor(Color.parseColor(colorAlert));
                mProTv.setVisibility(0);
                mCityTv.setVisibility(0);
                mAreaTv.setVisibility(8);
                break;
            case 2:
                mProTv.setTextColor(Color.parseColor(colorSelected));
                mCityTv.setTextColor(Color.parseColor(colorSelected));
                mAreaTv.setTextColor(Color.parseColor(colorAlert));
                mProTv.setVisibility(0);
                mCityTv.setVisibility(0);
                mAreaTv.setVisibility(0);
        }

    }

    private void updateIndicator() {
        popView.post(new Runnable() {
            public void run() {
                switch (tabIndex) {
                    case 0:
                        tabSelectedIndicatorAnimation(mProTv).start();
                        break;
                    case 1:
                        tabSelectedIndicatorAnimation(mCityTv).start();
                        break;
                    case 2:
                        tabSelectedIndicatorAnimation(mAreaTv).start();
                }

            }
        });
    }

    private AnimatorSet tabSelectedIndicatorAnimation(TextView tab) {
        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(mSelectedLine, "X", new float[]{mSelectedLine.getX(), tab.getX()});
        final ViewGroup.LayoutParams params = mSelectedLine.getLayoutParams();
        ValueAnimator widthAnimator = ValueAnimator.ofInt(new int[]{params.width, tab.getMeasuredWidth()});
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                params.width = (Integer) animation.getAnimatedValue();
                mSelectedLine.setLayoutParams(params);
            }
        });
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new FastOutSlowInInterpolator());
        set.playTogether(new Animator[]{xAnimator, widthAnimator});
        return set;
    }

    private void hidePop() {
        if (isShow()) {
            popWindow.dismiss();
        }
    }

    private boolean isShow() {
        return popWindow.isShowing();
    }

    @SuppressLint("WrongConstant")
    private void updateTabVisible() {
        mProTv.setVisibility(provinceList != null && !provinceList.isEmpty() ? 0 : 8);
        mCityTv.setVisibility(cityList != null && !cityList.isEmpty() ? 0 : 8);
        mAreaTv.setVisibility(areaList != null && !areaList.isEmpty() ? 0 : 8);
    }

    private void selectedList(int position) {
        switch (tabIndex) {
            case 0:
                NewProvinceBean provinceBean = mProvinceAdapter.getItem(position);
                if (provinceBean != null) {
                    mProTv.setText("" + provinceBean.getAreaName());
                    mCityTv.setText("?????????");
                    mProvinceAdapter.updateSelectedPosition(position);
                    mProvinceAdapter.notifyDataSetChanged();
                    posCity1(mProvinceAdapter.getItem(position).getAreaCode());
                }
                break;
            case 1:
                NewCityBean cityBean = mCityAdapter.getItem(position);
                if (cityBean != null) {
                    mCityTv.setText("" + cityBean.getAreaName());
                    mAreaTv.setText("?????????");
                    mCityAdapter.updateSelectedPosition(position);
                    mCityAdapter.notifyDataSetChanged();
                    posCity2(mCityAdapter.getItem(position).getAreaCode());
                }
                break;
            case 2:
                NewDistrictBean districtBean = mAreaAdapter.getItem(position);
                if (districtBean != null) {
                    callback(districtBean);
                }
        }

    }

    private void callback(NewDistrictBean districtBean) {
        NewProvinceBean provinceBean = provinceList != null && !provinceList.isEmpty() && mProvinceAdapter != null && mProvinceAdapter.getSelectedPosition() != -1 ? (NewProvinceBean) provinceList.get(mProvinceAdapter.getSelectedPosition()) : null;
        NewCityBean cityBean = cityList != null && !cityList.isEmpty() && mCityAdapter != null && mCityAdapter.getSelectedPosition() != -1 ? (NewCityBean) cityList.get(mCityAdapter.getSelectedPosition()) : null;
        //???????????????
        province = provinceBean.getAreaCode();
        city = cityBean.getAreaCode();
        area = districtBean.getAreaCode();

        provinceName = provinceBean.getAreaName();
        cityName = cityBean.getAreaName();
        areaName = districtBean.getAreaName();
        Log.e("??????", provinceBean.getAreaCode() + "" + cityBean.getAreaCode() + "" + districtBean.getAreaCode());
        province_tv.setText(provinceBean + "-" + cityBean + "-" + districtBean);
        hidePop();
    }

    // ?????????????????????
    public void posCity() {
        RequestParams params = new RequestParams();
        params.put("areaLevel", "1");
        params.put("parentCode", "");
        HttpRequest.getArea(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                ceshi(responseObj);
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

    private void ceshi(Object responseObj) {
        //???????????????????????????
        Gson gson = new GsonBuilder().serializeNulls().create();
        try {
            JSONObject result = new JSONObject(responseObj.toString());
            provinceList = gson.fromJson(result.getJSONArray("data").toString(),
                    new TypeToken<List<NewProvinceBean>>() {
                    }.getType());
            if (provinceList != null && !provinceList.isEmpty()) {
                mProvinceAdapter = new NewProvinceAdapter(context, provinceList);
                mCityListView.setAdapter(mProvinceAdapter);
            } else {
                Log.e("MainActivity.tshi", "?????????????????????????????????");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // ?????????????????????
    public void posCity1(String code) {
        RequestParams params = new RequestParams();
        params.put("areaLevel", "2");
        params.put("parentCode", code);
        HttpRequest.getArea(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //???????????????????????????
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    cityList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<NewCityBean>>() {
                            }.getType());
                    mCityAdapter = new NewCityAdapter(context, cityList);
                    mHandler.sendMessage(Message.obtain(mHandler, 1, cityList));

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

    // ??????????????????
    public void posCity2(String code) {
        RequestParams params = new RequestParams();
        params.put("areaLevel", "3");
        params.put("parentCode", code);
        HttpRequest.getArea(params,getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //???????????????????????????
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    areaList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<NewDistrictBean>>() {
                            }.getType());
                    mAreaAdapter = new NewAreaAdapter(context, areaList);
                    mHandler.sendMessage(Message.obtain(mHandler, 2, areaList));
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
    /***************************************** ??????????????????????????? --????????????????????????????????????***************************************************************/
}

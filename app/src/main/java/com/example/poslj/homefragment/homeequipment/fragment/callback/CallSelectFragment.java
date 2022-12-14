package com.example.poslj.homefragment.homeequipment.fragment.callback;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poslj.R;
import com.example.poslj.base.BaseFragment;
import com.example.poslj.bean.MerMachineBean;
import com.example.poslj.cap.android.CaptureActivity;
import com.example.poslj.cap.bean.ZxingConfig;
import com.example.poslj.cap.common.Constant;
import com.example.poslj.homefragment.homeequipment.activity.TransferCallbackActivity;
import com.example.poslj.homefragment.homeequipment.adapter.CeshiAdapter;
import com.example.poslj.homefragment.homeequipment.adapter.ChooserRecyclerAdapter;
import com.example.poslj.homefragment.homeequipment.adapter.RecyclerViewItemDecoration;
import com.example.poslj.homefragment.homeequipment.bean.CallbackEvenBusBean;
import com.example.poslj.homefragment.homeequipment.bean.TerminalBean;
import com.example.poslj.homefragment.homeequipment.bean.TerminalEvenBusBean;
import com.example.poslj.homefragment.hometeam.bean.HomeTeamBean;
import com.example.poslj.net.HttpRequest;
import com.example.poslj.net.OkHttpException;
import com.example.poslj.net.RequestParams;
import com.example.poslj.net.ResponseCallback;
import com.example.poslj.net.Utils;
import com.example.poslj.utils.SPUtils;
import com.example.poslj.views.MyDialog1;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * ??????: qgl
 * ???????????????2020/12/23
 * ??????:????????????
 */
public class CallSelectFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, PullLoadMoreRecyclerView.PullLoadMoreListener {
    //???????????????????????????ID
    private static String UserId = "";
    //?????????????????????
    private ImageView scan_code_btn;
    //?????????????????????
    private final int REQUEST_CODE_SCAN = 10;
    //????????????
    private Button serch_code_btn;
    //??????
    private int mCount = 1;
    //??????????????????
    private int pageSize = 20;
    //????????????
    private TextView callback_num_tv;
    //???????????????
    private EditText callback_query_ed_search;
    //??????????????????
    PullLoadMoreRecyclerView callback_listview;
    private RecyclerView mRecyclerView;
    //????????????
    private SwipeRefreshLayout swipe_layout;
    //??????????????????????????? ??????
    private AppBarLayout appBarLayout;
    //?????????
    private RecyclerViewItemDecoration recyclerViewItemDecoration;
    //???????????????
    private CeshiAdapter ceshiAdapter;
    //????????????
    private String sertch_value = "";
    //??????Bean
    private List<TerminalBean> beans = new ArrayList<>();
    private List<TerminalBean> beanList_size = new ArrayList<>();
    //??????????????????
    private RelativeLayout emptyBg;
    //???????????????
    private TextView callback_transfer_number;
    //????????????
    private boolean isType = false;
    //?????????
    private TextView check_box_type;
    //????????????
    private CheckBox check_box;
    //??????????????????
    private Button bt_sub;
    //?????????????????????pos???value???
    private int[] data;
    /**
     * ??????activity??????
     *
     * @param requestJson
     * @return
     */
    public static CallSelectFragment newInstance(String requestJson) {
        CallSelectFragment fragment = new CallSelectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        UserId = requestJson;
        return fragment;
    }

    @Override
    protected int getLayoutInflaterResId() {
        return R.layout.select_fragment;
    }

    @Override
    protected void initView(View rootView) {
        mContext = getActivity();
        //EventBus.getDefault().register(this);
        scan_code_btn = rootView.findViewById(R.id.scan_code_btn);
        serch_code_btn = rootView.findViewById(R.id.serch_code_btn);
        callback_num_tv = rootView.findViewById(R.id.callback_num_tv);
        callback_query_ed_search = rootView.findViewById(R.id.callback_query_ed_search);
        callback_listview = rootView.findViewById(R.id.callback_listview);
        swipe_layout = rootView.findViewById(R.id.swipe_layout);
        appBarLayout = rootView.findViewById(R.id.appBarLayout);
        emptyBg = rootView.findViewById(R.id.emptyBg);
        callback_transfer_number = rootView.findViewById(R.id.callback_transfer_number);
        check_box_type = rootView.findViewById(R.id.check_box_type);
        check_box = rootView.findViewById(R.id.check_box);
        bt_sub = rootView.findViewById(R.id.bt_sub);
        initList();
    }

    @Override
    protected void initListener() {
        //??????????????????????????????
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset >= 0) {
                    swipe_layout.setEnabled(true);
                } else {
                    swipe_layout.setEnabled(false);
                }
            }
        });
        scan_code_btn.setOnClickListener(this);
        serch_code_btn.setOnClickListener(this);
        check_box.setOnClickListener(this);
        bt_sub.setOnClickListener(this);
    }

    private void initList() {
        recyclerViewItemDecoration = new RecyclerViewItemDecoration(getActivity(), 1);
        //????????????
        swipe_layout.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        swipe_layout.setOnRefreshListener(this);
        //??????mRecyclerView??????
        mRecyclerView = callback_listview.getRecyclerView();
        //????????????scrollbar?????????????????????
        mRecyclerView.setVerticalScrollBarEnabled(true);
        //??????????????????
        callback_listview.setPullRefreshEnable(false);
        mRecyclerView.addItemDecoration(recyclerViewItemDecoration);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //????????????????????????
        callback_listview.setFooterViewText("loading");
        callback_listview.setLinearLayout();
        callback_listview.setOnPullLoadMoreListener(this);
        ceshiAdapter = new CeshiAdapter(getActivity());
        callback_listview.setAdapter(ceshiAdapter);
        postData();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //EventBus.getDefault().unregister(this);

    }

    public void postData() {
        RequestParams params = new RequestParams();
        params.put("merchId", UserId);
        params.put("posActivateStatus", "0");
        params.put("pageNo", mCount + "");
        params.put("pageSize", pageSize + "");
        params.put("posCode", sertch_value);
        params.put("operType", "2"); // 1. ?????? 2.??????
        HttpRequest.getPosList(params, "",new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                swipe_layout.setRefreshing(false);
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    List<TerminalBean> memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<TerminalBean>>() {
                            }.getType());
                    beans.addAll(memberList);
                    callback_num_tv.setText(result.getString("totalNum") + "");
                    ceshiAdapter.addAllData(memberList);
                    if (mCount == 1 && memberList.size() == 0) {
                        emptyBg.setVisibility(View.VISIBLE);
                    } else {
                        emptyBg.setVisibility(View.GONE);
                    }
                    callback_listview.setPullLoadMoreCompleted();
                    ceshiAdapter.setOnAddClickListener(onItemActionClick);
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

    /*************Adapter????????????********************/
    CeshiAdapter.OnAddClickListener onItemActionClick = new CeshiAdapter.OnAddClickListener() {
        @Override
        public void onItemClick() {
            Log.e("???", "??????");
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        int len = 0;
                        int lenght = beans.size();
                        if (lenght >= 1) {
                            for (int i = 0; i < lenght; i++) {
                                if (ceshiAdapter.ischeck.get(i, false)) {
                                    len = len + 1;
                                }
                            }
                            callback_transfer_number.setText("??????:" + len + "???");
                            if (len == 0) {
                                isType = false;
                                check_box_type.setText("??????");
                                check_box.setChecked(false);

                            } else if (len > 0 & len < lenght) {
                                isType = false;
                                check_box_type.setText("??????");
                                check_box.setChecked(false);

                            } else if (len == lenght) {
                                isType = true;
                                check_box_type.setText("??????");
                                check_box.setChecked(true);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    @Override
    public void onRefresh() {
        ceshiAdapter.setAllSelect();
        ceshiAdapter.clearData();
        swipe_layout.setRefreshing(true);
        callback_transfer_number.setText("??????:" + "0" + "???");
        check_box.setChecked(false);
        beans.clear();
        beanList_size.clear();
        sertch_value = "";
        mCount = 1;
        postData();
    }

    @Override
    public void onLoadMore() {
        Log.e("??????","???????????????");
        mCount = mCount + 1;
        postData();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //?????????????????????
            case R.id.scan_code_btn:
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                /*ZxingConfig????????????  ?????????????????????????????????????????????????????????????????????????????????  ???????????????
                 * ???????????????????????????
                 * ????????????  ???????????????????????????  ????????????true
                 * */
                ZxingConfig config = new ZxingConfig();
                config.setShowbottomLayout(false);//??????????????????????????????????????????
                config.setDecodeBarCode(true);//????????????????????? ?????????true
                config.setFullScreenScan(true);
                //config.setPlayBeep(true);//?????????????????????
                //config.setShake(true);//????????????
                //config.setShowAlbum(true);//??????????????????
                //config.setShowFlashLight(true);//?????????????????????
                intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
                break;
            //????????????
            case R.id.serch_code_btn:
                sertch_value = callback_query_ed_search.getText().toString();
                ceshiAdapter.setAllSelect();
                ceshiAdapter.clearData();
                swipe_layout.setRefreshing(true);
                callback_transfer_number.setText("??????:" + "0" + "???");
                check_box.setChecked(false);
                beans.clear();
                beanList_size.clear();
                mCount = 1;
                postData();
                break;
            case R.id.check_box:
                try {
                    if (!beans.isEmpty()) {
                        if (isType) {
                            ceshiAdapter.setAllSelect();
                            isType = false;
                            callback_transfer_number.setText("??????:" + 0 + "???");
                        } else {
                            isType = true;
                            ceshiAdapter.getAllSelect();
                            callback_transfer_number.setText("??????:" + beans.size() + "???");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_sub:
                StringBuffer sb = new StringBuffer();
                beanList_size = new ArrayList<>();
                //?????????????????????
                for (int i = 0; i < beans.size(); i++) {
                    if (ceshiAdapter.ischeck.get(i, false)) {
                        sb.append(beans.get(i).getPosId().toString());
                        beanList_size.add(beans.get(i));
                    }
                }
                if (sb.toString().equals("")) {
                    Toast.makeText(getActivity(), "???????????????????????????", Toast.LENGTH_LONG).show();
                } else {
                    showDialog();
                }
                break;
        }
    }

    //????????????
    private void showDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_select_fragment, null);
        TextView textView = view.findViewById(R.id.dialog_tv1);
        TextView dialog_cancel = view.findViewById(R.id.dialog_cancel);
        TextView dialog_determine = view.findViewById(R.id.dialog_determine);
        textView.setText("???" + beanList_size.size() + "???,?????????" + beanList_size.size() + "???");
        Dialog dialog = new MyDialog1(getActivity(), true, true, (float) 0.7).setNewView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog_determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //??????
                dialog.dismiss();
                //???????????????????????????
                data = new int[beanList_size.size()];
                for (int i = 0; i < beanList_size.size(); i++) {
                    data[i] = Integer.valueOf(beanList_size.get(i).getPosId());
                }
                SubMit();
            }
        });
    }

    //??????????????????
    public void SubMit() {
        // ???????????????
        loadDialog.show();
        RequestParams params = new RequestParams();
        params.put("userId", getUserId());
        params.put("merchId", UserId);
        params.put("operType", "2");  //1????????????2 ??????
        HttpRequest.updPosListFrom(params, data, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                // ???????????????
                loadDialog.dismiss();
                showToast( "????????????");
                //???????????????????????????
                onRefresh();
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                // ???????????????
                loadDialog.dismiss();
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // ???????????????/????????????
        if (requestCode == REQUEST_CODE_SCAN) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                callback_query_ed_search.setText(content);
            }
        }
    }
}

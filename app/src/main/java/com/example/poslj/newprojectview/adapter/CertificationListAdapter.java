package com.example.poslj.newprojectview.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.poslj.R;
import com.example.poslj.newprojectview.bean.CertificationBean;

import java.util.List;

import static com.example.poslj.utils.NumberUtil.isBankNo;

/**
 * 作者: qgl
 * 创建日期：2023/4/27
 * 描述:银行卡列表adapter
 */
public class CertificationListAdapter extends BaseQuickAdapter<CertificationBean,BaseViewHolder> {

    public CertificationListAdapter(int layoutResId, @Nullable List<CertificationBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CertificationBean item) {

        helper.setText(R.id.bank_num,"卡号："+isBankNo(item.getBankCardNo()));
    }
}
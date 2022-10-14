package com.example.poslj.homefragment.homemerchants.memerchants.adapter;

import android.support.annotation.Nullable;
import android.text.Html;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.poslj.R;
import com.example.poslj.homefragment.homemerchants.memerchants.bean.MerchantsDetailBean;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2022/3/21
 * 描述:
 */
public class MerchatsDetailAdapter extends BaseQuickAdapter<MerchantsDetailBean, BaseViewHolder> {


    public MerchatsDetailAdapter(int layoutResId, @Nullable List<MerchantsDetailBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MerchantsDetailBean item) {
        helper.setText(R.id.item_sn_num_tv,"SN号："+item.getSn());
        if (item.getTerminalNo() == null){
            helper.setText(R.id.item_terminal_num_tv,"终端编号：");
        }else {
            helper.setText(R.id.item_terminal_num_tv,"终端编号："+item.getTerminalNo());
        }
        String str1 = "本月交易：<font color= \"#52aaff\">" +item.getMouthAmount()+"元</font>";
        String str2 = "累计交易：<font color= \"#52aaff\">" +item.getSumAmount()+"元</font>";
        helper.setText(R.id.item_month_money_tv, Html.fromHtml(str1));
        helper.setText(R.id.item_month_score_tv,item.getMouthCount() + "笔");
        helper.setText(R.id.item_total_money_tv,Html.fromHtml(str2));
        helper.setText(R.id.item_total_score_tv,item.getSumCount()+ "笔");
    }
}
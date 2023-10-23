package com.example.poslj.newprojectview.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.poslj.R;
import com.example.poslj.newprojectview.bean.CerProvinceBean;

import java.util.List;


/**
 * 作者: qgl
 * 创建日期：2021/1/25
 * 描述:银行卡认证
 */
public class CerProvinceAdapter extends BaseAdapter {
    Context context;
    List<CerProvinceBean> mProList;
    private int provinceIndex = -1;

    public CerProvinceAdapter(Context context, List<CerProvinceBean> mProList) {
        this.context = context;
        this.mProList = mProList;
    }

    public void updateSelectedPosition(int index) {
        this.provinceIndex = index;
    }

    public int getSelectedPosition() {
        return this.provinceIndex;
    }

    public int getCount() {
        return this.mProList.size();
    }

    public CerProvinceBean getItem(int position) {
        return (CerProvinceBean)this.mProList.get(position);
    }

    public long getItemId(int position) {
        return Long.parseLong(((CerProvinceBean)this.mProList.get(position)).getCode());
    }

    @SuppressLint("WrongConstant")
    public View getView(int position, View convertView, ViewGroup parent) {
       CerProvinceAdapter.Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_jdcitypicker_item, parent, false);
            holder = new CerProvinceAdapter.Holder();
            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.selectImg = (ImageView)convertView.findViewById(R.id.selectImg);
            convertView.setTag(holder);
        } else {
            holder = (CerProvinceAdapter.Holder)convertView.getTag();
        }

        CerProvinceBean item = this.getItem(position);
        holder.name.setText(item.getName());
        boolean checked = this.provinceIndex != -1 && ((CerProvinceBean)this.mProList.get(this.provinceIndex)).getName().equals(item.getName());
        holder.name.setEnabled(!checked);
        holder.selectImg.setVisibility(checked ? 8 : 8);
        return convertView;
    }

    class Holder {
        TextView name;
        ImageView selectImg;

        Holder() {
        }
    }
}


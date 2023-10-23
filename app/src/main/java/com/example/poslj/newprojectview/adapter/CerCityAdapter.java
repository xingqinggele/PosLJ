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
import com.example.poslj.newprojectview.bean.CerCityBean;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/1/25
 * 描述:
 */
public class CerCityAdapter extends BaseAdapter {
    Context context;
    List<CerCityBean> mCityList;
    private int cityIndex = -1;

    public CerCityAdapter(Context context, List<CerCityBean> mCityList) {
        this.context = context;
        this.mCityList = mCityList;
    }

    public int getSelectedPosition() {
        return this.cityIndex;
    }

    public void updateSelectedPosition(int index) {
        this.cityIndex = index;
    }

    public int getCount() {
        return this.mCityList.size();
    }

    public CerCityBean getItem(int position) {
        return (CerCityBean)this.mCityList.get(position);
    }

    public long getItemId(int position) {
        return Long.parseLong(((CerCityBean)this.mCityList.get(position)).getCode());
    }

    @SuppressLint("WrongConstant")
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_jdcitypicker_item, parent, false);
            holder = new Holder();
            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.selectImg = (ImageView)convertView.findViewById(R.id.selectImg);
            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }

        CerCityBean item = this.getItem(position);
        holder.name.setText(item.getName());
        boolean checked = this.cityIndex != -1 && ((CerCityBean)this.mCityList.get(this.cityIndex)).getName().equals(item.getName());
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

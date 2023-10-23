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
import com.example.poslj.newprojectview.bean.CerAreaBean;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/1/25
 * 描述:
 */
public class CerAreaAdapter extends BaseAdapter {
    Context context;
    List<CerAreaBean> mDistrictList;
    private int districtIndex = -1;

    public CerAreaAdapter(Context context, List<CerAreaBean> mDistrictList) {
        this.context = context;
        this.mDistrictList = mDistrictList;
    }

    public int getSelectedPosition() {
        return this.districtIndex;
    }

    public void updateSelectedPosition(int index) {
        this.districtIndex = index;
    }

    public int getCount() {
        return this.mDistrictList.size();
    }

    public CerAreaBean getItem(int position) {
        return (CerAreaBean)this.mDistrictList.get(position);
    }

    public long getItemId(int position) {
        return Long.parseLong(((CerAreaBean)this.mDistrictList.get(position)).getCode());
    }

    @SuppressLint("WrongConstant")
    public View getView(int position, View convertView, ViewGroup parent) {
       CerAreaAdapter.Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_jdcitypicker_item, parent, false);
            holder = new CerAreaAdapter.Holder();
            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.selectImg = (ImageView)convertView.findViewById(R.id.selectImg);
            convertView.setTag(holder);
        } else {
            holder = (CerAreaAdapter.Holder)convertView.getTag();
        }

        CerAreaBean item = this.getItem(position);
        holder.name.setText(item.getName());
        boolean checked = this.districtIndex != -1 && ((CerAreaBean)this.mDistrictList.get(this.districtIndex)).getName().equals(item.getName());
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


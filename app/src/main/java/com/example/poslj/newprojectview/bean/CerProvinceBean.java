package com.example.poslj.newprojectview.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 作者: qgl
 * 创建日期：2023/4/28
 * 描述:银行卡认证Bean
 */


public class CerProvinceBean implements Parcelable {
    private String code;
    private String name;
    private String parent;
    private String level;
    private ArrayList<CerCityBean> cityList;

    public static final Creator<CerProvinceBean> CREATOR = new Creator<CerProvinceBean>() {
        public CerProvinceBean createFromParcel(Parcel source) {
            return new CerProvinceBean(source);
        }

        public CerProvinceBean[] newArray(int size) {
            return new CerProvinceBean[size];
        }
    };

    @Override
    public String toString() {
        return name;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.name);
        dest.writeTypedList(this.cityList);
    }

    protected CerProvinceBean(Parcel in) {
        this.code = in.readString();
        this.name = in.readString();
        this.cityList = in.createTypedArrayList(CerCityBean.CREATOR);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public ArrayList<CerCityBean> getCityList() {
        return cityList;
    }

    public void setCityList(ArrayList<CerCityBean> cityList) {
        this.cityList = cityList;
    }

}
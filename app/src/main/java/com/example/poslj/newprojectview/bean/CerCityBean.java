package com.example.poslj.newprojectview.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 作者: qgl
 * 创建日期：2023/4/28
 * 描述:银行卡认证省
 */
public class CerCityBean implements Parcelable {
    private String code;
    private String name;
    private String parent;
    private String level;

    private ArrayList<CerAreaBean> cityList;
    public static final Creator<CerCityBean> CREATOR = new Creator<CerCityBean>() {
        public CerCityBean createFromParcel(Parcel source) {
            return new CerCityBean(source);
        }

        public CerCityBean[] newArray(int size) {
            return new CerCityBean[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return name;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.name);
        dest.writeTypedList(this.cityList);
    }

    public CerCityBean() {
    }

    protected CerCityBean(Parcel in) {
        this.code = in.readString();
        this.name = in.readString();
        this.cityList = in.createTypedArrayList(CerAreaBean.CREATOR);
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

    public ArrayList<CerAreaBean> getCityList() {
        return cityList;
    }

    public void setCityList(ArrayList<CerAreaBean> cityList) {
        this.cityList = cityList;
    }
}
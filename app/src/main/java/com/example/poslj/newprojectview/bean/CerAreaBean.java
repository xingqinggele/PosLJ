package com.example.poslj.newprojectview.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者: qgl
 * 创建日期：2023/4/28
 * 描述:银行卡认证区
 */
public class CerAreaBean implements Parcelable {
    private String code;
    private String name;
    private String parent;
    private String level;

    public static final Creator<CerAreaBean> CREATOR = new Creator<CerAreaBean>() {
        public CerAreaBean createFromParcel(Parcel source) {
            return new CerAreaBean(source);
        }

        public CerAreaBean[] newArray(int size) {
            return new CerAreaBean[size];
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
    }

    protected CerAreaBean(Parcel in) {
        this.code = in.readString();
        this.name = in.readString();
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
}
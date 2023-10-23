package com.example.poslj.newprojectview.bean;

/**
 * 作者: qgl
 * 创建日期：2023/6/20
 * 描述:进件图片存储Bean
 */
public class ImageGridBean {
    private String id;
    private String url;
    private int placeholderimage;
    private String imagename;
    private String neturl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPlaceholderimage() {
        return placeholderimage;
    }

    public void setPlaceholderimage(int placeholderimage) {
        this.placeholderimage = placeholderimage;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public String getNeturl() {
        return neturl;
    }

    public void setNeturl(String neturl) {
        this.neturl = neturl;
    }
}
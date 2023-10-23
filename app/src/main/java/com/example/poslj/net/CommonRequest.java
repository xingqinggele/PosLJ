package com.example.poslj.net;

import android.text.TextUtils;
import android.util.Log;

import com.example.poslj.bean.OrderBean;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 创建： zb
 * 描述： 公共入参
 */

public class CommonRequest {

  /**
   * 创建Get请求的Request
   */
  public static Request createGetRequest(String url, RequestParams params) {
    StringBuilder urlBuilder = new StringBuilder(url).append("/");
    if (params != null) {
      for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
        urlBuilder
                .append(entry.getValue())
                .append("/");
      }
    }

    return new Request.Builder().url(urlBuilder.substring(0, urlBuilder.length() - 1))
        .get().build();
  }

  /**
   * 创建Get请求的Request
   */
  public static Request createGetRequest2(String url,String bearer, RequestParams params) {
    StringBuilder urlBuilder = new StringBuilder(url).append("?");

    if (params != null) {
      for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
        urlBuilder
                .append(entry.getKey())
                .append("=")
                .append(entry.getValue())
                .append("&");
      }
    }
    return new Request.Builder().addHeader("Authorization",bearer).url(urlBuilder.substring(0, urlBuilder.length() - 1)).get().build();
  }



  /**
   * 创建Post请求的Request
   * application/json
   * bearer token
   * @return 返回一个创建好的Request对象
   */
  public static Request createPostRequest(String url,String bearer,RequestParams params) {
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    //将请求参数逐一遍历添加到我们的请求构建类中
    Map<String, String> map = new HashMap<String, String>();
    for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
      map.put(entry.getKey(),entry.getValue());
    }
    JSONObject json2 =new JSONObject(map);

    if (!TextUtils.isEmpty(bearer)){
      bearer = "Bearer "+bearer;
    }else {
      bearer = "";
    }
    Log.e("llll----","....."+json2);
    RequestBody body = RequestBody.create(JSON,json2+"");
    Request request = new Request.Builder()
        .addHeader("Authorization",bearer)
        .url(url)
        .post(body)
        .build();
    return request;
  }

  /**
   * 接口传递List数据
   * @param url
   * @param params
   * @param data
   * @return
   */
  public static Request createPostRequest2(String url,RequestParams params,int [] data) {
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    //将请求参数逐一遍历添加到我们的请求构建类中
    Map<String, Object> map = new HashMap<String, Object>();
    for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
      map.put(entry.getKey(),entry.getValue());
    }
    map.put("posIds",data);
    //map转json对象
    Gson gson = new Gson();
    String jsonString_2 = gson.toJson(map);
    JsonObject jsonObject_2 = new JsonParser().parse(jsonString_2).getAsJsonObject();
    RequestBody body = RequestBody.create(JSON,jsonObject_2+"");
    Request request = new Request.Builder()
            .url(url)
            .post(body)
            .build();
    return request;
  }

  //实体类传输
  public static Request createPostRequest3(String url,RequestParams params,List<OrderBean> data) {
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    //将请求参数逐一遍历添加到我们的请求构建类中
    Map<String, Object> map = new HashMap<String, Object>();
    for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
      map.put(entry.getKey(),entry.getValue());
    }
    map.put("manyPosTypeId",data);
    //map转json对象
    Gson gson = new Gson();
    String jsonString_2 = gson.toJson(map);
    JsonObject jsonObject_2 = new JsonParser().parse(jsonString_2).getAsJsonObject();
    RequestBody body = RequestBody.create(JSON,jsonObject_2+"");
    Request request = new Request.Builder()
            .url(url)
            .post(body)
            .build();
    return request;
  }


  /**
   * 混合form和图片
   * @return 返回一个创建好的Request对象
   */
  public static Request createMultipartRequest(String url, RequestParams params, List<File> files) {

    //构建多部件builder
    MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
    //获取参数并放到请求体中
    try {
      if (params != null) {
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
          //将请求参数逐一遍历添加到我们的请求构建类中
          bodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
          jsonObject.put(entry.getKey(), entry.getValue());
        }
        Log.e("TAG", "入参:   " + jsonObject.toString());
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    //添加图片集合放到请求体中
    if (files != null) {
      for (File f : files) {
        bodyBuilder.addFormDataPart("files", f.getName(),
            RequestBody.create(MediaType.parse("image/png"), f));
      }
    }

    Request request = new Request.Builder()
        .url(url)
        .post(bodyBuilder.build())
        .build();

    return request;
  }


  /**
   * qgl
   * 混合form和图片
   * @return 返回一个创建好的Request对象
   */
  public static Request createMultipartRequest_qgl(String url, RequestParams params, List<File> files) {

    //构建多部件builder
    MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
    //获取参数并放到请求体中
    try {
      if (params != null) {
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
          //将请求参数逐一遍历添加到我们的请求构建类中
          bodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
          jsonObject.put(entry.getKey(), entry.getValue());
        }
        Log.e("TAG", "入参:   " + jsonObject.toString());
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    //添加图片集合放到请求体中
    if (files != null) {
      for (File f : files) {
        bodyBuilder.addFormDataPart("files1", f.getName(),
                RequestBody.create(MediaType.parse("image/png"), f));
      }
    }

    Request request = new Request.Builder()
            .url(url)
            .post(bodyBuilder.build())
            .build();

    return request;
  }


}

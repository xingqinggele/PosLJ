package com.example.poslj.net;

import android.util.Log;

import com.example.poslj.app.MyApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author：zb.

 * 时间：On 2019-05-05.

 * 描述：日志拦截器
 */

public class RequetInterceptor implements Interceptor {
  private String token = "";

    /**
     * 这个chain里面包含了request和response，所以你要什么都可以从这里拿
     */
  @Override
  public Response intercept(Chain chain) throws IOException {

    //获取本地存储的密钥
    if (!MyApp.getApp().getToken().equals("") && !MyApp.getApp().getToken().equals("null") && MyApp.getApp().getToken() != "" && MyApp.getApp().getToken() != null) {
      token = MyApp.getApp().getToken();
    }
    Log.e("TAG", "TOKEN--->" + token);
    Request originalRequest;
    //判断是否是登录接口、登录接口不需要Token
    if (chain.request().url().toString().equals("http://www.poshb.cn:8085/login")) {
      originalRequest = chain
              .request()
              .newBuilder()
              .build();
    } else {
      originalRequest = chain
              .request()
              .newBuilder()
              .header("Authorization", token)
              .build();
    }

    /**
     * 开始时间
     */
    long startTime = System.currentTimeMillis();
    Log.e("TAG","\n"+"requestUrl=" + originalRequest.url());
    String method = originalRequest.method();

    if ("POST".equals(method)) {
      try {
        JSONObject jsonObject = new JSONObject();
        if (originalRequest.body() instanceof FormBody) {
          FormBody body = (FormBody) originalRequest.body();
          for (int i = 0; i < body.size(); i++) {
            jsonObject.put(body.encodedName(i), body.encodedValue(i));
          }
          Log.e("TAG","入参JSON= " + jsonObject.toString());
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }

      Response response = chain.proceed(originalRequest);
    /**
     * 这里不能直接使用response.body().string()的方式输出日志
     * 因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一个新的response给应用层处理
     */
    ResponseBody responseBody = response.peekBody(1024 * 1024);
    Log.e("TAG","出参JSON=" + responseBody.string());
    long endTime = System.currentTimeMillis();
    long duration = endTime - startTime;
    Log.e("TAG","----------" + "耗时:" + duration + "毫秒----------");
    return response;


  }


}

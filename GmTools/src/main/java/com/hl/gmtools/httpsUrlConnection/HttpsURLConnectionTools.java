package com.hl.gmtools.httpsUrlConnection;

import android.content.Context;
import android.util.Log;

import com.hl.gmtools.SSLTools;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * HttpsURLConnection网络请求工具类
 */
public class HttpsURLConnectionTools {

    public static final String TAG = "HttpsURLConnectionTools";

    public static void get(Context mContext, String url, final RequestCallBack callBack) {
        get(mContext, url, new HashMap<>(), callBack);
    }

    /**
     * get请求
     *
     * @param context
     * @param url
     * @param headers
     * @param callBack
     */
    public static void get(Context context, String url, HashMap<String, String> headers, final RequestCallBack callBack) {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
            URL serverUrl = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) serverUrl.openConnection();
            conn.setRequestMethod("GET");
            // 设置 SSLSocketFactory
            conn.setSSLSocketFactory(SSLTools.getSSLSocketFactory(context));

            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
                Log.i(TAG, "header_key:" + entry.getKey() + ",value:" + entry.getValue());
            }

            conn.connect();
            Log.i(TAG, "used cipher suite:" + conn.getCipherSuite());
            Log.i(TAG, "url:" + url);
            StringBuilder content = new StringBuilder();
            try {
                int code = conn.getResponseCode();
                if (code == 200) {
                    InputStream in = conn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
                    BufferedReader reader = new BufferedReader(isr);
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        content.append(line);
                    }
                    Log.i(TAG, "content:" + content);
                    callBack.onSuccess(content.toString());
                } else {
                    String msg = "";
                    switch (code) {
                        case 400:
                            msg = "请求中有语法问题，或不能满足请求";
                            break;
                        case 404:
                            msg = "服务器找不到给定的资源";
                            break;
                        case 500:
                            msg = "服务器不能完成请求";
                            break;
                        default:
                            break;
                    }
                    if (callBack != null) {
                        callBack.onFail(msg);
                    }
                }
            } finally {
                conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (callBack != null) {
                callBack.onError(e);
            }
        }
    }

    /**
     * post请求
     *
     * @param context
     * @param url
     * @param body
     * @param headers
     * @param callBack
     */
    public static void post(Context context, String url, String body, HashMap<String, String> headers, final RequestCallBack callBack) {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
            URL serverUrl = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) serverUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            // 设置 SSLSocketFactory
            conn.setSSLSocketFactory(SSLTools.getSSLSocketFactory(context));

            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
                Log.i(TAG, "header_key:" + entry.getKey() + ",value:" + entry.getValue());
            }

            //加入数据
            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            conn.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            conn.setDoInput(true);
            DataOutputStream out = new DataOutputStream(
                    conn.getOutputStream());
            if (body != null)
                out.writeBytes(body);
            out.flush();
            out.close();

            conn.connect();
            Log.i(TAG, "used cipher suite:" + conn.getCipherSuite());
            Log.i(TAG, "url:" + url);
            Log.i(TAG, "body:" + body);
            StringBuilder content = new StringBuilder();
            try {
                int code = conn.getResponseCode();
                if (code == 200) {
                    InputStream in = conn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
                    BufferedReader reader = new BufferedReader(isr);
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        content.append(line);
                    }
                    Log.i(TAG, "content:" + content);
                    callBack.onSuccess(content.toString());
                } else {
                    String msg = "";
                    switch (code) {
                        case 400:
                            msg = "请求中有语法问题，或不能满足请求";
                            break;
                        case 404:
                            msg = "服务器找不到给定的资源";
                            break;
                        case 500:
                            msg = "服务器不能完成请求";
                            break;
                        default:
                            break;
                    }
                    if (callBack != null) {
                        callBack.onFail(msg);
                    }
                }
            } finally {
                conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (callBack != null) {
                callBack.onError(e);
            }
        }
    }
}

package com.hl.gmtools.okhttp;

import android.content.Context;

import com.aliyun.gmsse.CipherSuite;
import com.aliyun.gmsse.ProtocolVersion;
import com.hl.gmtools.SSLTools;

import java.io.InputStream;
import java.util.Arrays;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;

public class OkHttpTools {
    /**
     * 使 OkHttpClient 支持国密SSL, 使用默认的 root  证书
     *
     * @param builder OkHttpClient建造者对象
     * @param context 上下文
     */
    public static OkHttpClient.Builder supportGM(OkHttpClient.Builder builder, Context context) {
        return supportGM(builder, SSLTools.getDefaultRootInputStream(context));
    }

    /**
     * 使 OkHttpClient 支持国密SSL
     *
     * @param builder                OkHttpClient建造者对象
     * @param certificateInputStream 证书数据流
     */
    public static OkHttpClient.Builder supportGM(OkHttpClient.Builder builder, InputStream certificateInputStream) {
        // 添加支持国密协议 okhttp会校验协议
        ConnectionSpec GM_SPEC = new ConnectionSpec.Builder(true)
                .cipherSuites(CipherSuite.ECC_SM2_WITH_SM4_SM3)
                .tlsVersions(ProtocolVersion.GMTLS_1_1_NAME)
                .build();

        //获取 SSLSocketFactory 和 TrustManagerFactory
        Object[] args = SSLTools.getSSLSocketFactoryAndTrustManagerFactory(certificateInputStream);
        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) args[0];
        TrustManagerFactory trustManagerFactory = (TrustManagerFactory) args[1];

        //获取 X509TrustManager
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }

        X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

        builder.connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT, GM_SPEC))
                .sslSocketFactory(sslSocketFactory, trustManager);
        return builder;
    }
}

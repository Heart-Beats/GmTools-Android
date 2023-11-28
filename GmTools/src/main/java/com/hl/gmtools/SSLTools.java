package com.hl.gmtools;

import android.content.Context;

import com.aliyun.gmsse.GMProvider;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class SSLTools {

    /**
     * 获取 SSLSocketFactory 和 TrustManagerFactory
     *
     * @param context 上下文对象
     */
    public static Object[] getSSLSocketFactoryAndTrustManagerFactory(Context context) {
        try {
            InputStream rootInputStream = getDefaultRootInputStream(context);
            return getSSLSocketFactoryAndTrustManagerFactory(rootInputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取 SSLSocketFactory 和 TrustManagerFactory
     *
     * @param is 证书数据流
     */
    public static Object[] getSSLSocketFactoryAndTrustManagerFactory(InputStream is) {
        try {
            // 初始化 SSLSocketFactory
            GMProvider provider = new GMProvider();
            SSLContext sc = SSLContext.getInstance("TLS", provider);

            BouncyCastleProvider bc = new BouncyCastleProvider();
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            CertificateFactory cf = CertificateFactory.getInstance("X.509", bc);

            X509Certificate cert = (X509Certificate) cf.generateCertificate(is);
            ks.load(null, null);
            ks.setCertificateEntry("gmca", cert);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509", provider);
            tmf.init(ks);

            sc.init(null, tmf.getTrustManagers(), null);

            return new Object[]{sc.getSocketFactory(), tmf};
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取 SSLSocketFactory
     *
     * @param context 上下文对象
     */
    public static SSLSocketFactory getSSLSocketFactory(Context context) {
        Object[] sslSocketFactoryAndTrustManagerFactory = getSSLSocketFactoryAndTrustManagerFactory(context);
        return (SSLSocketFactory) sslSocketFactoryAndTrustManagerFactory[0];
    }

    /**
     * 获取 TrustManagerFactory
     *
     * @param context 上下文对象
     */
    public static TrustManagerFactory getTrustManagerFactory(Context context) {
        Object[] sslSocketFactoryAndTrustManagerFactory = getSSLSocketFactoryAndTrustManagerFactory(context);
        return (TrustManagerFactory) sslSocketFactoryAndTrustManagerFactory[1];
    }


    /**
     * 获取默认的根证书输入流
     */
    public static InputStream getDefaultRootInputStream(Context mContext) {
        InputStream is = null;
        try {
            is = mContext.getAssets().open("root.crt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }
}

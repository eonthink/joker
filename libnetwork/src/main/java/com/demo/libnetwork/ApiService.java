package com.demo.libnetwork;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ApiService {
    public static final OkHttpClient okhttpClient ;
    private static String sBaseUrl;
    public static Convert sConvert;

    static {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

         okhttpClient = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        TrustManager[] trustManagers = new TrustManager[]{
               new X509TrustManager() {
                   @Override
                   public void checkClientTrusted(X509Certificate[] certificates, String s) throws CertificateException {

                   }

                   @Override
                   public void checkServerTrusted(X509Certificate[] certificates, String s) throws CertificateException {

                   }

                   @Override
                   public X509Certificate[] getAcceptedIssuers() {
                       return new X509Certificate[0];
                   }
               }
        };

        try {
            SSLContext ssl = SSLContext.getInstance("SSL");
            ssl.init(null,
                    trustManagers,new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(ssl.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession session) {
                    return true;
                }
            });

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }


    public static void init(String baseUrl,Convert convert){
        sBaseUrl =baseUrl;
        if(convert == null){
            convert =new JsonConvert();
        }
        sConvert=convert;
    }
}
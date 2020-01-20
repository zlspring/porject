package com.aohui.btcorg.util;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class MailgunClient {

    static {
        try {

            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    // TODO Auto-generated method stub

                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    // TODO Auto-generated method stub

                }
            }};


            SSLContext sslcontext = SSLContext.getInstance("SSL");
            sslcontext.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext);
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            Unirest.setHttpClient(httpclient);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String sendSimpleMessage(String domainName, String fromEmail, String fromNickName, String toEmail, String subject, String text, String html) throws UnirestException {
        HttpResponse<String> request = Unirest.post("https://api.mailgun.net/v3/" + domainName + "/messages")
                .basicAuth("api", "key-83c317dd45b766aadb5f9beab856a68d")
                .queryString("from", fromNickName + " <" + fromEmail + ">")
                .queryString("to", toEmail)
                .queryString("subject", subject)
                .queryString("text", text)
                .queryString("html", html)

                .asString();
        return request.getBody();
    }

//  public static void main(String[] args) throws UnirestException {
//
//    String domainName = "aussiebit.com.au";
//    String domainName2 = "auscrypto.org";
//
//    String result = sendSimpleMessage(domainName,"admin@aussiebit.com.au", "Admin", "492187098@qq.com", "This is a test email", "","<p>Hello didi</p>");
//
//    System.out.println(result);
//  }
}
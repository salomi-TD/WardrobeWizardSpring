package com.example.demo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.security.cert.X509Certificate;

public class HttpClientExample {
    public static void main(String[] args) {
        String endpoint = "https://localhost:9002/occ/v2/electronics/upload";

        try {
            CloseableHttpClient httpClient = configureHttpClientWithCustomSSLContext(); // Disable SSL certificate validation
            sendFileViaHttpPost(httpClient, endpoint);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CloseableHttpClient configureHttpClientWithCustomSSLContext() throws Exception {
        TrustManager[] trustAllCertificates = new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());

        return HttpClients.custom()
                .setSslcontext(sslContext)
                .setSSLSocketFactory((LayeredConnectionSocketFactory) new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE))
                .build();
    }

    public static void sendFileViaHttpPost(CloseableHttpClient httpClient, String endpoint) throws Exception {
        HttpPost httpPost = new HttpPost(endpoint);

        File fileToUpload = new File("3547002 (2).jpg");
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.addBinaryBody("file", fileToUpload);
        HttpEntity multipartEntity = entityBuilder.build();
        httpPost.setEntity(multipartEntity);

        // Set headers
        httpPost.addHeader("accept", "application/json");

        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity responseEntity = response.getEntity();

        if (responseEntity != null) {
            // Handle the response, e.g., read the response content
            String responseContent = EntityUtils.toString(responseEntity);
            System.out.println(responseContent);
        }
    }
}

package com.xiao.pro.requester;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xiaoliang on 2015/12/7 11:20
 */
public class HttpRequester implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequester.class);

    protected CloseableHttpClient httpclient = null;
    protected InputStream inputStream = null;
    protected HttpResponse response = null;
    protected HttpClientContext context = HttpClientContext.create();

    private String requestUrl = null;

    public HttpRequester(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public void run() {
        try {
            doGet(requestUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(int timeout) {
        RequestConfig requestConfig = RequestConfig.DEFAULT;
        //设置超时
        if (timeout != 0) {
            requestConfig = RequestConfig.custom()
                    .setConnectTimeout(timeout)
                    .setConnectionRequestTimeout(timeout)
                    .setSocketTimeout(timeout).build();
        }
        context.setRequestConfig(requestConfig);

        //默认失败重发3次
        httpclient = HttpClientBuilder.create()
                .setRetryHandler(new DefaultHttpRequestRetryHandler())
                .build();
    }

    public void closeConnection() {
        if (httpclient != null) {
            try {
                httpclient.close();
            } catch (IOException e) {
                logger.error("close httpclient error");
            }
        }
    }

    public InputStream doGet(String url) throws IOException {
        StringBuffer sb = new StringBuffer();
        HttpGet httpGet = new HttpGet(url);
        response = httpclient.execute(httpGet, context);

        HttpEntity entity = response.getEntity();
        if (entity != null) {
            inputStream = entity.getContent();
        }
        return inputStream;
    }
}

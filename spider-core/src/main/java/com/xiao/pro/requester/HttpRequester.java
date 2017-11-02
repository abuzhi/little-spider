package com.xiao.pro.requester;

import com.xiao.pro.parser.HtmlParser;
import com.xiao.pro.utils.EncryptMD5;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Map;
import java.util.Queue;

/**
 * Created by xiaoliang on 2015/12/7 11:20
 */
public class HttpRequester implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequester.class);

    protected CloseableHttpClient httpclient = null;
    protected HttpClientContext context = HttpClientContext.create();

    private Queue<String> queue;
    private Map<String, String> paserMap;

    public HttpRequester(Queue<String> queue, Map<String, String> paserMap) {
        this.queue = queue;
        this.paserMap = paserMap;
    }

    public void run() {
        while (true) {
            if (!queue.isEmpty()) {
                String url = queue.poll();
                String md5 = EncryptMD5.md5(url);
                try {
                    InputStream inputStream = doGet(url);
                    boolean isok = HtmlParser.parserHtml(inputStream);
                    if (isok) {
                        paserMap.put(md5, "ok");
                    }
                    logger.info("do url = " + url);
                } catch (IOException e) {
                    logger.error("do get error : ", e);
                }


            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.error("do sleep error : ", e);
                }
            }
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
        InputStream inputStream = null;
        HttpResponse response = null;
        HttpGet httpGet = new HttpGet(url);
        response = httpclient.execute(httpGet, context);

        HttpEntity entity = response.getEntity();
        if (entity != null) {
            inputStream = entity.getContent();
        }
        return inputStream;
    }
}

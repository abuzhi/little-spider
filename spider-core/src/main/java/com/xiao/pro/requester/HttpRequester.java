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
public class HttpRequester{

    private static final Logger logger = LoggerFactory.getLogger(HttpRequester.class);

    protected CloseableHttpClient httpclient = null;
    protected HttpClientContext context = HttpClientContext.create();
//
//    private Queue<String> queue;
//    private Map<String, String> paserMap;
//
//    public HttpRequester(Queue<String> queue, Map<String, String> paserMap) {
//        this.queue = queue;
//        this.paserMap = paserMap;
//    }
//
//    public void run() {
//        while (true) {
//            if (!queue.isEmpty()) {
//                String url = queue.poll();
//                String md5 = EncryptMD5.md5(url);
//                if (paserMap.containsKey(md5)) {
//                    continue;
//                }
//
//                try {
//                    Document doc = Jsoup.connect(url).get();
//                    Element content = doc.body();
//                    Elements links = content.getElementsByTag("a");
//
//                    for (Element link : links) {
//                        String linkHref = link.attr("href");
//                        String linkText = link.text();
//                        String md5_href = EncryptMD5.md5(linkHref);
//                        queue.add(linkHref);
//                        paserMap.put(md5_href,"ok");
//                        logger.info("text=" + linkText + " , href=" + linkHref);
//                    }
//
//                } catch (IOException e) {
//                    logger.error("do get error : ", e);
//                }
//
//
//            } else {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    logger.error("do sleep error : ", e);
//                }
//            }
//        }
//    }

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

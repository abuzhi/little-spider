package com.xiao.pro.parser;

import com.xiao.pro.requester.HttpRequester;
import com.xiao.pro.utils.EncryptMD5;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by xiaoliang on 2017/11/26.
 */
public class UrlProducer implements Runnable {

    public static final Logger producerLogger = LoggerFactory.getLogger("producer");
    public static final Logger consumerLogger = LoggerFactory.getLogger("consumer");
    public static final Logger logger = LoggerFactory.getLogger(UrlProducer.class);

    private BlockingQueue<String> producerQueue;
    private BlockingQueue<Map<String, String>> consumerQueue;
    private ConcurrentMap<String, String> paserMap;
    private AtomicLong counter;

    private HttpRequester requester;

    public UrlProducer(BlockingQueue<String> producerQueue, BlockingQueue<Map<String, String>> consumerQueue, ConcurrentMap<String, String> paserMap, AtomicLong counter) {
        this.producerQueue = producerQueue;
        this.consumerQueue = consumerQueue;
        this.paserMap = paserMap;
        this.counter = counter;
        requester = new HttpRequester();
        requester.init(3000);
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (producerQueue.isEmpty()) {
                    Thread.sleep(500);
                } else {
                    String url = producerQueue.take();
                    if (StringUtils.isBlank(url)) {
                        continue;
                    }

                    if (!url.contains("www.xuexi111.com") && !url.contains("ed2k://|file|")) {
                        continue;
                    }
                    try {
                        Document doc = Jsoup.connect(url).get();
                        Element content = doc.body();
                        Elements links = content.select("a");
                        logger.info("count url = " + counter.addAndGet(1));
                        Map<String, String> map = new HashMap<>();

                        for (Element link : links) {
                            String linkHref = link.attr("href").replaceAll("\\n", "");
                            String linkText = link.text().replaceAll("\\n", "");
                            String md5 = EncryptMD5.md5(linkHref);
                            if (paserMap.containsKey(md5)) {
                                logger.info("alread parser url = " + url);
                                continue;
                            }
                            paserMap.put(md5, "ok");
                            if (linkHref.startsWith("http://www.xuexi111.com")) {
                                producerQueue.put(linkHref);
                                producerLogger.info("producerQueue link=" + linkHref + " , text=" + linkText);
                            } else if (linkHref.startsWith("ed2k:")) {
                                map.put(linkHref, linkText);
                                consumerLogger.info("consumerQueue link=" + linkHref + " , text=" + linkText);
                            }
                        }
                        if(!map.isEmpty()){
                            consumerQueue.put(map);
                            logger.info("consumerQueue put map ....");
                        }
                    } catch (IOException e) {
                        logger.error("do get error : ", e);
                    }
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                logger.error("error : ", e);
            }
        }


    }

}

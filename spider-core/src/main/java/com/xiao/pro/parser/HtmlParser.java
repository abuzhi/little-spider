package com.xiao.pro.parser;

import com.xiao.pro.utils.EncryptMD5;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by xiaoliang on 2017/10/30.
 */
public class HtmlParser {

    public static final Logger logger = LoggerFactory.getLogger(HtmlParser.class);

    public boolean producer(BlockingQueue<String> queue, Map<String,String> paserMap) {
        String url = queue.poll();
        String md5 = EncryptMD5.md5(url);
        if (paserMap.containsKey(md5)) {
            return false;
        }
        if(!url.contains("http://www.xuexi111.com") || !url.contains("ed2k://|file|")){
            logger.info("not pattern url = "+url);
            return false;
        }

        try {
            Document doc = Jsoup.connect(url).get();
            Element content = doc.body();
            Elements links = content.getElementsByTag("a");
            for (Element link : links) {
                String linkHref = link.attr("href");
                String linkText = link.text();
                String md5_href = EncryptMD5.md5(linkHref);
                queue.offer(linkHref);
                paserMap.put(md5_href,"ok");
                logger.info("text=" + linkText + " , href=" + linkHref);
            }
        } catch (IOException e) {
            logger.error("do get error : ", e);
        }

        return true;
    }

    public boolean consumer(BlockingQueue<String> queue, Map<String,String> paserMap){
        String url = queue.poll();

        return false;
    }
}

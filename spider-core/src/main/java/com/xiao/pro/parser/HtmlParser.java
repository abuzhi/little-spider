package com.xiao.pro.parser;

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

/**
 * Created by xiaoliang on 2017/10/30.
 */
public class HtmlParser {

    public static final Logger logger = LoggerFactory.getLogger(HtmlParser.class);

    private Queue<String> queue;
    private Map<String,String> paserMap;

    public HtmlParser(Queue<String> queue,Map<String,String> paserMap) {
        this.queue = queue;
        this.paserMap = paserMap;
    }

    public static boolean parserHtml(InputStream  data) throws IOException {
        StringBuffer sb = new StringBuffer();
        Reader reader = new InputStreamReader(data);
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            line = br.readLine();
        }

        Document doc = Jsoup.parse(sb.toString());
        Element content = doc.body();
        Elements links = content.getElementsByTag("a");
        for (Element link : links) {
            String linkHref = link.attr("href");
            String linkText = link.text();
            logger.info("text="+linkText + " , href="+linkHref);
            //TODO process url
        }


        if(StringUtils.isBlank(paserMap.get(md5))){

        }

    }

}

package com.xiao.pro.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by xiaoliang on 2017/10/30.
 */
public class HtmlParser {

    public static final Logger logger = LoggerFactory.getLogger(HtmlParser.class);

    public void parserHtml(InputStream  data) throws Exception {
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


    }

}

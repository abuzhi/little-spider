package com.xiao.pro.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

/**
 * Created by xiaoliang on 2017/10/30.
 */
public class HtmlParserTest {

    private Logger logger  = LoggerFactory.getLogger(HtmlParserTest.class);

    @Test
    public void testUrl() throws Exception {
        Document doc = Jsoup.connect("http://www.xuexi111.com").get();
        Element content = doc.body();
        Elements links = content.getElementsByTag("a");
        for (Element link : links) {
            String linkHref = link.attr("href");
            String linkText = link.text();
            logger.info("text="+linkText + " , href="+linkHref);
        }

    }
}
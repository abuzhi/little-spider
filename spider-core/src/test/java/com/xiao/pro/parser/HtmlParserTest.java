package com.xiao.pro.parser;

import com.xiao.pro.requester.HttpRequester;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.junit.Assert.*;

/**
 * Created by xiaoliang on 2017/10/30.
 */
public class HtmlParserTest {

    private Logger logger  = LoggerFactory.getLogger(HtmlParserTest.class);
    private HttpRequester httpRequester = null;


    @Before
    public void init() {
//        httpRequester = new HttpRequester("http://www.xuexi111.com");
        httpRequester.init(30000);
    }

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

    @Test
    public void testUrlString() throws Exception {

        StringBuffer sb = new StringBuffer();

        String url = "http://www.xuexi111.com/zazhi/131684.html";
        InputStream inputStream = httpRequester.doGet(url);
        Reader reader = new InputStreamReader(inputStream);
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
        }

    }
}
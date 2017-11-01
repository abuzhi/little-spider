package com.xiao.pro.requester;

import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

/**
 * Created by xiaoliang on 2017/10/30.
 */
public class HttpRequesterTest {

    private HttpRequester httpRequester = null;

    @Before
    public void init() {
        httpRequester = new HttpRequester("http://www.xuexi111.com");
        httpRequester.init(30000);
    }

    @Test
    public void closeConnection() throws Exception {

    }

    @Test
    public void doGet() throws Exception {
        StringBuffer sb = new StringBuffer();

        String url = "http://www.xuexi111.com";
        InputStream inputStream = httpRequester.doGet(url);
        Reader reader = new InputStreamReader(inputStream);
        BufferedReader br = new BufferedReader(reader);

        String line = br.readLine();
        while (line != null) {
            sb.append(line).append("\n");
            line = br.readLine();
        }

        assertNotNull(sb);
    }

}
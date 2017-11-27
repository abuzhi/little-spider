package com.xiao.pro.parser;

import com.xiao.pro.persistance.TestDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by xiaoliang on 2017/11/26.
 */
public class UrlConsumer implements Runnable {
    public static final Logger logger = LoggerFactory.getLogger(UrlConsumer.class);
    private BlockingQueue<Map<String, String>> consumerQueue;
    private TestDao dao = new TestDao();

    public UrlConsumer(BlockingQueue<Map<String, String>> consumerQueue) {
        this.consumerQueue = consumerQueue;
        dao.init();
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (consumerQueue.isEmpty()) {
                    Thread.sleep(500);
                } else {
                    Map<String, String> map = consumerQueue.take();
                    if(!map.isEmpty()){
                        dao.batchInsert(map);
                        logger.info("batch in ...");
                    }
                    Thread.sleep(10);
                }
            } catch (Exception e) {
                logger.error("err: ", e);
            }

        }

    }
}

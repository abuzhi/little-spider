package com.xiao.pro;

import com.xiao.pro.parser.UrlConsumer;
import com.xiao.pro.parser.UrlProducer;
import com.xiao.pro.requester.HttpRequester;
import com.xiao.pro.utils.EncryptMD5;
import com.xiao.pro.utils.HikariPoolUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by xiaoliang on 2017/10/31.
 */
public class AppStarter {

    public static final Logger logger = LoggerFactory.getLogger(AppStarter.class);

    private BlockingQueue<String> producerQueue;
    private ConcurrentMap<String,String> paserMap;
    private  ExecutorService pool ;

    public AppStarter(BlockingQueue<String> producerQueue) {
        this.producerQueue = producerQueue;
    }

    public void processor() {
        pool = Executors.newFixedThreadPool(200);
        paserMap = new ConcurrentHashMap<>();
        BlockingQueue<Map<String,String>> consumerQueue = new LinkedBlockingQueue();

        AtomicLong counter = new AtomicLong(0);

        for(int i =0;i<100;i++){
            pool.execute(new UrlProducer(producerQueue,consumerQueue,paserMap,counter));
        }

        for(int i =0;i<50;i++){
            pool.execute(new UrlConsumer(consumerQueue));
        }

        pool.shutdown();
        logger.info("shutdown()：启动一次顺序关闭，执行以前提交的任务，但不接受新任务。");
        while(true){
            if(pool.isTerminated()){
                logger.info("shut down pool....");
                break;
            }
            try {
                pool.awaitTermination(1, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                logger.info("wait 60s ... ");
            }
        }
    }

    public static void main(String[] args) {
        BlockingQueue<String> queue = new LinkedBlockingQueue();

        Properties properties = new Properties();
        properties.put("jdbcUrl","jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true");
        properties.put("username","root");
        properties.put("password","123456");
        properties.put("driverClassName","com.mysql.jdbc.Driver");
        HikariPoolUtils.initialPool(properties);

        try {
            queue.put("http://www.xuexi111.com");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AppStarter appStarter = new AppStarter(queue);
        appStarter.processor();
    }
}

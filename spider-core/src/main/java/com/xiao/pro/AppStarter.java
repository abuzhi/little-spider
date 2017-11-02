package com.xiao.pro;

import com.xiao.pro.requester.HttpRequester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by xiaoliang on 2017/10/31.
 */
public class AppStarter {

    public static final Logger logger = LoggerFactory.getLogger(AppStarter.class);

    private Queue<String> queue;
    private Map<String,String> paserMap;
    private  ExecutorService pool ;

    public void processor() {
        pool = Executors.newFixedThreadPool(20);
        paserMap = new HashMap<>();
        queue = new ConcurrentLinkedQueue();

        for(int i =0;i<10;i++){
            pool.execute(new HttpRequester(queue,paserMap));
        }

        pool.shutdown();
        logger.info("shutdown()：启动一次顺序关闭，执行以前提交的任务，但不接受新任务。");
        while(true){
            if(pool.isTerminated()){
                logger.info("shut down pool....");
                break;
            }
            try {
                pool.awaitTermination(3, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                logger.info("wait 60s ... ");
            }
        }
    }

    public static void main(String[] args) {
        AppStarter appStarter = new AppStarter();
        appStarter.queue.add("http://www.xuexi111.com");

        appStarter.processor();
    }
}

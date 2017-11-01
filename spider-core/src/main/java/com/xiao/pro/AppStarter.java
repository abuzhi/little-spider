package com.xiao.pro;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xiaoliang on 2017/10/31.
 */
public class AppStarter {

    private Queue<String> queue;
    private Map<String,String> paserMap;
    private  ExecutorService pool ;

    public void processor() {
        pool = Executors.newFixedThreadPool(20);

        queue = new ConcurrentLinkedQueue();
        queue.add("http://www.xuexi111.com");

        synchronized(queue) {
            if(!queue.isEmpty()) {
                queue.poll(obj);
            }
        }
    }
}

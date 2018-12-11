package com.hyr.lettuce.hellolettuce;


import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.concurrent.ExecutionException;

/*******************************************************************************
 * @date 2018-12-11 上午 11:33
 * @author: <a href=mailto:>黄跃然</a>
 * @Description: lettuce 单机操作
 ******************************************************************************/
public class LettuceClient {
    public static void main(String[] args) {
        RedisClient client = RedisClient.create(RedisURI.create("redis://127.0.0.1:7001"));
        StatefulRedisConnection<String, String> connect = client.connect();

        /*同步执行的命令*/
        RedisCommands<String, String> commands = connect.sync();
        commands.set("test1", "hyrtest1");
        String str = commands.get("test1");
        //Stringstr2=commands.get("test2");//MOVED8899192.168.37.128:7001;;;;client是单机版
        System.out.println(str);

        /*异步执行的命令*/
        RedisAsyncCommands<String, String> asyncCommands = connect.async();
        RedisFuture<String> future = asyncCommands.get("test1");
        try {
            String str1 = future.get();
            System.out.println(str1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        connect.close();
        client.shutdown();
    }
}
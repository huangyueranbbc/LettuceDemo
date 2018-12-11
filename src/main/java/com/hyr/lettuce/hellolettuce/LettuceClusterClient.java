package com.hyr.lettuce.hellolettuce;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/*******************************************************************************
 * @date 2018-12-11 上午 11:33
 * @author: <a href=mailto:>黄跃然</a>
 * @Description: lettuce 集群操作
 ******************************************************************************/
public class LettuceClusterClient {

    public static void main(String[] args) {
        ArrayList<RedisURI> list = new ArrayList <RedisURI>();
        list.add(RedisURI.create("redis://192.168.0.193:7001"));
        list.add(RedisURI.create("redis://192.168.0.193:7002"));
        list.add(RedisURI.create("redis://192.168.0.193:7003"));
        RedisClusterClient client = RedisClusterClient.create(list);
        //RedisClusterClientclient=RedisClusterClient.create("redis://192.168.0.193:7000");
        StatefulRedisClusterConnection<String, String> connect = client.connect();

        /*同步执行的命令*/
        RedisAdvancedClusterCommands<String, String> commands = connect.sync();
        String str = commands.get("test2");
        System.out.println(str);

        /*异步执行的命令*/
        RedisAdvancedClusterAsyncCommands<String, String> asyncCommands = connect.async();
        RedisFuture<String> future = asyncCommands.get("test2");
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

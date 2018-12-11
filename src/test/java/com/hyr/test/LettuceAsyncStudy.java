package com.hyr.test;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Threads(100)
@State(Scope.Benchmark)
@Measurement(iterations = 2, time = 600, timeUnit = TimeUnit.MILLISECONDS)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class LettuceAsyncStudy {
    private static final int LOOP = 100;
    private StatefulRedisConnection<String, String> connection;

    @Setup
    public void setup() {
        RedisClient client = RedisClient.create("redis://127.0.0.1:7001");
        connection = client.connect();
    }

    @Benchmark
    public void get() throws ExecutionException, InterruptedException {
        RedisAsyncCommands<String, String> commands = connection.async(); // 异步命令
        List<RedisFuture<String>> redisFutureList = new ArrayList<RedisFuture<String>>();
        for (int i = 0; i < LOOP; ++i) {
            RedisFuture<String> future = commands.set("a", i+"");
            redisFutureList.add(future);
            future.get();
        }
        for (RedisFuture<String> future : redisFutureList) {
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(LettuceAsyncStudy.class.getSimpleName())
                .output("benchmark/lettuceAsync-Throughput.log").forks(1).build();
        new Runner(options).run();
    }
}
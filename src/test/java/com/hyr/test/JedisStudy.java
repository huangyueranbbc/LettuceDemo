package com.hyr.test;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Threads(10)
@State(Scope.Thread)
@Measurement(iterations = 2, time = 600, timeUnit = TimeUnit.MILLISECONDS)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class JedisStudy {
    private static final int LOOP = 100;
    private Jedis jedis;
    @Setup
    public void setup() {
        jedis = new Jedis("127.0.0.1", 7001);
    }
    @Benchmark
    public void get() {
        for (int i = 0; i < LOOP; ++i) {
            jedis.set("a",i+"");
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(JedisStudy.class.getSimpleName())
                .output("benchmark/jedis-Throughput.log").forks(1).build();
        new Runner(options).run();
    }
}
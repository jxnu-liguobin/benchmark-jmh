package io.growingio;

import io.growing.sdk.java.GrowingAPI;
import io.growing.sdk.java.dto.GIOEventMessage;
import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author liguobin@growingio.com
 * @version 1.0, 2020/10/29
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
@Measurement(iterations = 1)
@Warmup(iterations = 1)
@Timeout(time = 5, timeUnit = TimeUnit.MINUTES)
@Threads(1)
public class CdpSdkBenchmark {

    static {
        System.setProperty("java.util.logging.config.file", "src/main/resources/logging.properties");
    }


    /**
     * thread=1, sleep 1ms 750  即单线程下最大750，否则可能出现队列慢，消息被拒收
     * thread=2, sleep 3ms 800
     * thread=5, sleep 5ms 850
     * thread=10, sleep 9ms 950
     * thread=20, sleep 18ms 950
     */
    @Benchmark
    public void apiSendEventTest() throws InterruptedException {

        GIOEventMessage msg = new GIOEventMessage.Builder()
                .eventKey(new Random().nextInt() + "")
                .eventNumValue(new Random().nextInt())
                .loginUserId(new Random().nextInt() + "")
                .addEventVariable("product_name", "苹果")
                .addEventVariable("product_classify", "水果")
                .addEventVariable("product_classify", "水果")
                .addEventVariable("product_price", 14)
                .build();
        GrowingAPI.send(msg);
        TimeUnit.MILLISECONDS.sleep(1);
    }

}

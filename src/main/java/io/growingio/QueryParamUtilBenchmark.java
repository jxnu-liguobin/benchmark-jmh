package io.growingio;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * 压测
 * <p>
 * {{{
 * -- 分支3，预热3，单次方法调用20万次，压测20次
 * Benchmark                                        Mode  Cnt    Score    Error  Units
 * QueryParamUtilBenchmark.regex_benchmark            ss   60  119.998 ± 31.018  ms/op
 * QueryParamUtilBenchmark.regex_benchmark_origin     ss   60  116.378 ± 24.942  ms/op
 * QueryParamUtilBenchmark.string_benchmark           ss   60    7.443 ±  0.245  ms/op
 * QueryParamUtilBenchmark.string_benchmark_origin    ss   60    7.270 ±  0.355  ms/op
 * <p>
 * -- 分支3，预热3，单次方法调用40万次，压测20次
 * Benchmark                                        Mode  Cnt    Score    Error  Units
 * QueryParamUtilBenchmark.regex_benchmark            ss   60  192.079 ± 17.174  ms/op
 * QueryParamUtilBenchmark.regex_benchmark_origin     ss   60  129.601 ± 13.881  ms/op
 * QueryParamUtilBenchmark.string_benchmark           ss   60   13.433 ±  0.301  ms/op
 * QueryParamUtilBenchmark.string_benchmark_origin    ss   60   13.325 ±  0.602  ms/op
 * <p>
 * -- 分支3，预热3，单次方法调用40万次，压测100次
 * Benchmark                                        Mode  Cnt    Score   Error  Units
 * QueryParamUtilBenchmark.regex_benchmark            ss  300  268.998 ± 9.382  ms/op
 * QueryParamUtilBenchmark.regex_benchmark_origin     ss  300  168.764 ± 6.610  ms/op
 * QueryParamUtilBenchmark.string_benchmark           ss  300   15.560 ± 0.380  ms/op
 * QueryParamUtilBenchmark.string_benchmark_origin    ss  300   14.301 ± 0.177  ms/op
 * <p>
 * }}}
 *
 * @author liguobin@growingio.com
 * @version 1.0, 2019/11/26
 */
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.SingleShotTime) //单发调用时间压测
@State(Scope.Thread)
@Threads(8)
public class QueryParamUtilBenchmark {

    String query = "adid=1630857748502539&cid=1630858744512548&convert_id=__CONVERT_ID__&csite=1&ctype=4&mac=__MAC__&idfa=D07A31DE-EAB5-4167-A425-D5619BA652D5&mac_md5=__MAC1__&uuid=__UUID__&openudid=594ae4a38846e6a932880dc45063a70460fbaee2&udid=4fae45cd8b4abf88845ad08c15a0d528&os=3&ip=__IP__&ua=__UA1__&tm=1556088457000&callback_param=http%3A%2F%2Fad.toutiao.com%2Ftrack%2Factivate%2F%3Fcallback%3DCIuwncaa6PICEKSAlaGe6PICGKby-OuYAyDF64b65AEos-CFutuB8gIwDDgJQiEyMDE5MDQyNDE0NDczNzAxMDAyMzAyODA5MTM3NzIxNDJIAVAAWAFgAQ%26os%3D3%26muid%3DD07A31DE-EAB5-4167-A425-D5619BA652D5";

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(QueryParamUtilBenchmark.class.getSimpleName()).output("./benchmark_f3_w3_m100_400000.log")
                .forks(3)//根据文档，这个值大点会更精确，但是更慢。
                .warmupIterations(3)//预热
                .measurementIterations(100)
                .build();

        new Runner(opt).run();
    }

    //调用一次方法，执行20 0000次压测
    //调用20次方法，执行一次压测
    @Benchmark
    public void string_benchmark() {
        for (int i = 0; i < 400000; i++) {
            QueryParamUtil.macroNotReplaced(query);
        }
    }

    @Benchmark
    public void string_benchmark_origin() {
        for (int i = 0; i < 400000; i++) {
            OriginQueryParamUtil.macroNotReplaced(query);
        }
    }

    @Benchmark
    public void regex_benchmark() {
        for (int i = 0; i < 400000; i++) {
            QueryParamUtil.macroNotReplaced_Regex(query);
        }
    }

    @Benchmark
    public void regex_benchmark_origin() {
        for (int i = 0; i < 400000; i++) {
            OriginQueryParamUtil.macroNotReplaced_Regex(query);
        }
    }
}

package io.growingio;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * 压测
 *
 * @author liguobin@growingio.com
 * @version 1.0, 2019/11/26
 */
@OutputTimeUnit(TimeUnit.MICROSECONDS) //微妙
@BenchmarkMode(Mode.AverageTime) //平均时间
@State(Scope.Thread)
@Threads(8)
public class QueryParamUtilBenchmark {

    String query = "adid=1630857748502539&cid=1630858744512548&convert_id=__CONVERT_ID__&csite=1&ctype=4&mac=__MAC__&idfa=D07A31DE-EAB5-4167-A425-D5619BA652D5&mac_md5=__MAC1__&uuid=__UUID__&openudid=594ae4a38846e6a932880dc45063a70460fbaee2&udid=4fae45cd8b4abf88845ad08c15a0d528&os=3&ip=__IP__&ua=__UA1__&tm=1556088457000&callback_param=http%3A%2F%2Fad.toutiao.com%2Ftrack%2Factivate%2F%3Fcallback%3DCIuwncaa6PICEKSAlaGe6PICGKby-OuYAyDF64b65AEos-CFutuB8gIwDDgJQiEyMDE5MDQyNDE0NDczNzAxMDAyMzAyODA5MTM3NzIxNDJIAVAAWAFgAQ%26os%3D3%26muid%3DD07A31DE-EAB5-4167-A425-D5619BA652D5";

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(QueryParamUtilBenchmark.class.getSimpleName()).output("./Benchmark_avg.log")
                .forks(1)
                .warmupIterations(20)//预热
                .measurementIterations(20)
                .build();

        new Runner(opt).run();
    }

    @Benchmark
    public void string_benchmark() {
        QueryParamUtil.macroNotReplaced(query);
    }

    @Benchmark
    public void string_benchmark_origin() {
        OriginQueryParamUtil.macroNotReplaced(query);
    }

    @Benchmark
    public void regex_benchmark() {
        QueryParamUtil.macroNotReplaced_Regex(query);
    }

    @Benchmark
    public void regex_benchmark_origin() {
        OriginQueryParamUtil.macroNotReplaced_Regex(query);
    }
}

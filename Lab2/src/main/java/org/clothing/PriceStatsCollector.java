package org.example.clothing;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collector;

/**
 * Collector that computes min, max, average and standard deviation for price.
 */
public class PriceStatsCollector implements Collector<Cloth, PriceStatsCollector.Acc, StatsData> {

    static class Acc {
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        double sum = 0.0;
        double sumSq = 0.0;
        long count = 0;
        void add(double v) {
            if (v < min) min = v;
            if (v > max) max = v;
            sum += v;
            sumSq += v * v;
            count++;
        }
        void merge(Acc o) {
            if (o.min < min) min = o.min;
            if (o.max > max) max = o.max;
            sum += o.sum;
            sumSq += o.sumSq;
            count += o.count;
        }
    }

    @Override
    public Supplier<Acc> supplier() { return Acc::new; }

    @Override
    public java.util.function.BiConsumer<Acc, Cloth> accumulator() {
        return (acc, c) -> acc.add(c.getPrice());
    }

    @Override
    public java.util.function.BiConsumer<Acc, Acc> combiner() {
        return (a, b) -> a.merge(b);
    }

    @Override
    public java.util.function.Function<Acc, StatsData> finisher() {
        return acc -> {
            double avg = acc.count == 0 ? 0.0 : acc.sum / acc.count;
            double variance = acc.count == 0 ? 0.0 : (acc.sumSq / acc.count) - (avg * avg);
            double std = variance <= 0 ? 0.0 : Math.sqrt(variance);
            double min = acc.count == 0 ? 0.0 : acc.min;
            double max = acc.count == 0 ? 0.0 : acc.max;
            return new StatsData(min, max, avg, std);
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }
}

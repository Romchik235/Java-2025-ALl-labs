package org.example.clothing;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Application entry point.
 */
public class App {
    public static void main(String[] args) {
        Supplier<Cloth> generator = ClothingGenerator.generator();
        int skipN = 3;
        String cityToSkip = "Kharkiv";
        List<Cloth> list = Gatherer.gather(Stream.generate(generator), cityToSkip, skipN, 500);
        int minMonths = 1;
        int maxMonths = 24;
        Map<String, List<Cloth>> grouped = list.stream()
                .filter(c -> {
                    int months = c.getMonthsSinceProduction();
                    return months >= minMonths && months <= maxMonths;
                })
                .collect(Collectors.groupingBy(Cloth::getFabricType));
        StatsData stats = list.stream().collect(new PriceStatsCollector());
        System.out.println("Price stats:");
        System.out.println("min=" + stats.getMin());
        System.out.println("max=" + stats.getMax());
        System.out.println("avg=" + stats.getAverage());
        System.out.println("std=" + stats.getStdDev());
        List<Double> prices = list.stream().map(Cloth::getPrice).sorted().collect(Collectors.toList());
        QuartileResult qr = Quartiles.compute(prices);
        double iqr = qr.getQ3() - qr.getQ1();
        double lowerFence = qr.getQ1() - 1.5 * iqr;
        double upperFence = qr.getQ3() + 1.5 * iqr;
        Map<String, Long> outlierCounts = list.stream().collect(Collectors.groupingBy(c -> {
            double p = c.getPrice();
            return (p < lowerFence || p > upperFence) ? "outliers" : "data";
        }, Collectors.counting()));
        Map<String, Long> result = new LinkedHashMap<>();
        result.put("data", outlierCounts.getOrDefault("data", 0L));
        result.put("outliers", outlierCounts.getOrDefault("outliers", 0L));
        System.out.println(result);
    }
}

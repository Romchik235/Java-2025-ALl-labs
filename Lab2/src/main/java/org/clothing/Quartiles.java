package org.example.clothing;

import java.util.List;

/**
 * Utilities to compute quartiles.
*/

public final class Quartiles {
    public static QuartileResult compute(List<Double> sorted) {
        int n = sorted.size();
        double q1 = percentile(sorted, 25);
        double q3 = percentile(sorted, 75);
        return new QuartileResult(q1, q3);
    }

    private static double percentile(List<Double> sorted, double p) {
        if (sorted.isEmpty()) return 0.0;
        double pos = p * (sorted.size() + 1) / 100.0;
        if (pos < 1) return sorted.get(0);
        if (pos >= sorted.size()) return sorted.get(sorted.size() - 1);
        int lower = (int) pos;
        double fraction = pos - lower;
        double lowerVal = sorted.get(lower - 1);
        double upperVal = sorted.get(lower);
        return lowerVal + fraction * (upperVal - lowerVal);
    }
}

package org.example.clothing;

/**
 * Container for statistical results.
*/

public class StatsData {
    private final double min;
    private final double max;
    private final double average;
    private final double stdDev;
    public StatsData(double min, double max, double average, double stdDev) {
        this.min = min;
        this.max = max;
        this.average = average;
        this.stdDev = stdDev;
    }
    public double getMin() { return min; }
    public double getMax() { return max; }
    public double getAverage() { return average; }
    public double getStdDev() { return stdDev; }
}

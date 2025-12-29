package org.example.clothing;

/**
 * Result of quartile computation.
*/

public class QuartileResult {
    private final double q1;
    private final double q3;
    public QuartileResult(double q1, double q3) {
        this.q1 = q1;
        this.q3 = q3;
    }
    public double getQ1() { return q1; }
    public double getQ3() { return q3; }
}

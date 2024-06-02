package com.chwieder;

public class Output {
    public double createMs;
    public double readMs;
    public double deleteMs;

    public Output(double createMs, double readMs, double deleteMs) {
        this.createMs = createMs;
        this.readMs = readMs;
        this.deleteMs = deleteMs;
    }
}

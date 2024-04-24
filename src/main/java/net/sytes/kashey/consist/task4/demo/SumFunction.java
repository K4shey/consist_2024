package net.sytes.kashey.consist.task4.demo;

import net.sytes.kashey.consist.task4.lib.function.CustomFunction;

public class SumFunction implements CustomFunction {
    @Override
    public double apply(double... arg) {
        return arg[0] + arg[1];
    }
}

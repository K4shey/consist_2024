package net.sytes.kashey.consist.task4.lib.calc;

public class CalculatorContext {

    private CalculatorStrategy strategy;

    public void setStrategy(CalculatorStrategy strategy) {
        this.strategy = strategy;
    }

    public double calculate(String expression) {
        return strategy.calculate(expression);
    }
}

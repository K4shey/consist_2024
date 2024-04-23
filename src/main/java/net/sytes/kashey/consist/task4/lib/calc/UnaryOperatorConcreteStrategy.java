package net.sytes.kashey.consist.task4.lib.calc;

import net.sytes.kashey.consist.task4.lib.unary.UnaryOperator;

public class UnaryOperatorConcreteStrategy implements CalculatorStrategy {

    private final UnaryOperator operator;

    public UnaryOperatorConcreteStrategy(UnaryOperator operator) {
        this.operator = operator;
    }

    @Override
    public double calculate(String expression) {
        return 0;
    }
}

package net.sytes.kashey.consist.task4.lib.calc;

import net.sytes.kashey.consist.task4.lib.unary.UnaryOperator;

/**
 * Конкретная реализация стратегии (требование 3). Вычисляет результат выражения, содержащего унарную операцию
 */
public class UnaryOperatorConcreteStrategy implements CalculatorStrategy {

    /**
     *  Вид унарного оператора, который используется в выражении
     */
    private final UnaryOperator operator;


    public UnaryOperatorConcreteStrategy(UnaryOperator operator) {
        this.operator = operator;
    }

    /**
     *
     * @param expression Строка, содержащая выражение для вычисления
     * @return Результат вычисления выражения
     */
    @Override
    public double calculate(String expression) {
        return 0;
    }
}

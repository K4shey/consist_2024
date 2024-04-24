package net.sytes.kashey.consist.task4.lib.calc;

/**
 *  Стратегия определяет интерфейс, общий для всех вариаций алгоритма
 */
public interface CalculatorStrategy {

    /**
     *
     * @param expression Строка, содержащая выражение для вычисления
     * @return Результат вычисления выражения
     */
    double calculate(String expression);
}

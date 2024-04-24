package net.sytes.kashey.consist.task4.lib.calc;

/**
 * Хранит ссылку на объект конкретной стратегии, работая с ним через общий интерфейс стратегий.
 * Имеется возможность смены стратегии "на лету"
 */
public class CalculatorContext {

    private CalculatorStrategy strategy;

    /**
     * Обеспечивает возможность установки произвольной стратегии
     *
     * @param strategy Класс, имплементирующий интерфейс CalculatorStrategy. Конкретная реализация какой-либо стратегии.
     */
    public void setStrategy(CalculatorStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Производит вычисление выражения, используя конкретную стратегию вычисления
     *
     * @param expression Строка, содержащая выражение для вычисления
     * @return Результат вычисления выражения
     */
    public double calculate(String expression) {
        return strategy.calculate(expression);
    }
}

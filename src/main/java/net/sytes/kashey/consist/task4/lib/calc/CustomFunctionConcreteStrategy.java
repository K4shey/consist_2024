package net.sytes.kashey.consist.task4.lib.calc;

import net.sytes.kashey.consist.task4.lib.function.CustomFunction;

import java.util.Map;

/**
 * Конкретная реализация стратегии (требование 5). Вычисляет результат выражения, содержащего пользовательские функции
 */
public class CustomFunctionConcreteStrategy implements CalculatorStrategy {

    /**
     * Содержит соответствия: Имя функции - Пользовательская функция
     */
    private Map<String, CustomFunction> functions;

    /**
     * Инициализирует описание пользовательских функций при создании объекта
     *
     * @param functions соответствия: Имя функции - Пользовательская функция
     */

    public CustomFunctionConcreteStrategy(Map<String, CustomFunction> functions) {
        this.functions = functions;
    }

    /**
     * Добавляет новое соответствие
     *
     * @param name     Имя пользовательской функции, указывается в строке выражения
     * @param function Пользовательская функция, вызываемая по имени
     */
    public void setFunction(String name, CustomFunction function) {
        functions.put(name, function);
    }

    /**
     * @param expression Строка, содержащая выражение для вычисления
     * @return Результат вычисления выражения
     */
    @Override
    public double calculate(String expression) {
        return 0;
    }
}

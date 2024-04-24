package net.sytes.kashey.consist.task4.lib.function;

/**
 * Интерфейс пользовательской функции. Для добавления новой пользовательской функции, нужно имплементировать его.
 */
public interface CustomFunction {
    /**
     * Метод для реализации пользовательской функции
     *
     * @param arg Список аргументов, передаваемых в функцию для вычисления
     * @return Результат работы функции
     */
    double apply(double... arg);
}

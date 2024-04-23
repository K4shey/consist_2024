package net.sytes.kashey.consist.task4.demo;

import net.sytes.kashey.consist.task4.lib.calc.BasicConcreteStrategy;
import net.sytes.kashey.consist.task4.lib.calc.CalculatorContext;
import net.sytes.kashey.consist.task4.lib.calc.CustomFunctionConcreteStrategy;
import net.sytes.kashey.consist.task4.lib.calc.UnaryOperatorConcreteStrategy;
import net.sytes.kashey.consist.task4.lib.unary.UnaryOperator;

import java.util.Map;

public class DemoCalculator {
    public static void main(String[] args) {

        CalculatorContext calculatorContext = new CalculatorContext();
        calculatorContext.setStrategy(new CustomFunctionConcreteStrategy(Map.of("mySquareFunc", new SquareFunction())));
        double result1 = calculatorContext.calculate("2+mySquareFunc(10)");
        System.out.println(result1);
        calculatorContext.setStrategy(new BasicConcreteStrategy());
        double result2 = calculatorContext.calculate("2+3*(4-545)/60");
        System.out.println(result2);
        calculatorContext.setStrategy(new UnaryOperatorConcreteStrategy(UnaryOperator.NEGATE));
        double result3 = calculatorContext.calculate("5");
        System.out.println(result3);
    }
}

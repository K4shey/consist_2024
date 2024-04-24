package net.sytes.kashey.consist.task4.demo;

import net.sytes.kashey.consist.task4.lib.calc.BasicConcreteStrategy;
import net.sytes.kashey.consist.task4.lib.calc.CalculatorContext;
import net.sytes.kashey.consist.task4.lib.calc.CustomFunctionConcreteStrategy;
import net.sytes.kashey.consist.task4.lib.calc.UnaryOperatorConcreteStrategy;
import net.sytes.kashey.consist.task4.lib.function.CustomFunction;
import net.sytes.kashey.consist.task4.lib.unary.UnaryOperator;

import java.util.HashMap;
import java.util.Map;

public class DemoCalculator {
    public static void main(String[] args) {

        CalculatorContext calculatorContext = new CalculatorContext();

        calculatorContext.setStrategy(new BasicConcreteStrategy());
        printResult(calculatorContext, "2+3*(4-545)/60");

        calculatorContext.setStrategy(new UnaryOperatorConcreteStrategy(UnaryOperator.NEGATE));
        printResult(calculatorContext, "5");

        Map<String, CustomFunction> functions = new HashMap<>();
        functions.put("mySquareFunc", new SquareFunction());
        CustomFunctionConcreteStrategy customFunctionStrategy = new CustomFunctionConcreteStrategy(functions);
        customFunctionStrategy.setFunction("mySumFunction", new SumFunction());
        calculatorContext.setStrategy(customFunctionStrategy);
        printResult(calculatorContext, "2+mySquareFunc(10)*mySumFunction(1,2)");
    }

    private static void printResult(CalculatorContext context, String expression) {
        double result = context.calculate(expression);
        System.out.println(result);
    }
}

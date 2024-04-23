package net.sytes.kashey.consist.task4.lib.calc;

import net.sytes.kashey.consist.task4.lib.function.CustomFunction;

import java.util.HashMap;
import java.util.Map;

public class CustomFunctionConcreteStrategy implements CalculatorStrategy {

    private  Map<String, CustomFunction> functions = new HashMap<>();

    public CustomFunctionConcreteStrategy(Map<String, CustomFunction> functions)  {
        this.functions = functions;
    }

    public void setFunction(String name, CustomFunction function) {
        functions.put(name, function);
    }

    @Override
    public double calculate(String expression) {
        return 0;
    }
}

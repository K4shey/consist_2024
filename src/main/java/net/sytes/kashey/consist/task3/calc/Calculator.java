package net.sytes.kashey.consist.task3.calc;

import net.sytes.kashey.consist.task3.model.ExpressionModelStatus;
import net.sytes.kashey.consist.task3.model.ExpressionModel;
import org.springframework.stereotype.Component;

@Component
public class Calculator {
    public static ExpressionModel calculate(ExpressionModel expressionModel) {

        String[] parts = expressionModel.getExpression().split("(?<=[*+\\-/])|(?=[*+\\-/])");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid expression format");
        }
        double operand1 = Double.parseDouble(parts[0]);
        double operand2 = Double.parseDouble(parts[2]);
        double calculationResult = getCalculationResult(parts, operand1, operand2);

        expressionModel.setStatus(ExpressionModelStatus.CALCULATED);
        expressionModel.setResult(calculationResult);
        return expressionModel;
    }

    private static double getCalculationResult(String[] parts, double operand1, double operand2) {
        double calculationResult;
        String operator = parts[1];

        switch (operator) {
            case "+" -> {
                calculationResult = operand1 + operand2;
            }
            case "-" -> {
                calculationResult = operand1 - operand2;
            }
            case "*" -> {
                calculationResult = operand1 * operand2;
            }
            case "/" -> {
                if (operand2 == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                calculationResult = operand1 / operand2;
            }
            default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
        return calculationResult;
    }
}

package net.sytes.kashey.consist.task3.calc;

import net.sytes.kashey.consist.task3.model.Expression;
import net.sytes.kashey.consist.task3.model.ExpressionStatus;
import org.springframework.stereotype.Component;

@Component
public class Calculator {

    public static Expression calculate(Expression expression) {

        try {
            Thread.sleep(1000 * 3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String[] parts = expression.getExpression().split("(?<=[*+\\-/])|(?=[*+\\-/])");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid expression format");
        }
        double operand1 = Double.parseDouble(parts[0]);
        double operand2 = Double.parseDouble(parts[2]);
        double calculationResult = getCalculationResult(parts, operand1, operand2);

        return new Expression(expression.getId(), expression.getExpression(), expression.isNeedLog(),
                ExpressionStatus.COMPLETED, calculationResult, expression.getDescription());
    }


    private static double getCalculationResult(String[] parts, double operand1, double operand2) {
        double calculationResult;
        String operator = parts[1];

        switch (operator) {
            case "+" -> calculationResult = operand1 + operand2;
            case "-" -> calculationResult = operand1 - operand2;
            case "*" -> calculationResult = operand1 * operand2;
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

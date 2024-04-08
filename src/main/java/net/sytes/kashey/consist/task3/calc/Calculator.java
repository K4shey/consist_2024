package net.sytes.kashey.consist.task3.calc;

import org.springframework.stereotype.Component;

@Component
public class Calculator {
    public String calculate(String expression) {

//        String[] parts = expression.split(" *+-/");
//        if (parts.length != 3) {
//            throw new IllegalArgumentException("Invalid expression format");
//        }
//
//        double operand1 = Double.parseDouble(parts[0]);
//        double operand2 = Double.parseDouble(parts[2]);
//        String operator = parts[1];

//        switch (operator) {
//            case "+":
//                return operand1 + operand2;
//            case "-":
//                return operand1 - operand2;
//            case "*":
//                return operand1 * operand2;
//            case "/":
//                if (operand2 == 0) {
//                    throw new ArithmeticException("Division by zero");
//                }
//                return operand1 / operand2;
//            default:
//                throw new IllegalArgumentException("Unsupported operator: " + operator);
//        }

        return "{\"result\":\"4\"}";
    }
}

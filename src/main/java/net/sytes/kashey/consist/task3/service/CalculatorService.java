package net.sytes.kashey.consist.task3.service;

import net.sytes.kashey.consist.task3.calc.Calculator;
import net.sytes.kashey.consist.task3.exception.ExpressionNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.regex.*;

@Component
public class CalculatorService {

    private static final String REGEXP = "\\d+[+*\\-\\/]\\d+";

    private final Calculator calculator;

    @Autowired
    public CalculatorService(Calculator calculator) {
        this.calculator = calculator;
    }

    public Mono<String> calculate(String expression) {

        Pattern pattern = Pattern.compile(REGEXP);
        Matcher matcher = pattern.matcher(expression);

        if (expression.isEmpty() || expression.isBlank() || !matcher.matches()) {
            throw new ExpressionNotValidException("Expression is not valid");
        } else {
            return Mono.just(calculator.calculate(expression));
        }
    }
}

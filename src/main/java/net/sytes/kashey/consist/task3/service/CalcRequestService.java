package net.sytes.kashey.consist.task3.service;

import net.sytes.kashey.consist.task3.calc.Calculator;
import net.sytes.kashey.consist.task3.client.GitlabClient;
import net.sytes.kashey.consist.task3.dto.ExpressionDto;
import net.sytes.kashey.consist.task3.mapper.ExpressionMapper;
import net.sytes.kashey.consist.task3.model.Expression;
import net.sytes.kashey.consist.task3.model.ExpressionStatus;
import net.sytes.kashey.consist.task3.model.Note;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CalcRequestService {

    private Map<Integer, CompletableFuture<Expression>> expressionPool = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(0);

    private final GitlabClient client;

    private static final String REGEXP = "\\d+[+*\\-\\/]\\d+";

    public CalcRequestService(GitlabClient client) {
        this.client = client;
    }

    public Map<Integer, CompletableFuture<Expression>> getExpressionPool() {
        return expressionPool;
    }

    public void setExpressionPool(Map<Integer, CompletableFuture<Expression>> expressionPool) {
        this.expressionPool = expressionPool;
    }

    public String addExpression(String expression, boolean needLog) {

        Pattern pattern = Pattern.compile(REGEXP);
        Matcher matcher = pattern.matcher(expression);

        if (!matcher.matches() || expression.isEmpty() || expression.isBlank()) {
            return null;
        }

        int id = idGenerator.incrementAndGet();
        Expression expressionModel = new Expression(id, expression, needLog);

        CompletableFuture<Expression> future = CompletableFuture.supplyAsync(() -> Calculator.calculate(expressionModel));
        expressionPool.put(id, future);
        if (needLog) {
            client.addNote(new Note("Expression " + expression + ", id=" + id + " was added to pool"));
        }
        return String.valueOf(id);
    }

    public Expression getResultById(int id) {

        if (!expressionPool.containsKey(id)) {
            return null;
        }

        Expression model;
        CompletableFuture<Expression> future = expressionPool.get(id);
        if (future.isDone()) {
            try {
                model = future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
            if (model.isNeedLog()) {
                client.addNote(new Note("Expression " + model.getExpression() + ", id=" + id
                                        + " was successfully calculated. Result = " + model.getResult()));
                return model;
            }
        } else {
            model = new Expression("dummy");
        }
        return model;
    }

    public List<ExpressionDto> getAllExpressions() {

        List<ExpressionDto> list = new ArrayList<>();
        expressionPool.forEach((nextKey, future) -> {
            int key = nextKey;
            if (future.isDone()) {
                try {
                    list.add(ExpressionMapper.INSTANCE.ToDto(future.get()));
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            } else {
                list.add(new ExpressionDto(key, ExpressionStatus.IN_PROGRESS));
            }
        });
        return list;
    }

    public boolean deleteById(int id, boolean needLog) {

        boolean wasRemoved = Optional.ofNullable(expressionPool.remove(id)).isPresent();
        if (wasRemoved && needLog) {
            client.addNote(new Note("Expression with id=" + id + " was successfully removed"));
        }
        return wasRemoved;
    }

    public boolean updateById(int id, String expression, boolean needLog) {

        CompletableFuture<Expression> future = expressionPool.get(id);

        if (future == null) {
            throw new IllegalArgumentException("No expression found with id " + id);
        }

        if (future.isDone()) {
            Expression expressionToUpdate = new Expression(expression, needLog);
            expressionToUpdate.setId(id);
            CompletableFuture<Expression> newFuture = CompletableFuture.supplyAsync(() -> Calculator.calculate(expressionToUpdate));
            expressionPool.put(id, newFuture);
            if (needLog) {
                client.addNote(new Note("Expression " + expressionToUpdate.getExpression() + ", id=" + id
                                        + " was updated and added back to the pool"));
            }
            return true;
        }
        return false;
    }
}
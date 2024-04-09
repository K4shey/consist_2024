package net.sytes.kashey.consist.task3.service;

import net.sytes.kashey.consist.task3.calc.Calculator;
import net.sytes.kashey.consist.task3.client.GitlabClient;
import net.sytes.kashey.consist.task3.dto.ExpressionDto;
import net.sytes.kashey.consist.task3.model.ExpressionModel;
import net.sytes.kashey.consist.task3.model.ExpressionModelStatus;
import net.sytes.kashey.consist.task3.model.Note;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CalcRequestService {

    private final Map<Integer, CompletableFuture<ExpressionModel>> expressionPool = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(0);

    private final GitlabClient client;

    public CalcRequestService(GitlabClient client) {
        this.client = client;
    }

    public boolean addExpression(String expression, boolean needLog) {

        int id = idGenerator.incrementAndGet();
        ExpressionModel expressionModel = new ExpressionModel(expression, needLog);

        CompletableFuture<ExpressionModel> future = CompletableFuture.supplyAsync(() -> Calculator.calculate(expressionModel));
        expressionPool.put(id, future);
        if (needLog) {
            client.addNote(new Note("Expression " + expression + ", id=" + id + " was added to pool"));
        }
        return true;
    }

    public ExpressionModel getResultById(int id) {

        if (!expressionPool.containsKey(id)) {
            return null;
        }

        ExpressionModel model;
        CompletableFuture<ExpressionModel> future = expressionPool.get(id);
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
            model = new ExpressionModel("dummy");
        }
        return model;
    }

    public List<ExpressionDto> getAllExpressions() {

        List<ExpressionDto> list = new ArrayList<>();
        expressionPool.forEach((nextKey, future) -> {
            int key = nextKey;
            if (future.isDone()) {
                try {
                    list.add(new ExpressionDto(key, future.get().getStatus()));
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            } else {
                list.add(new ExpressionDto(key, ExpressionModelStatus.IN_PROGRESS));
            }
        });
        return list;
    }
}
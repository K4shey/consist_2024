package net.sytes.kashey.consist.task3.service;

import net.sytes.kashey.consist.task3.calc.Calculator;
import net.sytes.kashey.consist.task3.client.GitlabClient;
import net.sytes.kashey.consist.task3.model.ExpressionModel;
import net.sytes.kashey.consist.task3.model.Note;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CalcRequestService {

    private final Map<Integer, ExpressionModel> expressionPool = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(0);

    private final GitlabClient client;

    public CalcRequestService(GitlabClient client) {
        this.client = client;
    }

    public boolean addCalculateRequest(String expression, boolean needLog) {
        int id = idGenerator.incrementAndGet();
        ExpressionModel expressionModel = new ExpressionModel(expression, needLog);
        expressionPool.put(id, expressionModel);
        if (needLog) {
            client.addNote(new Note("Expression " + expression + ", id=" + id + " was added to pool"));
        }
        return true;
    }

    public ExpressionModel getResultById(int id) {
        if (!expressionPool.containsKey(id)) {
            return null;
        }
        ExpressionModel calculatedExpression = Calculator.calculate(expressionPool.get(id));
        if (calculatedExpression.isNeedLog()) {
            client.addNote(new Note("Expression " + calculatedExpression.getExpression() + ", id=" +
                                    id + " was successfully calculated. Result = "
                                    + calculatedExpression.getResult()));
        }
        return calculatedExpression;
    }
}

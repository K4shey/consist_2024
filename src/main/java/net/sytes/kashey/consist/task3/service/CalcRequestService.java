package net.sytes.kashey.consist.task3.service;

import net.sytes.kashey.consist.task3.calc.Calculator;
import net.sytes.kashey.consist.task3.client.GitlabClient;
import net.sytes.kashey.consist.task3.dto.ExpressionDto;
import net.sytes.kashey.consist.task3.mapper.ExpressionMapper;
import net.sytes.kashey.consist.task3.model.Expression;
import net.sytes.kashey.consist.task3.model.Note;
import net.sytes.kashey.consist.task3.repository.ExpressionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CalcRequestService {

    private final ExpressionRepository repository;

    private final GitlabClient client;

    private final ExpressionMapper mapper;

    @Autowired
    public CalcRequestService(GitlabClient client, ExpressionRepository repository, ExpressionMapper mapper) {
        this.client = client;
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public String addExpression(String expression, boolean needLog, String description) {

        Expression expressionModel = new Expression(expression, needLog, description);
        Expression savedExpr = repository.save(expressionModel);

        int id = savedExpr.getId();

        CompletableFuture<Expression> future = CompletableFuture.supplyAsync(() -> Calculator.calculate(expressionModel));
        if (needLog) {
            client.addNote(new Note("Expression " + expression + ", id=" + id + " was added to pool"));
        }

        future.thenAcceptAsync(expr -> {
            Expression savedExpression = repository.save(expr);
            if (needLog) {
                client.addNote(new Note("Expression " + savedExpression.getExpression() + ", id="
                                        + savedExpression.getId() + " was calculated and saved to repository with result = "
                                        + savedExpression.getResult()));
            }
        });
        return String.valueOf(id);
    }

    public ExpressionDto getResultById(int id) {

        Optional<Expression> optionalExpression = repository.findById(id);
        if (optionalExpression.isPresent()) {
            Expression expression = optionalExpression.get();
            if (expression.isNeedLog()) {
                client.addNote(new Note("Expression " + expression.getExpression() + ", id=" + id
                                        + " was successfully calculated. Result = " + expression.getResult()));
            }
            return mapper.toDto(expression);
        }
        return null;
    }

    public List<ExpressionDto> getAllExpressions() {

        List<Expression> expressions = repository.findAll();
        return expressions.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public boolean deleteById(int id, boolean needLog) {

        Optional<Expression> optionalExpression = repository.findById(id);
        if (optionalExpression.isPresent()) {
            if (needLog) {
                client.addNote(new Note("Expression with id=" + id + " was successfully removed"));
            }
            return true;
        }
        return false;
    }

    public boolean updateById(int id, String newExpression, boolean needLog) {

        Optional<Expression> optionalExpression = repository.findById(id);
        if (optionalExpression.isPresent()) {
            Expression existingExpression = optionalExpression.get();
            Expression updatedExpr = new Expression(existingExpression.getId(), newExpression,
                    existingExpression.isNeedLog(), existingExpression.getStatus(),
                    existingExpression.getResult(), existingExpression.getDescription());
            CompletableFuture<Expression> future = CompletableFuture.supplyAsync(() -> Calculator.calculate(updatedExpr));
            future.thenAcceptAsync(updExpr -> {
                repository.save(updExpr);
                if (needLog) {
                    client.addNote(new Note("Expression " + updExpr.getExpression() + ", id=" + id
                                            + " was updated and added back to the pool"));
                }
            });
            return true;
        }
        return false;
    }

    @Transactional
    public boolean updateDescription(int id, String newDescription) {
        Optional<Expression> optionalExpression = repository.findById(id);
        if (optionalExpression.isPresent()) {
            Expression existingExpression = optionalExpression.get();
            Expression updatedExpression = new Expression(existingExpression.getId(), existingExpression.getExpression(),
                    existingExpression.isNeedLog(), existingExpression.getStatus(), existingExpression.getResult(), newDescription);
            repository.save(updatedExpression);
            return true;
        }
        return false;
    }
}
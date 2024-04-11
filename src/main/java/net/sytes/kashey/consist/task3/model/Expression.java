package net.sytes.kashey.consist.task3.model;

public class Expression {

    private int id;
    private final String expression;
    private ExpressionStatus status;
    private final boolean needLog;
    private double result;

    public Expression(String expression) {
        this.expression = expression;
        this.needLog = false;
        this.status = ExpressionStatus.IN_PROGRESS;
        this.result = 0.0;
    }

    public Expression(String expression, boolean needLog) {
        this.expression = expression;
        this.needLog = needLog;
        this.status = ExpressionStatus.IN_PROGRESS;
        this.result = 0.0;
    }

    public Expression(int id, String expression, boolean needLog) {
        this.expression = expression;
        this.needLog = needLog;
        this.status = ExpressionStatus.IN_PROGRESS;
        this.result = 0.0;
        this.id = id;
    }

    public String getExpression() {
        return expression;
    }

    public ExpressionStatus getStatus() {
        return status;
    }

    public boolean isNeedLog() {
        return needLog;
    }

    public double getResult() {
        return result;
    }

    public void setStatus(ExpressionStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setResult(double result) {
        this.result = result;
    }
}

package net.sytes.kashey.consist.task3.model;

public class ExpressionModel {

    private String expression;
    private ExpressionModelStatus status;
    private boolean needLog;
    private double result;

    public ExpressionModel(String expression) {
        this.expression = expression;
        this.needLog = false;
        this.status = ExpressionModelStatus.CREATED;
        this.result = 0.0;
    }

    public ExpressionModel(String expression, boolean needLog) {
        this.expression = expression;
        this.needLog = needLog;
        this.status = ExpressionModelStatus.CREATED;
        this.result = 0.0;
    }

    public String getExpression() {
        return expression;
    }

    public ExpressionModelStatus getStatus() {
        return status;
    }

    public boolean isNeedLog() {
        return needLog;
    }

    public double getResult() {
        return result;
    }

    public void setStatus(ExpressionModelStatus status) {
        this.status = status;
    }

    public void setResult(double result) {
        this.result = result;
    }
}

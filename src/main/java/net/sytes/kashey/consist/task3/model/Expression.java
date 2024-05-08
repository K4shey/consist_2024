package net.sytes.kashey.consist.task3.model;


import jakarta.persistence.*;

@Entity
@Table(name = "expressions")
public class Expression {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String expression;
    @Enumerated(EnumType.STRING)
    private ExpressionStatus status;

    @Column(name = "log")
    private boolean needLog;
    private double result;

    private String description;

    public Expression() {
        this.status = ExpressionStatus.IN_PROGRESS;
        this.result = 0.0;
    }

    public Expression(String expression) {
        this(expression, false);
    }

    public Expression(String expression, boolean needLog) {
        this();
        this.expression = expression;
        this.needLog = needLog;
    }

    public Expression(int id, String expression, boolean needLog) {
        this(expression, needLog);
        this.id = id;
    }

    public Expression(int id, String expression, boolean needLog, ExpressionStatus status, Double result) {
        this(expression, needLog);
        this.id = id;
        this.status = status;
        this.result = result;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

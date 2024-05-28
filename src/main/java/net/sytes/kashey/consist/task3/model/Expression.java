package net.sytes.kashey.consist.task3.model;


import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

@Entity
@Table(name = "expressions")
public class Expression {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String expression;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private ExpressionStatus status;

    @Column(name = "log")
    private boolean needLog;
    private double result;

    private String description;

    private boolean deleted;

    public Expression() {
        this.status = ExpressionStatus.IN_PROGRESS;
        this.result = 0.0;
    }

    public Expression(String expression, boolean needLog) {
        this();
        this.expression = expression;
        this.needLog = needLog;
    }

    public Expression(String expression, boolean needLog, String description) {
        this();
        this.expression = expression;
        this.needLog = needLog;
        this.description = description;
    }


    public Expression(int id, String expression, boolean needLog, ExpressionStatus status, double result,
                      String description) {
        this(expression, needLog);
        this.id = id;
        this.status = status;
        this.result = result;
        this.description = description;
    }

    public Expression(String expression, boolean needLog, ExpressionStatus status, double result) {
        this.expression = expression;
        this.status = status;
        this.needLog = needLog;
        this.result = result;
        this.description = "";
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

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void deletionMark(boolean deleted) {
        this.deleted = deleted;
    }
}

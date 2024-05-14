package net.sytes.kashey.consist.task3.repository;

import net.sytes.kashey.consist.task3.model.Expression;
import net.sytes.kashey.consist.task3.model.ExpressionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml"})
class ExpressionRepositoryTest {

    private final ExpressionRepository repository;

    @Autowired
    public ExpressionRepositoryTest(ExpressionRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Save new expression into DB")
    void saveExpression_ExpressionIsCreated() {

        Expression expressionToSave = new Expression("2+2", false, ExpressionStatus.COMPLETED, 4.0);

        Expression savedExpression = repository.save(expressionToSave);

        assertThat(savedExpression).isNotNull();
        assertThat(savedExpression.getResult()).isEqualTo(4.0);
    }

    @Test
    @DisplayName("Delete existing expression from DB")
    void deleteExpression_ExpressionIsDeleted() {

        Expression savedExpression = repository.save(
                new Expression("2+2", false, ExpressionStatus.COMPLETED, 4.0));

        repository.deleteById(savedExpression.getId());
        Expression deletedExpression = repository.findById(savedExpression.getId()).orElse(null);

        assertThat(deletedExpression).isNull();
    }

    @Test
    @DisplayName("Update expression description in DB")
    void updateDescription_ExpressionDescriptionIsUpdated() {

        Expression savedExpression = repository.save(
                new Expression("2+2", false, ExpressionStatus.COMPLETED, 4.0));

        Expression expressionToUpdate = repository.findById(savedExpression.getId()).orElse(null);
        assert expressionToUpdate != null;
        expressionToUpdate.setDescription("Addition of 2 and 2");
        Expression updatedExpression = repository.save(expressionToUpdate);

        assertThat(updatedExpression).isNotNull();
        assertThat(updatedExpression.getDescription()).isEqualTo("Addition of 2 and 2");
    }

    @Test
    @DisplayName("Get expression by ID")
    void getExpressionById_ExpressionIsFound() {
        Expression expressionToSave = new Expression("2+2", false, ExpressionStatus.COMPLETED, 4.0);
        Expression savedExpression = repository.save(expressionToSave);
        Expression foundExpression = repository.findById(savedExpression.getId()).orElse(null);
        assertThat(foundExpression).isNotNull();
        assertThat(foundExpression.getId()).isEqualTo(savedExpression.getId());
    }

    @Test
    @DisplayName("Get all expressions")
    void getAllExpressions_ExpressionsAreFound() {
        repository.save(new Expression("2+2", false, ExpressionStatus.COMPLETED, 4.0));
        repository.save(new Expression("3*3", false, ExpressionStatus.COMPLETED, 9.0));
        List<Expression> expressions = repository.findAll();
        assertThat(expressions).isNotEmpty();
        assertThat(expressions.size()).isEqualTo(2);
    }
}
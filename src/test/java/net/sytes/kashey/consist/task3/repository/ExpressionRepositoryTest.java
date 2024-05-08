package net.sytes.kashey.consist.task3.repository;

import net.sytes.kashey.consist.task3.model.Expression;
import net.sytes.kashey.consist.task3.model.ExpressionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
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

        Expression expressionToSave = new Expression(1, "2+2", false, ExpressionStatus.COMPLETED, 4.0);

        Expression savedExpression = repository.save(expressionToSave);

        assertThat(savedExpression).isNotNull();
        assertThat(savedExpression.getResult()).isEqualTo(4.0);
    }

    @Test
    @DisplayName("Delete existing expression from DB")
    void deleteExpression_ExpressionIsDeleted() {

        Expression savedExpression = repository.save(
                new Expression(1, "2+2", false, ExpressionStatus.COMPLETED, 4.0));

        repository.deleteById(savedExpression.getId());
        Expression deletedExpression = repository.findById(savedExpression.getId()).orElse(null);

        assertThat(deletedExpression).isNull();
    }

    @Test
    @DisplayName("Update expression description in DB")
    void updateDescription_ExpressionDescriptionIsUpdated() {

        Expression savedExpression = repository.save(
                new Expression(1, "2+2", false, ExpressionStatus.COMPLETED, 4.0));

        Expression expressionToUpdate = repository.findById(savedExpression.getId()).orElse(null);
        assert expressionToUpdate != null;
        expressionToUpdate.setDescription("Addition of 2 and 2");
        Expression updatedExpression = repository.save(expressionToUpdate);

        assertThat(updatedExpression).isNotNull();
        assertThat(updatedExpression.getDescription()).isEqualTo("Addition of 2 and 2");
    }
}
package net.sytes.kashey.consist.task3.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import net.sytes.kashey.consist.task3.model.Expression;
import net.sytes.kashey.consist.task3.model.ExpressionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
@Rollback
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml"})
class ExpressionRepositoryTest {

    private final ExpressionRepository repository;

    private final EntityManager entityManager;

    @Autowired
    public ExpressionRepositoryTest(ExpressionRepository repository, EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @Test
    @DisplayName("Save new expression into DB")
    void saveExpression_ExpressionIsCreated() {

        Expression expressionToSave = new Expression("2+2", false, ExpressionStatus.COMPLETED, 4.0);

        Expression savedExpression = repository.save(expressionToSave);

        assertThat(savedExpression).isNotNull();
        assertThat(savedExpression.getResult()).isEqualTo(4.0);

        Query query = entityManager.createQuery("SELECT e FROM Expression e WHERE e.id = :id");
        query.setParameter("id", savedExpression.getId());
        Expression foundExpression = (Expression) query.getSingleResult();

        assertThat(foundExpression).isNotNull();
        assertThat(foundExpression.getExpression()).isEqualTo("2+2");
        assertThat(foundExpression.isNeedLog()).isFalse();
        assertThat(foundExpression.getStatus()).isEqualTo(ExpressionStatus.COMPLETED);
        assertThat(foundExpression.getResult()).isEqualTo(4.0);
    }

    @Test
    @DisplayName("Delete existing expression from DB")
    void deleteExpression_ExpressionIsDeleted() {

        Expression savedExpression = repository.save(
                new Expression("2+2", false, ExpressionStatus.COMPLETED, 4.0));

        repository.deleteById(savedExpression.getId());

        Query query = entityManager.createQuery("SELECT COUNT(e) FROM Expression e WHERE e.id = :id");
        query.setParameter("id", savedExpression.getId());
        long count = (long) query.getSingleResult();
        assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("Update expression description in DB")
    void updateDescription_ExpressionDescriptionIsUpdated() {

        Expression savedExpression = repository.save(
                new Expression("2+2", false, ExpressionStatus.COMPLETED, 4.0));
        Expression expressionToUpdate = repository.findById(savedExpression.getId()).orElse(null);
        assert expressionToUpdate != null;
        Expression updatedExpression = new Expression(
                expressionToUpdate.getId(),
                expressionToUpdate.getExpression(),
                expressionToUpdate.isNeedLog(),
                expressionToUpdate.getStatus(),
                expressionToUpdate.getResult(),
                "Addition of 2 and 2"
        );

        Expression resultExpression = repository.save(updatedExpression);

        assertThat(resultExpression).isNotNull();
        assertThat(resultExpression.getDescription()).isEqualTo("Addition of 2 and 2");
    }

    @Test
    @DisplayName("Get expression by ID")
    void getExpressionById_ExpressionIsFound() {

        entityManager.createNativeQuery("""
                INSERT INTO expressions (expression, log, status, result)
                VALUES ('2+2', false, 'COMPLETED', 4.0)
                """).executeUpdate();

        Integer savedExpressionId = (Integer) entityManager.createNativeQuery("""
                SELECT id FROM expressions
                WHERE expression = '2+2' AND result = 4.0
                """).getSingleResult();

        Expression foundExpression = repository.findById(savedExpressionId).orElse(null);
        assertThat(foundExpression).isNotNull();
        assertThat(foundExpression.getId()).isEqualTo(savedExpressionId);
    }

    @Test
    @DisplayName("Get all expressions")
    void getAllExpressions_ExpressionsAreFound() {

        entityManager.createNativeQuery("""
                INSERT INTO expressions (expression, log, status, result)
                VALUES ('2+2', false, 'COMPLETED', 4.0)
                """).executeUpdate();
        entityManager.createNativeQuery("""
                INSERT INTO expressions (expression, log, status, result)
                VALUES ('3*3', false, 'COMPLETED', 9.0)
                """).executeUpdate();

        List<Expression> expressions = repository.findAll();

        assertThat(expressions).isNotEmpty();
        assertThat(expressions.size()).isEqualTo(2);
    }
}
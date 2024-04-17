package net.sytes.kashey.consist.task3.service;


import net.sytes.kashey.consist.task3.client.RestGitlabClient;
import net.sytes.kashey.consist.task3.dto.ExpressionDto;
import net.sytes.kashey.consist.task3.model.Expression;
import net.sytes.kashey.consist.task3.model.ExpressionStatus;
import net.sytes.kashey.consist.task3.model.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalcRequestServiceTest {

    @Mock
    private RestGitlabClient gitlabClient;


    private CalcRequestService service;

    @BeforeEach
    void setUp() {
        this.service = new CalcRequestService(gitlabClient);
    }

    @Test
    void addExpression_ValidExpressionWithLogging_ReturnsId_WriteLog() {

        when(gitlabClient.addNote(any(Note.class))).thenReturn(true);

        assertThat(service.addExpression("2+3", true)).isNotNull();
    }


    @Test
    void addExpression_InvalidExpression_ReturnsNull() {

        assertThat(service.addExpression("a+b", false)).isNull();
    }

    @Test
    void getResultById_ExistingId_ReturnsExpression() {

        Expression expectedResult = new Expression(1, "2+3", true);
        CompletableFuture<Expression> future = CompletableFuture.completedFuture(expectedResult);
        service.getExpressionPool().put(1, future);

        when(gitlabClient.addNote(any(Note.class))).thenReturn(true);
        Expression result = service.getResultById(1);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expectedResult);
    }


    @Test
    void getResultById_ExpressionNotExists_ReturnsNull() {

        Expression result = service.getResultById(999);

        assertThat(result).isNull();
    }

    @Test
    void getResultById_ExceptionThrown() throws InterruptedException, ExecutionException {

        CompletableFuture<Expression> future = mock(CompletableFuture.class);
        Map<Integer, CompletableFuture<Expression>> expressionPool = new ConcurrentHashMap<>();
        expressionPool.put(1, future);
        service.setExpressionPool(expressionPool);

        when(future.isDone()).thenReturn(true);
        when(future.get()).thenThrow(new ExecutionException(new RuntimeException()));

        assertThrows(RuntimeException.class, () -> service.getResultById(1));
    }

    @Test
    void getResultById_CalculationInProgress() {

        CompletableFuture<Expression> future = new CompletableFuture<>();
        service.getExpressionPool().put(1, future);

        Expression result = service.getResultById(1);

        assertThat(result).isNotNull();
        assertThat(result.getExpression()).isEqualTo("dummy");
    }

    @Test
    void getAllExpressions_ReturnsCorrectList() {

        Map<Integer, CompletableFuture<Expression>> expressionPool = new ConcurrentHashMap<>();
        CompletableFuture<Expression> completedFuture1 = CompletableFuture
                .completedFuture(new Expression(1, "2+3", true, ExpressionStatus.COMPLETED, 5.0));
        CompletableFuture<Expression> completedFuture2 = CompletableFuture
                .completedFuture(new Expression(2, "4*5", false, ExpressionStatus.COMPLETED, 20.0));
        CompletableFuture<Expression> inProgressFuture = new CompletableFuture<>();
        expressionPool.put(1, completedFuture1);
        expressionPool.put(2, completedFuture2);
        expressionPool.put(3, inProgressFuture);
        service.setExpressionPool(expressionPool);

        List<ExpressionDto> expectedResults = Arrays.asList(
                new ExpressionDto(1, ExpressionStatus.COMPLETED),
                new ExpressionDto(2, ExpressionStatus.COMPLETED),
                new ExpressionDto(3, ExpressionStatus.IN_PROGRESS)
        );

        List<ExpressionDto> result = service.getAllExpressions();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(3);
        assertThat(result).containsExactlyElementsOf(expectedResults);
    }

    @Test
    void testGetAllExpressions_ExceptionThrown() throws InterruptedException, ExecutionException {

        CompletableFuture<Expression> exceptionFuture = new CompletableFuture<>();
        exceptionFuture.completeExceptionally(new InterruptedException("Test exception"));

        Map<Integer, CompletableFuture<Expression>> expressionPool = new ConcurrentHashMap<>();
        expressionPool.put(1, exceptionFuture);
        service.setExpressionPool(expressionPool);

        assertThrows(RuntimeException.class, () -> {
            service.getAllExpressions();
        });
    }


    @Test
    void deleteById_RemovesExpressionFromPool() {

        Map<Integer, CompletableFuture<Expression>> expressionPool = new ConcurrentHashMap<>();
        CompletableFuture<Expression> future = CompletableFuture.completedFuture(new Expression("2+2"));
        expressionPool.put(1, future);
        service.setExpressionPool(expressionPool);

        boolean result = service.deleteById(1, true);

        assertThat(expressionPool.containsKey(1)).isFalse();
        assertThat(result).isTrue();
        verify(gitlabClient, times(1)).addNote(any(Note.class));
    }

    @Test
    void deleteById_ReturnsFalseIfExpressionNotPresent() {

        boolean needLog = true;

        boolean result = service.deleteById(1, needLog);

        assertThat(result).isFalse();
        verify(gitlabClient, never()).addNote(any(Note.class));
    }

    @Test
    void updateById_ReturnsTrueAndUpdateExpressionInPool() {

        CompletableFuture<Expression> future = CompletableFuture.completedFuture(new Expression("2+3", false));
        Map<Integer, CompletableFuture<Expression>> expressionPool = new ConcurrentHashMap<>();
        expressionPool.put(1, future);
        service.setExpressionPool(expressionPool);

        boolean result = service.updateById(1, "5+7", false);

        assertThat(result).isTrue();
    }

    @Test
    void updateById_ReturnsFalseIfExpressionNotFound() {

        CompletableFuture<Expression> future = new CompletableFuture<>();
        Map<Integer, CompletableFuture<Expression>> expressionPool = new ConcurrentHashMap<>();
        expressionPool.put(1, future);
        service.setExpressionPool(expressionPool);

        boolean result = service.updateById(1, "2+3", true);

        assertThat(result).isFalse();
    }

    @Test
    void updateById_ExpressionNotFound_ThrowsIllegalArgumentException() {

        assertThrows(IllegalArgumentException.class, () -> service.updateById(1, "2+3", true));
    }
}
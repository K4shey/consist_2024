package net.sytes.kashey.consist.task3.service;


import net.sytes.kashey.consist.task3.client.RestGitlabClient;
import net.sytes.kashey.consist.task3.dto.ExpressionDto;
import net.sytes.kashey.consist.task3.mapper.ExpressionMapper;
import net.sytes.kashey.consist.task3.model.Expression;
import net.sytes.kashey.consist.task3.model.ExpressionStatus;
import net.sytes.kashey.consist.task3.model.Note;
import net.sytes.kashey.consist.task3.repository.ExpressionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalcRequestServiceTest {

    @Mock
    private RestGitlabClient gitlabClient;

    @Mock
    private ExpressionRepository repository;

    private CalcRequestService service;

    @Mock
    private ExpressionMapper mapper;


    @BeforeEach
    void setUp() {
        this.service = new CalcRequestService(gitlabClient, repository, mapper);
    }

    @Test
    void addExpression_ValidExpressionWithLogging_ReturnsId_WriteLog() {

        when(gitlabClient.addNote(any(Note.class))).thenReturn(true);
        Expression savedExpression = new Expression(1, "2+3", true, ExpressionStatus.IN_PROGRESS,
                5.0, "");
        when(repository.save(any(Expression.class))).thenReturn(savedExpression);

        Integer expressionId = Integer.valueOf(service.addExpression("2+3", true, ""));

        assertThat(expressionId).isNotNull();
        assertThat(expressionId).isEqualTo(1);
        ArgumentCaptor<Expression> expressionCaptor = ArgumentCaptor.forClass(Expression.class);
        verify(repository).save(expressionCaptor.capture());
        Expression capturedExpression = expressionCaptor.getValue();
        assertThat(capturedExpression.getExpression()).isEqualTo("2+3");
        assertThat(capturedExpression.isNeedLog()).isTrue();
        assertThat(capturedExpression.getStatus()).isEqualTo(ExpressionStatus.IN_PROGRESS);
        verify(gitlabClient).addNote(any(Note.class));
    }

    @Test
    void addExpression_InvalidExpression__ExceptionThrown() {

        assertThrows(RuntimeException.class, () -> service.addExpression("a+b", false, ""));
    }

    @Test
    void getResultById_ExistingId_ReturnsExpression() {

        Expression expectedResult = new Expression(1, "2+3", true, ExpressionStatus.COMPLETED,
                5.0, "");
        when(repository.findById(1)).thenReturn(Optional.of(expectedResult));
        when(gitlabClient.addNote(any(Note.class))).thenReturn(true);

        ExpressionDto result = service.getResultById(1);

        assertThat(result).isEqualTo(mapper.toDto(expectedResult));
    }

    @Test
    void getResultById_ExpressionNotExists_ReturnsNull() {

        ExpressionDto result = service.getResultById(999);

        assertThat(result).isNull();
    }

    @Test
    void getResultById_ExceptionThrown() {

        when(repository.findById(1)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> service.getResultById(1));
    }

    @Test
    void getResultById_CalculationInProgress() {

        Expression inProgressExpression = new Expression(1, "dummy", true, ExpressionStatus.IN_PROGRESS, 0.0, "");
        ExpressionDto inProgressExpressionDto = new ExpressionDto(1, ExpressionStatus.IN_PROGRESS);
        when(repository.findById(1)).thenReturn(Optional.of(inProgressExpression));
        when(mapper.toDto(inProgressExpression)).thenReturn(inProgressExpressionDto);

        ExpressionDto result = service.getResultById(1);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1);
        assertThat(result.status()).isEqualTo(ExpressionStatus.IN_PROGRESS);
    }

    @Test
    void getAllExpressions_ReturnsCorrectList() {

        Expression expression1 = new Expression(1, "2+3", true, ExpressionStatus.COMPLETED, 5.0, "");
        Expression expression2 = new Expression(2, "4*5", false, ExpressionStatus.COMPLETED, 20.0, "");
        Expression expression3 = new Expression(3, "6-1", false, ExpressionStatus.IN_PROGRESS, 0.0, "");
        List<Expression> expressions = Arrays.asList(expression1, expression2, expression3);
        when(repository.findAll()).thenReturn(expressions);
        when(mapper.toDto(expression1)).thenReturn(new ExpressionDto(1, ExpressionStatus.COMPLETED));
        when(mapper.toDto(expression2)).thenReturn(new ExpressionDto(2, ExpressionStatus.COMPLETED));
        when(mapper.toDto(expression3)).thenReturn(new ExpressionDto(3, ExpressionStatus.IN_PROGRESS));
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
    void testGetAllExpressions_ExceptionThrown() {

        when(repository.findAll()).thenThrow(new RuntimeException("Test exception"));

        assertThrows(RuntimeException.class, () -> service.getAllExpressions());
    }

    @Test
    void deleteById_ReturnsFalseIfExpressionNotPresent() {

        boolean needLog = true;

        boolean result = service.deleteById(1, needLog);

        assertThat(result).isFalse();
        verify(gitlabClient, never()).addNote(any(Note.class));
    }

    @Test
    void updateById_ReturnsTrueAndUpdateExpression() {

        Expression originalExpression = new Expression("2+3", false);
        Expression updatedExpression = new Expression("5+7", false);
        when(repository.findById(1)).thenReturn(Optional.of(originalExpression));

        boolean result = service.updateById(1, "5+7", false);

        assertThat(updatedExpression.getExpression()).isEqualTo("5+7");
        assertThat(result).isTrue();
    }

    @Test
    void updateById_ReturnsFalseIfExpressionNotFound() {

        when(repository.findById(1)).thenReturn(Optional.empty());

        boolean result = service.updateById(1, "2+3", true);

        assertThat(result).isFalse();
    }


    @Test
    void updateDescription_ExistingExpression_SuccessfulUpdate() {

        Expression existingExpression = new Expression("2+3", true, "Old Description");
        when(repository.findById(1)).thenReturn(Optional.of(existingExpression));

        boolean result = service.updateDescription(1, "New Description");

        assertThat(result).isTrue();
        ArgumentCaptor<Expression> expressionCaptor = ArgumentCaptor.forClass(Expression.class);
        verify(repository).save(expressionCaptor.capture());
        Expression capturedExpression = expressionCaptor.getValue();
        assertThat(capturedExpression.getDescription()).isEqualTo("New Description");
    }

    @Test
    void updateDescription_NonExistingExpression_UnsuccessfulUpdate() {

        when(repository.findById(1)).thenReturn(Optional.empty());

        boolean result = service.updateDescription(1, "New Description");

        assertThat(result).isFalse();
        verify(repository, Mockito.never()).save(Mockito.any());
    }
}
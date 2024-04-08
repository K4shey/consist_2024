package net.sytes.kashey.consist.task3.controller;

import net.sytes.kashey.consist.task3.service.CalculatorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = {CalculatorController.class})
class CalculatorControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    CalculatorService calculatorService;

    @Test
    void doCalculation_whenCorrectExpression_thenReturnsResult() {
        BDDMockito.given(calculatorService.calculate(any(String.class))).willReturn(Mono.just("4"));

        WebTestClient.ResponseSpec calculationResult = webTestClient.get().uri("/api/calculate?expr=2+2").exchange();

        calculationResult
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.result", "4");
    }

    @Test
    void doCalculation_whenIncorrectExpression_thenThrowsException() {
        BDDMockito.given(calculatorService.calculate(any(String.class)))
                .willThrow(new IllegalArgumentException("Something went wrong"));
        WebTestClient.ResponseSpec calculationResult = webTestClient.get().uri("/api/calculate?abc=1+3").exchange();

        calculationResult
                .expectStatus()
                .is4xxClientError()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.error", "Something went wrong");
    }
}
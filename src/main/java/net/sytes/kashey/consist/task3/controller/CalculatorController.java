package net.sytes.kashey.consist.task3.controller;

import net.sytes.kashey.consist.task3.service.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/calculate")

public class CalculatorController {

    private final CalculatorService service;

    @Autowired
    public CalculatorController(CalculatorService service) {
        this.service = service;
    }

    @GetMapping
    public Mono<ResponseEntity<String>> doCalculation(@RequestParam(value = "expr") String expression) {

        return service.calculate(expression)
                .flatMap(result -> Mono.just(ResponseEntity.ok().body(result)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}

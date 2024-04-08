package net.sytes.kashey.consist.task3.controller;

import net.sytes.kashey.consist.task3.model.ExpressionModelStatus;
import net.sytes.kashey.consist.task3.model.ExpressionModel;
import net.sytes.kashey.consist.task3.service.CalcRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/calcrequest")

public class CalcRequestController {

    private final CalcRequestService service;

    @Autowired
    public CalcRequestController(CalcRequestService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> addExpression(@RequestParam(value = "expr") String expression,
                                                @RequestParam(value = "needlog", required = false,
                                                        defaultValue = "false") boolean needLog) {

        if (service.addCalculateRequest(URLEncoder.encode(expression, StandardCharsets.UTF_8), needLog)) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getResultById(@PathVariable(value = "id") int id) {
        ExpressionModel result = service.getResultById(id);
        if (result != null) {
            if (result.getStatus() == ExpressionModelStatus.CALCULATED) {
                return ResponseEntity.ok(result);
            } else if (result.getStatus() == ExpressionModelStatus.IN_PROGRESS) {
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            }
        }
        return ResponseEntity.notFound().build();
    }
}

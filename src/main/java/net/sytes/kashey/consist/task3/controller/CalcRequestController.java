package net.sytes.kashey.consist.task3.controller;

import net.sytes.kashey.consist.task3.dto.ExpressionDto;
import net.sytes.kashey.consist.task3.model.Expression;
import net.sytes.kashey.consist.task3.model.ExpressionStatus;
import net.sytes.kashey.consist.task3.service.CalcRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/calcrequest")

public class CalcRequestController {

    private final CalcRequestService service;

    @Autowired
    public CalcRequestController(CalcRequestService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> add(@RequestParam(value = "expr") String expression,
                                      @RequestParam(value = "needlog", required = false,
                                              defaultValue = "false") boolean needLog) {

        String newExpressionId = service.addExpression(URLEncoder.encode(expression, StandardCharsets.UTF_8), needLog);
        if (newExpressionId != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/api/calcrequest/" + newExpressionId);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .headers(headers)
                    .build();
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expression> getResultById(@PathVariable(value = "id") int id) {
        Expression result = service.getResultById(id);
        if (result != null) {
            if (result.getStatus() == ExpressionStatus.COMPLETED) {
                return ResponseEntity.ok(result);
            } else if (result.getStatus() == ExpressionStatus.IN_PROGRESS) {
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping()
    public ResponseEntity<List<ExpressionDto>> getAll() {
        return ResponseEntity.ok(service.getAllExpressions());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Expression> deleteById(@PathVariable(value = "id") int id,
                                                 @RequestParam(value = "needlog", required = false,
                                                         defaultValue = "false") boolean needLog) {
        if (service.deleteById(id, needLog)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expression> updateById(@PathVariable(value = "id") int id,
                                                 @RequestParam(value = "expr") String expression,
                                                 @RequestParam(value = "needlog", required = false,
                                                         defaultValue = "false") boolean needLog) {

        if (service.updateById(id, URLEncoder.encode(expression, StandardCharsets.UTF_8), needLog)) {
            return ResponseEntity.ok().build();
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

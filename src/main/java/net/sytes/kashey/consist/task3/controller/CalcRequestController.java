package net.sytes.kashey.consist.task3.controller;

import net.sytes.kashey.consist.task3.dto.ExpressionDto;
import net.sytes.kashey.consist.task3.dto.UpdateExpressionDto;
import net.sytes.kashey.consist.task3.model.ExpressionStatus;
import net.sytes.kashey.consist.task3.service.CalcRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public ResponseEntity<ExpressionDto> add(@RequestBody ExpressionDto expressionDto) {

        String newExpressionId = service.addExpression(
                URLEncoder.encode(expressionDto.expression(), StandardCharsets.UTF_8),
                expressionDto.needLog(),
                expressionDto.description() == null ? "" : URLEncoder.encode(expressionDto.description(), StandardCharsets.UTF_8));
        if (newExpressionId != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/api/calcrequest/" + newExpressionId);

            ExpressionDto createdExpressionDto = new ExpressionDto(
                    expressionDto.expression(),
                    expressionDto.needLog(),
                    ExpressionStatus.IN_PROGRESS,
                    expressionDto.description()
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .headers(headers)
                    .body(createdExpressionDto);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpressionDto> getResultById(@PathVariable(value = "id") int id) {

        ExpressionDto result = service.getResultById(id);
        if (result != null) {
            if (result.status() == ExpressionStatus.COMPLETED) {
                return ResponseEntity.ok(result);
            } else if (result.status() == ExpressionStatus.IN_PROGRESS) {
                return ResponseEntity.status(HttpStatus.ACCEPTED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(result);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping()
    public ResponseEntity<List<ExpressionDto>> getAll() {
        return ResponseEntity.ok(service.getAllExpressions());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UpdateExpressionDto> deleteById(@PathVariable(value = "id") int id,
                                                          @RequestParam(value = "needlog", required = false,
                                                                  defaultValue = "false") boolean needLog) {
        if (service.deleteById(id, needLog)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateExpressionDto> updateById(@PathVariable(value = "id") int id,
                                                          @RequestBody UpdateExpressionDto updateExpressionDto) {
        if (service.updateById(id, URLEncoder.encode(updateExpressionDto.description(), StandardCharsets.UTF_8))) {
            return ResponseEntity.ok().build();
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
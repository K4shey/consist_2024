package net.sytes.kashey.consist.task3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sytes.kashey.consist.task3.dto.ExpressionDto;
import net.sytes.kashey.consist.task3.model.ExpressionStatus;
import net.sytes.kashey.consist.task3.service.CalcRequestService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CalcRequestController.class)
class CalcRequestControllerTest {

    @MockBean
    CalcRequestService service;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void add_ExpressionValid_ReturnStatusCreated_ExpressionIdInHeader() throws Exception {

        ExpressionDto expressionDto = new ExpressionDto(
                "2+3",
                false,
                ExpressionStatus.IN_PROGRESS,
                ""
        );
        String expressionDtoJson = objectMapper.writeValueAsString(expressionDto);

        BDDMockito.given(service.addExpression(URLEncoder.encode("2+3", StandardCharsets.UTF_8), false, ""))
                .willReturn("1");

        mockMvc.perform(post("/api/calcrequest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expressionDtoJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/calcrequest/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                            "expression": "2+3",
                            "needLog": false,
                            "status": "IN_PROGRESS",
                            "description": ""
                        }
                        """));
    }

    @Test
    void add_ExpressionInvalid_ReturnStatusInternalServerError() throws Exception {

        String expressionDtoJson = """
                {
                    "expression": "a+b",
                    "needLog": false
                }
                """;

        BDDMockito
                .given(service.addExpression(URLEncoder.encode("a+b", StandardCharsets.UTF_8), false, ""))
                .willReturn(null);

        mockMvc.perform(post("/api/calcrequest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expressionDtoJson))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getResultById_ExpressionFound_StatusCompleted_ReturnResult() throws Exception {

        ExpressionDto completedExpressionDto = new ExpressionDto(
                "2+3",
                false,
                ExpressionStatus.COMPLETED,
                ""
        );
        String expressionDtoJson = """
                    {
                        "expression": "2+3",
                        "needLog": false,
                        "status": "COMPLETED",
                        "description": ""
                    }
                """;
        BDDMockito.given(service.getResultById(1)).willReturn(completedExpressionDto);

        mockMvc.perform(get("/api/calcrequest/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expressionDtoJson));
    }

    @Test
    void getResultById_ExpressionFound_StatusInProgress_ReturnStatusAccepted() throws Exception {

        ExpressionDto expressionCalculationInProgress = new ExpressionDto(
                "2+3",
                false,
                ExpressionStatus.IN_PROGRESS,
                ""
        );
        BDDMockito.given(service.getResultById(1)).willReturn(expressionCalculationInProgress);

        mockMvc.perform(get("/api/calcrequest/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expressionCalculationInProgress)));
    }

    @Test
    void getResultById_ExpressionNotFound_ReturnStatusNotFound() throws Exception {

        BDDMockito.given(service.getResultById(1)).willReturn(null);

        mockMvc.perform(get("/api/calcrequest/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll_ReturnsListOfDto_StatusOK() throws Exception {

        List<ExpressionDto> listOfDto = Arrays.asList(
                new ExpressionDto(
                        "2+3",
                        true,
                        ExpressionStatus.COMPLETED,
                        ""
                ),
                new ExpressionDto(
                        "4*5",
                        false,
                        ExpressionStatus.COMPLETED,
                        ""
                ),
                new ExpressionDto(
                        "6-1",
                        false,
                        ExpressionStatus.IN_PROGRESS,
                        ""
                )
        );

        BDDMockito
                .given(service.getAllExpressions())
                .willReturn(listOfDto);

        mockMvc.perform(get("/api/calcrequest"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(listOfDto)));
    }

    @Test
    void deleteById_ExpressionFound_ReturnStatusOK() throws Exception {

        BDDMockito
                .given(service.deleteById(1, false))
                .willReturn(true);

        mockMvc.perform(delete("/api/calcrequest/1")
                        .param("needlog", "false"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteById_ExpressionNotFound_ReturnStatusNotFound() throws Exception {

        BDDMockito
                .given(service.deleteById(999, false))
                .willReturn(false);

        mockMvc.perform(delete("/api/calcrequest/999")
                        .param("needlog", "false"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateById_ExpressionFound_ReturnStatusOK() throws Exception {

        String requestJson = """
                {
                    "description": "Updated description"
                }
                """;

        BDDMockito.given(service.updateById(1, URLEncoder.encode("Updated description", StandardCharsets.UTF_8)))
                .willReturn(true);

        mockMvc.perform(put("/api/calcrequest/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void updateById_ExpressionNotFound_ReturnStatusNotFound() throws Exception {

        String requestJson = """
                {
                    "description": "Updated description"
                }
                """;

        BDDMockito.given(service.updateById(1, URLEncoder.encode("Updated description", StandardCharsets.UTF_8)))
                .willReturn(false);

        mockMvc.perform(put("/api/calcrequest/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
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
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

        BDDMockito
                .given(service.addExpression(URLEncoder.encode("2+3", StandardCharsets.UTF_8), false, ""))
                .willReturn("1");

        mockMvc.perform(post("/api/calcrequest")
                        .param("expr", "2+3")
                        .param("needlog", "false"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/calcrequest/1"));
    }

    @Test
    void add_ExpressionInvalid_ReturnStatusInternalServerError() throws Exception {

        BDDMockito
                .given(service.addExpression(URLEncoder.encode("a+b", StandardCharsets.UTF_8), false, ""))
                .willReturn(null);

        mockMvc.perform(post("/api/calcrequest")
                        .param("expr", "a+b")
                        .param("needlog", "false"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getResultById_ExpressionFound_StatusCompleted_ReturnResult() throws Exception {

        ExpressionDto completedExpressionDto = new ExpressionDto(1, ExpressionStatus.COMPLETED);

        BDDMockito
                .given(service.getResultById(1))
                .willReturn(completedExpressionDto);

        mockMvc.perform(get("/api/calcrequest/1")
                        .param("expr", "2+3"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(completedExpressionDto)));
    }

    @Test
    void getResultById_ExpressionFound_StatusInProgress_ReturnStatusAccepted() throws Exception {

        ExpressionDto expressionCalculationInProgress = new ExpressionDto(1, ExpressionStatus.IN_PROGRESS);

        BDDMockito
                .given(service.getResultById(1))
                .willReturn(expressionCalculationInProgress);

        mockMvc.perform(get("/api/calcrequest/1")
                        .param("expr", "2+3")
                        .param("needlog", "false"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isAccepted());
    }

    @Test
    void getResultById_ExpressionNotFound_ReturnStatusNotFound() throws Exception {

        BDDMockito
                .given(service.getResultById(1))
                .willReturn(null);

        mockMvc.perform(get("/api/calcrequest/1")

                        .param("expr", "2+3")
                        .param("needlog", "false"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll_ReturnsListOfDto_StatusOK() throws Exception {
        List<ExpressionDto> listOfDto = new ArrayList<>();
        listOfDto.add(new ExpressionDto(1, ExpressionStatus.COMPLETED));
        listOfDto.add(new ExpressionDto(2, ExpressionStatus.COMPLETED));
        listOfDto.add(new ExpressionDto(3, ExpressionStatus.IN_PROGRESS));

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

        BDDMockito.given(service.updateById(any(Integer.class), any(String.class), eq(false))).willReturn(true);

        mockMvc.perform(put("/api/calcrequest/1")
                        .param("expr", "3+7")
                        .param("needlog", "false"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void updateById_ExpressionNotFound_ReturnStatusNotFound() throws Exception {

        BDDMockito.given(service.updateById(any(Integer.class), any(String.class), eq(false))).willReturn(false);

        mockMvc.perform(put("/api/calcrequest/999")
                        .param("expr", "3+7")
                        .param("needlog", "false"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateDescription_ExpressionFound_ReturnStatusOK() throws Exception {

        BDDMockito.given(service.updateDescription(any(Integer.class), any(String.class))).willReturn(true);

        mockMvc.perform(put("/api/calcrequest/1/descriptions")
                        .param("text", "Updated description"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void updateDescription_ExpressionNotFound_ReturnStatusNotFound() throws Exception {

        BDDMockito.given(service.updateDescription(any(Integer.class), any(String.class))).willReturn(false);

        mockMvc.perform(put("/api/calcrequest/999/description")
                        .param("text", "Updated description"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }
}
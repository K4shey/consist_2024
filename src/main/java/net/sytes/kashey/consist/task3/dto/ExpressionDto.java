package net.sytes.kashey.consist.task3.dto;

import net.sytes.kashey.consist.task3.model.ExpressionStatus;

public record ExpressionDto(String expression, boolean needLog, ExpressionStatus status, String description) {
}
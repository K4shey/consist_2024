package net.sytes.kashey.consist.task3.mapper;

import net.sytes.kashey.consist.task3.dto.ExpressionDto;
import net.sytes.kashey.consist.task3.model.Expression;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExpressionMapper {

    ExpressionDto toDto(Expression model);
}

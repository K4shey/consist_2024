package net.sytes.kashey.consist.task3.mapper;

import net.sytes.kashey.consist.task3.dto.ExpressionDto;
import net.sytes.kashey.consist.task3.model.Expression;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExpressionMapper {
    ExpressionMapper INSTANCE = Mappers.getMapper(ExpressionMapper.class);

    ExpressionDto ToDto(Expression model);
}

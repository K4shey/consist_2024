package net.sytes.kashey.consist.task3.mapper;

import net.sytes.kashey.consist.task3.dto.IntegrationDto;
import net.sytes.kashey.consist.task3.model.Note;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NoteMapper {
    NoteMapper INSTANCE = Mappers.getMapper(NoteMapper.class);

    IntegrationDto ToDto(Note note);
}

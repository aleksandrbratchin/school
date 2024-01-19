package ru.hogwarts.school.mapper.faculty;

import org.springframework.stereotype.Component;
import ru.hogwarts.school.dto.faculty.FacultyAddRequestDto;
import ru.hogwarts.school.mapper.RequestMapper;
import ru.hogwarts.school.model.faculty.Faculty;
import ru.hogwarts.school.services.impl.FacultyServiceImpl;

import java.util.ArrayList;

@Component
public class FacultyAddRequestMapper implements RequestMapper<Faculty, FacultyAddRequestDto> {

    @Override
    public Faculty fromDto(FacultyAddRequestDto dto) {
        return Faculty.builder()
                .name(dto.name())
                .color(dto.color())
                .students(new ArrayList<>())
                .build();
    }

}

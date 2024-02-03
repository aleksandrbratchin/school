package ru.hogwarts.school.mapper.faculty;

import org.springframework.stereotype.Component;
import ru.hogwarts.school.dto.faculty.FacultyInfoDto;
import ru.hogwarts.school.mapper.ResponseMapper;
import ru.hogwarts.school.model.faculty.Faculty;

@Component
public class FacultyInfoMapper implements ResponseMapper<Faculty, FacultyInfoDto> {

    @Override
    public FacultyInfoDto toDto(Faculty obj) {
        return new FacultyInfoDto(
                obj.getId(),
                obj.getName(),
                obj.getColor()
        );
    }

}

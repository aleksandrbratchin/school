package ru.hogwarts.school.mapper.faculty;

import org.springframework.stereotype.Component;
import ru.hogwarts.school.dto.faculty.FacultyResponseDto;
import ru.hogwarts.school.mapper.ResponseMapper;
import ru.hogwarts.school.mapper.student.StudentResponseMapper;
import ru.hogwarts.school.model.faculty.Faculty;

@Component
public class FacultyResponseMapper implements ResponseMapper<Faculty, FacultyResponseDto> {

    private final StudentResponseMapper studentResponseMapper;

    public FacultyResponseMapper(StudentResponseMapper studentResponseMapper) {
        this.studentResponseMapper = studentResponseMapper;
    }

    @Override
    public FacultyResponseDto toDto(Faculty obj) {
        return new FacultyResponseDto(
                obj.getId(),
                obj.getName(),
                obj.getColor(),
                obj.getStudents().stream().map(
                        studentResponseMapper::toDto
                ).toList()
        );
    }
}

package ru.hogwarts.school.mapper.student;

import org.springframework.stereotype.Component;
import ru.hogwarts.school.dto.student.StudentResponseDto;
import ru.hogwarts.school.mapper.ResponseMapper;
import ru.hogwarts.school.mapper.faculty.FacultyInfoMapper;
import ru.hogwarts.school.model.student.Student;

@Component
public class StudentResponseMapper implements ResponseMapper<Student, StudentResponseDto> {

    private final FacultyInfoMapper facultyInfoMapper;

    public StudentResponseMapper(FacultyInfoMapper facultyInfoMapper) {
        this.facultyInfoMapper = facultyInfoMapper;
    }

    @Override
    public StudentResponseDto toDto(Student dto) {
        return new StudentResponseDto(
                dto.getId(),
                dto.getName(),
                dto.getAge(),
                dto.getFaculty() == null ? null : facultyInfoMapper.toDto(dto.getFaculty())
        );
    }
}

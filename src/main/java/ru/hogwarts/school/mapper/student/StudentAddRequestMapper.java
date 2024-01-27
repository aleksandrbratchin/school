package ru.hogwarts.school.mapper.student;

import org.springframework.stereotype.Component;
import ru.hogwarts.school.dto.student.StudentAddRequestDto;
import ru.hogwarts.school.mapper.RequestMapper;
import ru.hogwarts.school.model.faculty.Faculty;
import ru.hogwarts.school.model.student.Student;
import ru.hogwarts.school.services.impl.FacultyServiceImpl;
import ru.hogwarts.school.specifications.FacultySpecification;

import java.util.Optional;

@Component
public class StudentAddRequestMapper implements RequestMapper<Student, StudentAddRequestDto> {

    private final FacultyServiceImpl facultyService;

    public StudentAddRequestMapper(FacultyServiceImpl facultyService) {
        this.facultyService = facultyService;
    }

    @Override
    public Student fromDto(StudentAddRequestDto dto) {
        Optional.ofNullable(dto.facultyId()).orElseThrow(IllegalArgumentException::new);
        Faculty faculty = facultyService.findOne(FacultySpecification.idEqual(dto.facultyId()));
        return Student.builder()
                .name(dto.name())
                .age(dto.age())
                .faculty(faculty)
                .build();
    }

}

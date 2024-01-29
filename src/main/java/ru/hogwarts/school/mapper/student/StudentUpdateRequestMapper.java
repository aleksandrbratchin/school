package ru.hogwarts.school.mapper.student;

import org.springframework.stereotype.Component;
import ru.hogwarts.school.dto.student.StudentUpdateRequestDto;
import ru.hogwarts.school.mapper.RequestMapper;
import ru.hogwarts.school.model.faculty.Faculty;
import ru.hogwarts.school.model.student.Student;
import ru.hogwarts.school.services.impl.FacultyServiceImpl;
import ru.hogwarts.school.services.impl.StudentServiceImpl;
import ru.hogwarts.school.specifications.FacultySpecification;
import ru.hogwarts.school.specifications.StudentSpecification;

import java.util.Optional;

@Component
public class StudentUpdateRequestMapper implements RequestMapper<Student, StudentUpdateRequestDto> {

    private final StudentServiceImpl studentService;
    private final FacultyServiceImpl facultyService;

    public StudentUpdateRequestMapper(StudentServiceImpl studentService, FacultyServiceImpl facultyService) {
        this.studentService = studentService;
        this.facultyService = facultyService;
    }

    @Override
    public Student fromDto(StudentUpdateRequestDto dto) {
        Optional.ofNullable(dto).orElseThrow(IllegalArgumentException::new);
        Optional.ofNullable(dto.id()).orElseThrow(IllegalArgumentException::new);
        Faculty faculty = dto.facultyId() == null ? null : facultyService.findOne(FacultySpecification.idEqual(dto.facultyId()));
        Student student = studentService.findOne(StudentSpecification.idEqual(dto.id()));
        student.setName(dto.name());
        student.setAge(dto.age());
        student.setFaculty(faculty);
        return student;
    }

}

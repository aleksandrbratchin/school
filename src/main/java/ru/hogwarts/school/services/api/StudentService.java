package ru.hogwarts.school.services.api;

import org.springframework.data.domain.PageRequest;
import ru.hogwarts.school.dto.faculty.FacultyResponseDto;
import ru.hogwarts.school.dto.student.StudentAddRequestDto;
import ru.hogwarts.school.dto.student.StudentResponseDto;
import ru.hogwarts.school.dto.student.StudentUpdateRequestDto;
import ru.hogwarts.school.model.student.Student;

import java.util.List;
import java.util.UUID;

public interface StudentService extends CRUDService<UUID, Student> {
    StudentResponseDto create(StudentAddRequestDto student);

    StudentResponseDto update(StudentUpdateRequestDto studentDto);

    StudentResponseDto deleteById(UUID id);

    List<StudentResponseDto> findAllDto();

    List<StudentResponseDto> findAllDto(PageRequest pageRequest);

    List<StudentResponseDto> filterByAge(Integer age);

    FacultyResponseDto getFacultyById(UUID id);

    List<StudentResponseDto> findByAgeBetween(Integer min, Integer max);

    Double getAverageAge();

    Integer getCountStudents();

    List<StudentResponseDto> getLastFiveOldStudents();

    List<String> nameStartsWithLetterA();

    Double getAverageAgeWithStream();
}

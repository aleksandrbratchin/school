package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.dto.faculty.FacultyResponseDto;
import ru.hogwarts.school.dto.student.StudentAddRequestDto;
import ru.hogwarts.school.dto.student.StudentResponseDto;
import ru.hogwarts.school.dto.student.StudentUpdateRequestDto;
import ru.hogwarts.school.mapper.RequestMapper;
import ru.hogwarts.school.mapper.ResponseMapper;
import ru.hogwarts.school.model.faculty.Faculty;
import ru.hogwarts.school.model.student.Student;
import ru.hogwarts.school.services.api.StudentService;
import ru.hogwarts.school.services.impl.StudentServiceImpl;
import ru.hogwarts.school.specifications.StudentSpecification;

import java.util.UUID;

@RestController
@RequestMapping("student")
public class StudentController {
    private final StudentService service;
    private final RequestMapper<Student, StudentUpdateRequestDto> studentUpdateRequestMapper;
    private final RequestMapper<Student, StudentAddRequestDto> studentAddRequestMapper;
    private final ResponseMapper<Student, StudentResponseDto> studentResponseMapper;
    private final ResponseMapper<Faculty, FacultyResponseDto> facultyResponseMapper;

    public StudentController(
            @Qualifier("studentServiceImpl") StudentServiceImpl service,
            RequestMapper<Student, StudentUpdateRequestDto> studentUpdateRequestMapper,
            RequestMapper<Student, StudentAddRequestDto> studentAddRequestMapper,
            ResponseMapper<Student, StudentResponseDto> studentResponseMapper, ResponseMapper<Faculty, FacultyResponseDto> facultyResponseMapper) {
        this.service = service;
        this.studentUpdateRequestMapper = studentUpdateRequestMapper;
        this.studentAddRequestMapper = studentAddRequestMapper;
        this.studentResponseMapper = studentResponseMapper;
        this.facultyResponseMapper = facultyResponseMapper;
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(
            @PathVariable("id") UUID id
    ) {
        try {
            return ResponseEntity.ok(
                    studentResponseMapper.toDto(
                            service.delete(id)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping()
    public ResponseEntity<?> add(
            @RequestBody StudentAddRequestDto student
    ) {
        try {
            return ResponseEntity.ok(
                    studentResponseMapper.toDto(
                            service.create(
                                    studentAddRequestMapper.fromDto(student)
                            )
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping()
    public ResponseEntity<?> update(
            @RequestBody StudentUpdateRequestDto student
    ) {
        try {
            return ResponseEntity.ok(
                    studentResponseMapper.toDto(
                            service.update(
                                    studentUpdateRequestMapper.fromDto(student)
                            )
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "All")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(
                    service.findAll()
                            .stream()
                            .map(studentResponseMapper::toDto)
                            .toList()
            );
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "filterByAge")
    public ResponseEntity<?> filterByAge(@RequestParam Integer age) {
        try {
            return ResponseEntity.ok(
                    service.findAll(StudentSpecification.ageEqual(age))
                            .stream()
                            .map(studentResponseMapper::toDto)
                            .toList()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "{id}/faculty")
    public ResponseEntity<?> Faculty(@PathVariable("id") UUID id) {
        try {
            return ResponseEntity.ok(
                    facultyResponseMapper.toDto(
                            service.findOne(StudentSpecification.idEqual(id)).getFaculty()
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "findByAgeBetween")
    public ResponseEntity<?> findByAgeBetween(
            @RequestParam Integer min,
            @RequestParam Integer max
    ) {
        try {
            return ResponseEntity.ok(
                    service.findAll(StudentSpecification.ageInBetween(min, max))
                            .stream()
                            .map(studentResponseMapper::toDto)
                            .toList()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "getAverageAge")
    public ResponseEntity<?> getAverageAge() {
        try {
            return ResponseEntity.ok(
                    service.getAverageAge()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "getCountStudents")
    public ResponseEntity<?> getCountStudents() {
        try {
            return ResponseEntity.ok(
                    service.getCountStudents()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "getLastFiveOldStudents")
    public ResponseEntity<?> getLastFiveOldStudents() {
        try {
            return ResponseEntity.ok(
                    service.getLastFiveOldStudents()
                            .stream()
                            .map(studentResponseMapper::toDto)
                            .toList()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}

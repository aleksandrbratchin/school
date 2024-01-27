package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.dto.faculty.FacultyAddRequestDto;
import ru.hogwarts.school.dto.faculty.FacultyInfoDto;
import ru.hogwarts.school.dto.faculty.FacultyResponseDto;
import ru.hogwarts.school.dto.student.StudentResponseDto;
import ru.hogwarts.school.mapper.Mapper;
import ru.hogwarts.school.mapper.RequestMapper;
import ru.hogwarts.school.mapper.ResponseMapper;
import ru.hogwarts.school.model.faculty.Faculty;
import ru.hogwarts.school.model.student.Student;
import ru.hogwarts.school.services.api.FacultyService;
import ru.hogwarts.school.specifications.FacultySpecification;

import java.util.UUID;

@RestController
@RequestMapping("faculty")
public class FacultyController {

    private final FacultyService service;
    private final Mapper<Faculty, FacultyInfoDto> facultyInfoMapper;
    private final RequestMapper<Faculty, FacultyAddRequestDto> facultyAddRequestMapper;
    private final ResponseMapper<Faculty, FacultyResponseDto> facultyResponseMapper;
    private final ResponseMapper<Student, StudentResponseDto> studentResponseMapper;

    public FacultyController(
            @Qualifier("facultyServiceImpl") FacultyService service,
            Mapper<Faculty, FacultyInfoDto> facultyInfoMapper,
            RequestMapper<Faculty, FacultyAddRequestDto> facultyAddRequestMapper,
            ResponseMapper<Faculty, FacultyResponseDto> facultyResponseMapper,
            ResponseMapper<Student, StudentResponseDto> studentResponseMapper) {
        this.service = service;
        this.facultyInfoMapper = facultyInfoMapper;
        this.facultyAddRequestMapper = facultyAddRequestMapper;
        this.facultyResponseMapper = facultyResponseMapper;
        this.studentResponseMapper = studentResponseMapper;
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(
            @PathVariable("id") UUID id
    ) {
        try {
            return ResponseEntity.ok(
                    facultyInfoMapper.toDto(service.delete(id))
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping()
    public ResponseEntity<?> add(
            @RequestBody FacultyAddRequestDto faculty
    ) {
        try {
            return ResponseEntity.ok(
                    facultyInfoMapper.toDto(
                            service.create(
                                    facultyAddRequestMapper.fromDto(faculty)
                            )
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping()
    public ResponseEntity<?> update(
            @RequestBody FacultyInfoDto faculty
    ) {
        try {
            return ResponseEntity.ok(
                    facultyInfoMapper.toDto(
                            service.update(
                                    facultyInfoMapper.fromDto(faculty)
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
                            .map(facultyResponseMapper::toDto)
                            .toList()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "filterByColor")
    public ResponseEntity<?> filterByColor(@RequestParam String color) {
        try {
            return ResponseEntity.ok(
                    service.findAll(FacultySpecification.colorLike(color))
                            .stream()
                            .map(facultyResponseMapper::toDto)
                            .toList()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "{id}/students")
    public ResponseEntity<?> Students(@PathVariable("id") UUID id) {
        try {
            return ResponseEntity.ok(
                    service.findOne(FacultySpecification.idEqual(id)).getStudents()
                            .stream()
                            .map(studentResponseMapper::toDto)
                            .toList()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "findByNameOrColor")
    public ResponseEntity<?> findByNameOrColor(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String color
    ) {
        try {
            return ResponseEntity.ok(
                    service.findAll(FacultySpecification.findByNameOrColor(name, color))
                            .stream()
                            .map(facultyResponseMapper::toDto)
                            .toList()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}

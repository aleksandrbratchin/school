package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.dto.faculty.FacultyAddRequestDto;
import ru.hogwarts.school.dto.faculty.FacultyInfoDto;
import ru.hogwarts.school.services.api.FacultyService;

import java.util.UUID;

@RestController
@RequestMapping("faculty")
public class FacultyController {

    private final FacultyService service;

    public FacultyController(
            @Qualifier("facultyServiceImpl") FacultyService service
    ) {
        this.service = service;
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(
            @PathVariable("id") UUID id
    ) {
        try {
            return ResponseEntity.ok(
                    service.deleteDto(id)
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
                    service.createFacultyAdd(faculty)
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
                    service.updateFacultyInfo(faculty)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "All")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(
                    service.findAllDto()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "filterByColor")
    public ResponseEntity<?> filterByColor(@RequestParam String color) {
        try {
            return ResponseEntity.ok(
                    service.filterByColor(color)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "{id}/students")
    public ResponseEntity<?> getStudentsById(@PathVariable("id") UUID id) {
        try {
            return ResponseEntity.ok(
                    service.getStudentsById(id)
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
                    service.findByNameOrColor(name, color)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}

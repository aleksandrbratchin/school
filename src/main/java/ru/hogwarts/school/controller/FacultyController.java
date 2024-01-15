package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.faculty.Faculty;
import ru.hogwarts.school.specifications.FacultySpecification;
import ru.hogwarts.school.services.impl.FacultyServiceImpl;

import java.util.UUID;

@RestController
@RequestMapping("faculty")
public class FacultyController {

    private final FacultyServiceImpl service;

    public FacultyController(@Qualifier("facultyServiceImpl") FacultyServiceImpl service) {
        this.service = service;
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(
            @PathVariable("id") UUID id
    ) {
        try {
            return ResponseEntity.ok(service.delete(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping()
    public ResponseEntity<?> add(
            @RequestBody Faculty faculty
    ) {
        try {
            return ResponseEntity.ok(service.create(faculty));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping()
    public ResponseEntity<?> update(
            @RequestBody Faculty faculty
    ) {
        try {
            return ResponseEntity.ok(service.update(faculty));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "All")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(service.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "filterByColor")
    public ResponseEntity<?> filterByColor(@RequestParam String color) {
        try {
            return ResponseEntity.ok(service.findAll(FacultySpecification.colorLike(color)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

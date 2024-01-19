package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.student.Student;
import ru.hogwarts.school.services.impl.StudentServiceImpl;
import ru.hogwarts.school.specifications.StudentSpecification;

import java.util.UUID;

@RestController
@RequestMapping("student")
public class StudentController {
    private final StudentServiceImpl service;

    public StudentController(@Qualifier("studentServiceImpl") StudentServiceImpl service) {
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
            @RequestBody Student student
    ) {
        try {
            return ResponseEntity.ok(service.create(student));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping()
    public ResponseEntity<?> update(
            @RequestBody Student student
    ) {
        try {
            return ResponseEntity.ok(service.update(student));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "All")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(service.findAll());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "filterByAge")
    public ResponseEntity<?> filterByAge(@RequestParam Integer age) {
        try {
            return ResponseEntity.ok(service.findAll(StudentSpecification.ageEqual(age)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "{id}/faculty")
    public ResponseEntity<?> Faculty(@PathVariable("id") UUID id) {
        try {
            return ResponseEntity.ok(service.findOne(StudentSpecification.idEqual(id)).getFaculty());
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
            return ResponseEntity.ok(service.findAll(StudentSpecification.ageInBetween(min, max)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}

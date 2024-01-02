package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.student.Student;
import ru.hogwarts.school.service.impl.StudentServiceImpl;
import ru.hogwarts.school.specification.StudentEqualsAgeSpecification;

@RestController
@RequestMapping("student")
public class StudentController {
    private final StudentServiceImpl service;

    public StudentController(@Qualifier("studentServiceImpl") StudentServiceImpl service) {
        this.service = service;
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(
            @PathVariable("id") Long id
    ) {
        return new ResponseEntity<>(service.delete(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> add(
            @RequestBody Student student
    ) {
        return new ResponseEntity<>(service.create(student), HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<?> update(
            @RequestBody Student student
    ) {
        return new ResponseEntity<>(service.update(student), HttpStatus.OK);
    }

    @GetMapping(path = "All")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "filterByAge")
    public ResponseEntity<?> filterByAge(@RequestParam Integer age) {
        return new ResponseEntity<>(service.findAll(new StudentEqualsAgeSpecification(age)), HttpStatus.OK);
    }
}

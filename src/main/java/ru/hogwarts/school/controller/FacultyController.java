package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.faculty.Faculty;
import ru.hogwarts.school.service.impl.FacultyServiceImpl;
import ru.hogwarts.school.specification.FacultyContainsColorSpecification;

@RestController
@RequestMapping("faculty")
public class FacultyController {

    private final FacultyServiceImpl service;

    public FacultyController(@Qualifier("facultyServiceImpl") FacultyServiceImpl service) {
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
            @RequestBody Faculty faculty
    ) {
        return new ResponseEntity<>(service.create(faculty), HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<?> update(
            @RequestBody Faculty faculty
    ) {
        return new ResponseEntity<>(service.update(faculty), HttpStatus.OK);
    }

    @GetMapping(path = "All")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "filterByColor")
    public ResponseEntity<?> filterByColor(@RequestParam String color) {
        return new ResponseEntity<>(service.findAll(new FacultyContainsColorSpecification(color)), HttpStatus.OK);
    }
}

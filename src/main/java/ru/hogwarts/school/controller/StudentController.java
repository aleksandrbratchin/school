package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.dto.student.StudentAddRequestDto;
import ru.hogwarts.school.dto.student.StudentResponseDto;
import ru.hogwarts.school.dto.student.StudentUpdateRequestDto;
import ru.hogwarts.school.model.student.Student;
import ru.hogwarts.school.services.api.StudentService;
import ru.hogwarts.school.services.impl.StudentServiceImpl;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@RestController
@RequestMapping("student")
public class StudentController {
    private final StudentService service;

    public StudentController(
            @Qualifier("studentServiceImpl") StudentServiceImpl service) {
        this.service = service;
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(
            @PathVariable("id") UUID id
    ) {
        try {
            return ResponseEntity.ok(
                    service.deleteById(id)
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
                    service.create(student)
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
                    service.update(student)
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
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "filterByAge")
    public ResponseEntity<?> filterByAge(@RequestParam Integer age) {
        try {
            return ResponseEntity.ok(
                    service.filterByAge(age)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "{id}/faculty")
    public ResponseEntity<?> getFacultyById(@PathVariable("id") UUID id) {
        try {
            return ResponseEntity.ok(
                    service.getFacultyById(id)
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
                    service.findByAgeBetween(min, max)
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
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "nameStartsWithLetterA")
    public ResponseEntity<?> nameStartsWithLetterA() {
        try {
            return ResponseEntity.ok(
                    service.nameStartsWithLetterA()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "getAverageAgeWithStream")
    public ResponseEntity<?> getAverageAgeWithStream() {
        try {
            return ResponseEntity.ok(
                    service.getAverageAgeWithStream()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "print-parallel")
    public ResponseEntity<?> printParallel() {
        try {
            PageRequest pageRequest = PageRequest.of(0, 6);
            List<StudentResponseDto> sixStudent = service.findAllDto(pageRequest);
            printStudent(sixStudent.get(0));
            printStudent(sixStudent.get(1));
            Thread thread1 = new Thread(
                    () -> {
                        printStudent(sixStudent.get(2));
                        printStudent(sixStudent.get(3));
                    }
            );
            Thread thread2 = new Thread(
                    () -> {
                        printStudent(sixStudent.get(4));
                        printStudent(sixStudent.get(5));
                    }
            );
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
            return ResponseEntity.ok(sixStudent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private void printStudent(StudentResponseDto student) {
        System.out.println(Thread.currentThread().getName() + ": " + student);
    }

    @GetMapping(path = "print-synchronized")
    public ResponseEntity<?> printSynchronized() {
        try {
            PageRequest pageRequest = PageRequest.of(0, 6);
            List<StudentResponseDto> sixStudent = service.findAllDto(pageRequest);
            BlockingQueue<StudentResponseDto> studentQueue = new LinkedBlockingQueue<>(sixStudent);
            printStudentSynchronized(studentQueue);
            printStudentSynchronized(studentQueue);
            Thread thread1 = new Thread(
                    () -> {
                        printStudentSynchronized(studentQueue);
                        printStudentSynchronized(studentQueue);
                    }
            );
            Thread thread2 = new Thread(
                    () -> {
                        printStudentSynchronized(studentQueue);
                        printStudentSynchronized(studentQueue);
                    }
            );
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
            return ResponseEntity.ok(sixStudent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private synchronized void printStudentSynchronized(BlockingQueue<StudentResponseDto> studentQueue) {
        System.out.println(Thread.currentThread().getName() + ": " + studentQueue.poll());
    }

}

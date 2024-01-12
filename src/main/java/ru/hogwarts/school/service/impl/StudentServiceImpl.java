package ru.hogwarts.school.service.impl;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.MyIllegalArgumentException;
import ru.hogwarts.school.exception.NotFoundElementException;
import ru.hogwarts.school.model.student.Student;
import ru.hogwarts.school.service.api.StudentService;
import ru.hogwarts.school.specification.MySpecification;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final Map<Long, Student> studentMap = new HashMap<>();
    private long id = 0;
    private final String NULL_ERROR_MESSAGE = "Студент не может быть null!";

    @Override
    public Student create(Student student) {
        Optional.ofNullable(student).orElseThrow(() -> new NullPointerException(NULL_ERROR_MESSAGE));
        if (
                studentMap.values().stream().anyMatch(
                        student1 -> student1.getName().equals(student.getName()) &&
                                student1.getAge() == student.getAge()
                )
        ) {
            throw new MyIllegalArgumentException("Студент '" + student.getName() + "' уже добавлен!");
        }
        student.setId(id++);
        studentMap.put(student.getId(), student);
        return student;
    }

    @Override
    public Student update(Student student) {
        Optional.ofNullable(student).orElseThrow(() -> new NullPointerException(NULL_ERROR_MESSAGE));
        if (
                studentMap.values().stream().anyMatch(
                        student1 -> student1.getName().equals(student.getName()) &&
                                student1.getAge() == student.getAge() &&
                                !student1.getId().equals(student.getId())
                )
        ) {
            throw new MyIllegalArgumentException("Студент '" + student.getName() + "' уже добавлен!");
        }
        Student updated = studentMap.replace(student.getId(), student);
        return Optional.ofNullable(updated)
                .orElseThrow(
                        () -> new NotFoundElementException(
                                "Ошибка при попытке изменения студента! " +
                                        "Студент с id = '" + student.getId() + "' не найден."
                        )
                );
    }

    @Override
    public Student delete(Long id) {
        Optional.ofNullable(id).orElseThrow(() -> new NullPointerException("ID не должен быть null!"));
        Student removed = studentMap.remove(id);
        return Optional.ofNullable(removed)
                .orElseThrow(
                        () -> new NotFoundElementException(
                                "Ошибка при попытке удаления студента! " +
                                        "Студент с id = '" + id + "' не найден."
                        )
                );
    }

    @Override
    public Map<Long, Student> findAll(MySpecification<Student> specification) {
        Optional.ofNullable(specification).orElseThrow(NullPointerException::new);
        return studentMap.entrySet()
                .stream()
                .filter(entry -> specification.test(entry.getValue()))
                .collect(
                        Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
                );
    }

    @Override
    public Map<Long, Student> findAll() {
        return studentMap;
    }

    @Override
    public Student findOne(MySpecification<Student> specification) {
        Optional.ofNullable(specification).orElseThrow(NullPointerException::new);
        return studentMap
                .values()
                .stream()
                .filter(specification)
                .findFirst()
                .orElseThrow(
                        NotFoundElementException::new
                );
    }

}

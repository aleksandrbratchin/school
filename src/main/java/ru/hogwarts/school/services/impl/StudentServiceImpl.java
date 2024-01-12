package ru.hogwarts.school.services.impl;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.student.Student;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.services.api.StudentService;
import ru.hogwarts.school.specifications.StudentSpecification;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.repository = studentRepository;
    }

    @Override
    public Student create(Student student) {
        Optional.ofNullable(student).orElseThrow(IllegalArgumentException::new);
        student.setId(null);
        return repository.save(student);
    }

    @Override
    public Student update(Student student) {
        Optional.ofNullable(student).orElseThrow(IllegalArgumentException::new);
        UUID id = student.getId();
        if (!repository.existsById(id)) {
            throw new NoSuchElementException(
                    "Ошибка при попытке изменения студента! " +
                            "Студент с id = '" + id + "' не найден."
            );
        }
        return repository.save(student);
    }

    @Override
    public Student delete(UUID id) {
        Optional.ofNullable(id).orElseThrow(IllegalArgumentException::new);
        Student old = repository.findOne(StudentSpecification.idEqual(id))
                .orElseThrow(() -> new NoSuchElementException(
                                "Ошибка при попытке удаления студента! " +
                                        "Студент с id = '" + id + "' не найден."
                        )
                );
        repository.deleteById(id);
        return old;
    }

    @Override
    public List<Student> findAll(Specification<Student> specification) {
        Optional.ofNullable(specification).orElseThrow(IllegalArgumentException::new);
        return repository.findAll(specification);
    }


    @Override
    public List<Student> findAll() {
        return repository.findAll();
    }

    @Override
    public Student findOne(Specification<Student> specification) {
        Optional.ofNullable(specification).orElseThrow(IllegalArgumentException::new);
        return repository.findOne(specification).orElseThrow(NoSuchElementException::new);
    }


}

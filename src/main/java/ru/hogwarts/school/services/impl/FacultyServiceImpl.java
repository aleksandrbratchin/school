package ru.hogwarts.school.services.impl;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.model.faculty.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.services.api.FacultyService;
import ru.hogwarts.school.specifications.FacultySpecification;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository repository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.repository = facultyRepository;
    }

    @Override
    public Faculty create(Faculty faculty) {
        Optional.ofNullable(faculty).orElseThrow(IllegalArgumentException::new);
        String name = faculty.getName();
        if (repository.exists(FacultySpecification.nameEqual(name))) {
            throw new IllegalArgumentException("Факультет '" + name + "' уже добавлен!");
        }
        faculty.setId(null);
        return repository.save(faculty);
    }

    @Override
    public Faculty update(Faculty faculty) {
        Optional.ofNullable(faculty).orElseThrow(IllegalArgumentException::new);
        UUID id = faculty.getId();
        Faculty old = repository.findById(faculty.getId())
                .orElseThrow(
                        () -> new NoSuchElementException(
                                "Ошибка при попытке изменения факультета! " +
                                        "Факультет с id = '" + id + "' не найден.")
                );
        if (!old.getName().equals(faculty.getName()) &&
                repository.exists(FacultySpecification.nameEqual(faculty.getName()))) {
            throw new IllegalArgumentException(
                    "Ошибка при попытке изменения факультета! " +
                            "Факультет с именем = '" + faculty.getName() + "' уже добавлен."
            );
        }
        faculty = repository.save(faculty);
        return faculty;
    }

    @Override
    public Faculty delete(UUID id) {
        Optional.ofNullable(id).orElseThrow(IllegalArgumentException::new);
        Faculty old = repository.findOne(FacultySpecification.idEqual(id))
                .orElseThrow(() -> new NoSuchElementException(
                                "Ошибка при попытке удаления факультета! " +
                                        "Факультет с id = '" + id + "' не найден."
                        )
                );
        repository.deleteById(id);
        return old;
    }

    @Override
    public List<Faculty> findAll(Specification<Faculty> specification) {
        Optional.ofNullable(specification).orElseThrow(IllegalArgumentException::new);
        return repository.findAll(specification);
    }

    @Override
    public List<Faculty> findAll() {
        return repository.findAll();
    }

    @Override
    public Faculty findOne(Specification<Faculty> specification) {
        Optional.ofNullable(specification).orElseThrow(IllegalArgumentException::new);
        return repository.findOne(specification).orElseThrow(NoSuchElementException::new);
    }

}

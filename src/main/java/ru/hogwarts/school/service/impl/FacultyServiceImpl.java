package ru.hogwarts.school.service.impl;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.MyIllegalArgumentException;
import ru.hogwarts.school.exception.NotFoundElementException;
import ru.hogwarts.school.model.faculty.Faculty;
import ru.hogwarts.school.service.api.FacultyService;
import ru.hogwarts.school.specification.MySpecification;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final Map<Long, Faculty> facultyMap = new HashMap<>();
    private long id = 0;
    private final String NULL_ERROR_MESSAGE = "Факультет не должен быть null!";

    @Override
    public Faculty create(Faculty faculty) {
        Optional.ofNullable(faculty).orElseThrow(() -> new NullPointerException(NULL_ERROR_MESSAGE));
        if (
                facultyMap.values().stream().anyMatch(
                        faculty1 -> faculty1.getName().equals(faculty.getName())
                )
        ) {
            throw new MyIllegalArgumentException("Факультет '" + faculty.getName() + "' уже добавлен!");
        }
        faculty.setId(id++);
        facultyMap.put(faculty.getId(), faculty);
        return faculty;
    }

    @Override
    public Faculty update(Faculty faculty) {
        Optional.ofNullable(faculty).orElseThrow(() -> new NullPointerException(NULL_ERROR_MESSAGE));
        if (
                facultyMap.values().stream().anyMatch(
                        faculty1 -> faculty1.getName().equals(faculty.getName()) &&
                                !faculty1.getId().equals(faculty.getId())
                )
        ) {
            throw new MyIllegalArgumentException("Факультет '" + faculty.getName() + "' уже добавлен!");
        }
        Faculty updated = facultyMap.replace(faculty.getId(), faculty);
        return Optional.ofNullable(updated)
                .orElseThrow(
                        () -> new NotFoundElementException(
                                "Ошибка при попытке изменения факультета! " +
                                        "Факультет с id = '" + faculty.getId() + "' не найден."
                        )
                );
    }

    @Override
    public Faculty delete(Long id) {
        Optional.ofNullable(id).orElseThrow(() -> new NullPointerException("ID не должен быть null!"));
        Faculty removed = facultyMap.remove(id);
        return Optional.ofNullable(removed)
                .orElseThrow(
                        () -> new NotFoundElementException(
                                "Ошибка при попытке удаления факультета! " +
                                        "Факультет с id = '" + id + "' не найден."
                        )
                );
    }

    @Override
    public Map<Long, Faculty> findAll(MySpecification<Faculty> specification) {
        Optional.ofNullable(specification).orElseThrow(NullPointerException::new);
        return facultyMap.entrySet()
                .stream()
                .filter(entry -> specification.test(entry.getValue()))
                .collect(
                        Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
                );
    }

    @Override
    public Map<Long, Faculty> findAll() {
        return facultyMap;
    }

    @Override
    public Faculty findOne(MySpecification<Faculty> specification) {
        Optional.ofNullable(specification).orElseThrow(NullPointerException::new);
        return facultyMap
                .values()
                .stream()
                .filter(specification)
                .findFirst()
                .orElseThrow(
                        NotFoundElementException::new
                );
    }
}

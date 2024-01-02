package ru.hogwarts.school.specification;


import ru.hogwarts.school.model.faculty.Faculty;

import java.util.Optional;

public class FacultyContainsColorSpecification extends MySpecification<Faculty> {
    private String color;

    public FacultyContainsColorSpecification(String color) {
        this.color = color;
    }

    @Override
    public boolean test(Faculty faculty) {
        return Optional.ofNullable(faculty.getColor())
                .orElseThrow(
                        () -> new NullPointerException("У факультета " + faculty.getName() + " не указан цвет!")
                ).contains(color);
    }
}
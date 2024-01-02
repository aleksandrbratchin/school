package ru.hogwarts.school.specification;



import ru.hogwarts.school.model.student.Student;

import java.util.Optional;

public class StudentEqualsAgeSpecification extends MySpecification<Student> {
    private Integer age;

    public StudentEqualsAgeSpecification(Integer age) {
        this.age = age;
    }

    @Override
    public boolean test(Student student) {
        return Optional.ofNullable(student.getAge())
                .orElseThrow(
                        () -> new NullPointerException("У студента " + student.getName() + " не указан возраст!")
                ).equals(age);
    }
}
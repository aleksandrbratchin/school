package ru.hogwarts.school.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.hogwarts.school.model.student.Student;

import java.util.UUID;

public class StudentSpecification {

    public static Specification<Student> ageEqual(Integer age) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("age"), age);
    }

    public static Specification<Student> nameEqual(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("name"), name);
    }

    public static Specification<Student> idEqual(UUID id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id);
    }

}

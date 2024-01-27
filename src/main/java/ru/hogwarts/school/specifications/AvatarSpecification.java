package ru.hogwarts.school.specifications;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import ru.hogwarts.school.model.avatar.Avatar;
import ru.hogwarts.school.model.student.Student;

import java.util.UUID;

public class AvatarSpecification {

    public static Specification<Avatar> idEqual(UUID id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<Avatar> findByIdStudent(UUID idStudent) {
        return (root, query, criteriaBuilder) -> {
            Join<Avatar, Student> student = root.join("student");
            return criteriaBuilder.equal(student.get("id"), idStudent);
        };
    }

}

package ru.hogwarts.school.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.hogwarts.school.model.avatar.Avatar;

import java.util.UUID;

public class AvatarSpecification {

    public static Specification<Avatar> idEqual(UUID id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<Avatar> findByIdStudent(UUID idStudent) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("student_id"), idStudent);
    }

/*    public static Specification<Avatar> findByIdStudent(UUID idStudent) {
        return (root, query, criteriaBuilder) -> {
            Join<Avatar, Student> student = root.join("student_id");
            return criteriaBuilder.equal(student.get("id"), idStudent);
        };
    }*/

}

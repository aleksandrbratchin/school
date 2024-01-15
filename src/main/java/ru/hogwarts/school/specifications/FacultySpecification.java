package ru.hogwarts.school.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.hogwarts.school.model.faculty.Faculty;

import java.util.UUID;

public class FacultySpecification {

    public static Specification<Faculty> nameEqual(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("name"), name);
    }

    public static Specification<Faculty> nameEqualIgnoreCase(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("name")),
                        criteriaBuilder.lower(criteriaBuilder.literal(name))
                );
    }

    public static Specification<Faculty> colorLike(String color) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("color"), "%" + color + "%");
    }

    public static Specification<Faculty> colorLikeIgnoreCase(String color) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("color")),
                        criteriaBuilder.lower(criteriaBuilder.literal("%" + color + "%")));
    }

    public static Specification<Faculty> idEqual(UUID id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<Faculty> findByNameOrColor(String name, String color) {
        return nameEqualIgnoreCase(name)
                .or(colorLikeIgnoreCase(color));
    }

}

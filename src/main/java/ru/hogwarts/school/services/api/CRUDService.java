package ru.hogwarts.school.services.api;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CRUDService<K, E> {
    E create(E e);

    E update(E e);

    E delete(K e);

    List<E> findAll(Specification<E> specification);

    List<E> findAll();

    E findOne(Specification<E> specification);
}

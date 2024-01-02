package ru.hogwarts.school.service.api;

import ru.hogwarts.school.specification.MySpecification;

import java.util.Map;

public interface CRUDService<K, E> {
    E create(E e);

    E update(E e);

    E delete(K e);

    Map<K, E> findAll(MySpecification<E> specification);

    Map<K, E> findAll();

    E findOne(MySpecification<E> specification);
}

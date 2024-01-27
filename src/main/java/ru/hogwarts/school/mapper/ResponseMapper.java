package ru.hogwarts.school.mapper;

public interface ResponseMapper<R, C> {
    C toDto(R dto);
}

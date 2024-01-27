package ru.hogwarts.school.mapper;

public interface RequestMapper<R, C> {
    R fromDto(C dto);
}

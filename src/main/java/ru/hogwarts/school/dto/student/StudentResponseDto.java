package ru.hogwarts.school.dto.student;

import ru.hogwarts.school.dto.faculty.FacultyInfoDto;

import java.util.UUID;

public record StudentResponseDto (
        UUID id,
        String name,
        Integer age,
        FacultyInfoDto faculty
) {}

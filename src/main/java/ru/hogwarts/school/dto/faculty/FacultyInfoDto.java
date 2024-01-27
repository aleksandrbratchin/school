package ru.hogwarts.school.dto.faculty;

import java.util.UUID;

public record FacultyInfoDto (
        UUID id,
        String name,
        String color
){}

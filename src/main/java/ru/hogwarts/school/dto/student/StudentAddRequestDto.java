package ru.hogwarts.school.dto.student;

import java.util.UUID;

public record StudentAddRequestDto (
        String name,
        Integer age,
        UUID facultyId
){}

package ru.hogwarts.school.dto.student;

import java.util.UUID;

public record StudentUpdateRequestDto (
        UUID id,
        String name,
        int age,
        UUID facultyId
){}

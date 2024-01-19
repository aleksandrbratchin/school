package ru.hogwarts.school.dto.faculty;

import ru.hogwarts.school.dto.student.StudentResponseDto;

import java.util.List;
import java.util.UUID;

public record FacultyResponseDto (
        UUID id,
        String name,
        String color,
        List<StudentResponseDto> student
){}

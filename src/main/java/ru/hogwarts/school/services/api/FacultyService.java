package ru.hogwarts.school.services.api;

import ru.hogwarts.school.dto.faculty.FacultyAddRequestDto;
import ru.hogwarts.school.dto.faculty.FacultyInfoDto;
import ru.hogwarts.school.dto.faculty.FacultyResponseDto;
import ru.hogwarts.school.dto.student.StudentResponseDto;
import ru.hogwarts.school.model.faculty.Faculty;

import java.util.List;
import java.util.UUID;

public interface FacultyService extends CRUDService<UUID, Faculty> {

    FacultyInfoDto create(FacultyAddRequestDto facultyDto);

    FacultyInfoDto update(FacultyInfoDto facultyDto);

    FacultyInfoDto deleteById(UUID id);

    List<FacultyResponseDto> findAllDto();

    List<FacultyResponseDto> filterByColor(String color);

    List<StudentResponseDto> getStudentsById(UUID id);

    List<FacultyResponseDto> findByNameOrColor(String name, String color);

    String longestFacultyName();
}

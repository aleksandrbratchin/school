package ru.hogwarts.school.mapper.faculty;

import org.springframework.stereotype.Component;
import ru.hogwarts.school.dto.faculty.FacultyInfoDto;
import ru.hogwarts.school.mapper.Mapper;
import ru.hogwarts.school.mapper.RequestMapper;
import ru.hogwarts.school.model.faculty.Faculty;
import ru.hogwarts.school.services.impl.FacultyServiceImpl;
import ru.hogwarts.school.specifications.FacultySpecification;

@Component
public class FacultyInfoMapper implements Mapper<Faculty, FacultyInfoDto> {

    private final FacultyServiceImpl facultyService;

    public FacultyInfoMapper(FacultyServiceImpl facultyService) {
        this.facultyService = facultyService;
    }

    @Override
    public Faculty fromDto(FacultyInfoDto dto) {
        Faculty faculty = facultyService.findOne(FacultySpecification.idEqual(dto.id()));
        faculty.setName(dto.name());
        faculty.setColor(dto.color());
        return faculty;
    }

    @Override
    public FacultyInfoDto toDto(Faculty dto) {
        return new FacultyInfoDto(
                dto.getId(),
                dto.getName(),
                dto.getColor()
        );
    }

}

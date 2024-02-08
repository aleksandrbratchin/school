package ru.hogwarts.school.services.impl;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.dto.faculty.FacultyAddRequestDto;
import ru.hogwarts.school.dto.faculty.FacultyInfoDto;
import ru.hogwarts.school.dto.faculty.FacultyResponseDto;
import ru.hogwarts.school.dto.student.StudentResponseDto;
import ru.hogwarts.school.mapper.Mapper;
import ru.hogwarts.school.mapper.RequestMapper;
import ru.hogwarts.school.mapper.ResponseMapper;
import ru.hogwarts.school.model.faculty.Faculty;
import ru.hogwarts.school.model.student.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.services.api.FacultyService;
import ru.hogwarts.school.specifications.FacultySpecification;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository repository;
    private final ResponseMapper<Faculty, FacultyInfoDto> facultyInfoMapper;
    private final RequestMapper<Faculty, FacultyAddRequestDto> facultyAddRequestMapper;
    private final ResponseMapper<Faculty, FacultyResponseDto> facultyResponseMapper;
    private final ResponseMapper<Student, StudentResponseDto> studentResponseMapper;

    public FacultyServiceImpl(
            FacultyRepository facultyRepository,
            ResponseMapper<Faculty, FacultyInfoDto> facultyInfoMapper,
            RequestMapper<Faculty, FacultyAddRequestDto> facultyAddRequestMapper,
            ResponseMapper<Faculty, FacultyResponseDto> facultyResponseMapper,
            ResponseMapper<Student, StudentResponseDto> studentResponseMapper
    ) {
        this.repository = facultyRepository;
        this.facultyInfoMapper = facultyInfoMapper;
        this.facultyAddRequestMapper = facultyAddRequestMapper;
        this.facultyResponseMapper = facultyResponseMapper;
        this.studentResponseMapper = studentResponseMapper;
    }

    @Override
    public FacultyInfoDto create(FacultyAddRequestDto facultyDto) {
        Optional.ofNullable(facultyDto).orElseThrow(IllegalArgumentException::new);
        Faculty faculty = facultyAddRequestMapper.fromDto(facultyDto);
        return facultyInfoMapper.toDto(create(faculty));
    }

    @Override
    public Faculty create(Faculty faculty) {
        Optional.ofNullable(faculty).orElseThrow(IllegalArgumentException::new);
        String name = faculty.getName();
        if (repository.exists(FacultySpecification.nameEqual(name))) {
            throw new IllegalArgumentException("Факультет '" + name + "' уже добавлен!");
        }
        faculty.setId(null);
        return repository.save(faculty);
    }

    @Override
    public Faculty update(Faculty faculty) {
        Optional.ofNullable(faculty).orElseThrow(IllegalArgumentException::new);
        UUID id = faculty.getId();
        Faculty old = repository.findById(faculty.getId())
                .orElseThrow(
                        () -> new NoSuchElementException(
                                "Ошибка при попытке изменения факультета! " +
                                        "Факультет с id = '" + id + "' не найден.")
                );
        if (!old.getName().equals(faculty.getName()) &&
                repository.exists(FacultySpecification.nameEqual(faculty.getName()))) {
            throw new IllegalArgumentException(
                    "Ошибка при попытке изменения факультета! " +
                            "Факультет с именем = '" + faculty.getName() + "' уже добавлен."
            );
        }
        faculty = repository.save(faculty);
        return faculty;
    }

    @Override
    public FacultyInfoDto update(FacultyInfoDto facultyDto) {
        Optional.ofNullable(facultyDto).orElseThrow(IllegalArgumentException::new);
        Faculty faculty = findOne(FacultySpecification.idEqual(facultyDto.id()));
        faculty.setName(facultyDto.name());
        faculty.setColor(facultyDto.color());
        return facultyInfoMapper.toDto(update(faculty));
    }

    @Override
    public Faculty delete(UUID id) {
        Optional.ofNullable(id).orElseThrow(IllegalArgumentException::new);
        Faculty old = repository.findOne(FacultySpecification.idEqual(id))
                .orElseThrow(() -> new NoSuchElementException(
                                "Ошибка при попытке удаления факультета! " +
                                        "Факультет с id = '" + id + "' не найден."
                        )
                );
        repository.deleteById(id);
        return old;
    }

    @Override
    public FacultyInfoDto deleteById(UUID id) {
        return facultyInfoMapper.toDto(delete(id));
    }

    @Override
    public List<Faculty> findAll(Specification<Faculty> specification) {
        Optional.ofNullable(specification).orElseThrow(IllegalArgumentException::new);
        return repository.findAll(specification);
    }

    @Override
    public List<Faculty> findAll() {
        return repository.findAll();
    }

    @Override
    public List<FacultyResponseDto> findAllDto() {
        return repository.findAll().stream()
                .map(facultyResponseMapper::toDto)
                .toList();
    }

    @Override
    public List<FacultyResponseDto> filterByColor(String color) {
        return repository.findAll(FacultySpecification.colorLike(color))
                .stream()
                .map(facultyResponseMapper::toDto)
                .toList();
    }

    @Override
    public List<StudentResponseDto> getStudentsById(UUID id) {
        return findOne(FacultySpecification.idEqual(id)).getStudents()
                .stream()
                .map(studentResponseMapper::toDto)
                .toList();
    }

    @Override
    public List<FacultyResponseDto> findByNameOrColor(String name, String color) {
        return findAll(FacultySpecification.findByNameOrColor(name, color))
                .stream()
                .map(facultyResponseMapper::toDto)
                .toList();
    }

    @Override
    public Faculty findOne(Specification<Faculty> specification) {
        Optional.ofNullable(specification).orElseThrow(IllegalArgumentException::new);
        return repository.findOne(specification).orElseThrow(NoSuchElementException::new);
    }

}

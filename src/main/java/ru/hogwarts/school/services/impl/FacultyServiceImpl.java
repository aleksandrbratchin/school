package ru.hogwarts.school.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.dto.faculty.FacultyAddRequestDto;
import ru.hogwarts.school.dto.faculty.FacultyInfoDto;
import ru.hogwarts.school.dto.faculty.FacultyResponseDto;
import ru.hogwarts.school.dto.student.StudentResponseDto;
import ru.hogwarts.school.mapper.RequestMapper;
import ru.hogwarts.school.mapper.ResponseMapper;
import ru.hogwarts.school.model.faculty.Faculty;
import ru.hogwarts.school.model.student.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.services.api.FacultyService;
import ru.hogwarts.school.specifications.FacultySpecification;

import java.util.*;

@Service
@Transactional
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository repository;
    private final ResponseMapper<Faculty, FacultyInfoDto> facultyInfoMapper;
    private final RequestMapper<Faculty, FacultyAddRequestDto> facultyAddRequestMapper;
    private final ResponseMapper<Faculty, FacultyResponseDto> facultyResponseMapper;
    private final ResponseMapper<Student, StudentResponseDto> studentResponseMapper;
    private Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

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
        logger.info("Был вызван метод сохранения факультета из dto");
        Optional.ofNullable(facultyDto).orElseThrow(
                () -> {
                    String msq = "Ошибка в методе сохранения факультета из dto. facultyDto = null";
                    logger.error(msq);
                    return new IllegalArgumentException(msq);
                }
        );
        Faculty faculty = facultyAddRequestMapper.fromDto(facultyDto);
        return facultyInfoMapper.toDto(create(faculty));
    }

    @Override
    public Faculty create(Faculty faculty) {
        logger.info("Был вызван метод сохранения факультета");
        Optional.ofNullable(faculty).orElseThrow(
                () -> {
                    String msq = "Ошибка в методе сохранения факультета. faculty = null";
                    logger.error(msq);
                    return new IllegalArgumentException(msq);
                }
        );
        String name = faculty.getName();
        if (repository.exists(FacultySpecification.nameEqual(name))) {
            String msq = "Факультет '" + name + "' уже добавлен!";
            logger.error(msq);
            throw new IllegalArgumentException(msq);
        }
        faculty.setId(null);
        return repository.save(faculty);
    }

    @Override
    public Faculty update(Faculty faculty) {
        logger.info("Был вызван метод изменения факультета");
        Optional.ofNullable(faculty).orElseThrow(
                () -> {
                    String msq = "Ошибка в методе изменения факультета. faculty = null";
                    logger.error(msq);
                    return new IllegalArgumentException(msq);
                }
        );
        UUID id = faculty.getId();
        Faculty old = repository.findById(faculty.getId())
                .orElseThrow(
                        () -> {
                            String msq = "Ошибка при попытке изменения факультета! " +
                                    "Факультет с id = '" + id + "' не найден.";
                            logger.error(msq);
                            return new NoSuchElementException(msq);
                        }
                );
        if (!old.getName().equals(faculty.getName()) &&
                repository.exists(FacultySpecification.nameEqual(faculty.getName()))) {
            String msq = "Ошибка при попытке изменения факультета! " +
                    "Факультет с именем = '" + faculty.getName() + "' уже добавлен.";
            logger.error(msq);
            throw new IllegalArgumentException(msq);
        }
        faculty = repository.save(faculty);
        return faculty;
    }

    @Override
    public FacultyInfoDto update(FacultyInfoDto facultyDto) {
        logger.info("Был вызван метод изменения факультета из dto");
        Optional.ofNullable(facultyDto).orElseThrow(
                () -> {
                    String msq = "Ошибка в методе изменения факультета из dto. facultyDto = null";
                    logger.error(msq);
                    return new IllegalArgumentException(msq);
                }
        );
        Faculty faculty = findOne(FacultySpecification.idEqual(facultyDto.id()));
        faculty.setName(facultyDto.name());
        faculty.setColor(facultyDto.color());
        return facultyInfoMapper.toDto(update(faculty));
    }

    @Override
    public Faculty delete(UUID id) {
        logger.info("Был вызван метод удаления факультета по id");
        Optional.ofNullable(id).orElseThrow(
                () -> {
                    String msq = "Ошибка в методе удаления факультета по id. id = null";
                    logger.error(msq);
                    return new IllegalArgumentException(msq);
                }
        );
        Faculty old = repository.findOne(FacultySpecification.idEqual(id))
                .orElseThrow(
                        () -> {
                            String msq = "Ошибка при попытке удаления факультета! " +
                                    "Факультет с id = '" + id + "' не найден.";
                            logger.error(msq);
                            return new NoSuchElementException(msq);
                        }
                );
        repository.deleteById(id);
        return old;
    }

    @Override
    public FacultyInfoDto deleteById(UUID id) {
        logger.info("Был вызван метод удаления факультета по id");
        Optional.ofNullable(id).orElseThrow(
                () -> {
                    String msq = "Ошибка в методе удаления факультета по id. id = null";
                    logger.error(msq);
                    return new IllegalArgumentException(msq);
                }
        );
        return facultyInfoMapper.toDto(delete(id));
    }

    @Override
    public List<Faculty> findAll(Specification<Faculty> specification) {
        logger.info("Был вызван метод фильтрации факультетов по спецификации");
        Optional.ofNullable(specification).orElseThrow(
                () -> {
                    String msq = "Ошибка в методе фильтрации факультетов по спецификации. specification = null";
                    logger.error(msq);
                    return new IllegalArgumentException(msq);
                }
        );
        return repository.findAll(specification);
    }

    @Override
    public List<Faculty> findAll() {
        logger.info("Был вызван метод получения всех факультетов");
        return repository.findAll();
    }

    @Override
    public List<FacultyResponseDto> findAllDto() {
        logger.info("Был вызван метод получения всех факультетов");
        return repository.findAll().stream()
                .map(facultyResponseMapper::toDto)
                .toList();
    }

    @Override
    public List<FacultyResponseDto> filterByColor(String color) {
        logger.info("Был вызван метод фильтрации факультетов по цвету");
        return repository.findAll(FacultySpecification.colorLike(color))
                .stream()
                .map(facultyResponseMapper::toDto)
                .toList();
    }

    @Override
    public List<StudentResponseDto> getStudentsById(UUID id) {
        logger.info("Был вызван метод получения студентов по id факультета");
        Optional.ofNullable(id).orElseThrow(
                () -> {
                    String msq = "Ошибка в методе получения студентов по id факультета. id = null";
                    logger.error(msq);
                    return new IllegalArgumentException(msq);
                }
        );
        return findOne(FacultySpecification.idEqual(id)).getStudents()
                .stream()
                .map(studentResponseMapper::toDto)
                .toList();
    }

    @Override
    public List<FacultyResponseDto> findByNameOrColor(String name, String color) {
        logger.info("Был вызван метод поиска факультета по имени и цвету");
        return findAll(FacultySpecification.findByNameOrColor(name, color))
                .stream()
                .map(facultyResponseMapper::toDto)
                .toList();
    }

    @Override
    public String longestFacultyName() {
        logger.info("Был вызван метод поиска самого длинного названия факультета");
        List<Faculty> faculties = findAll();
        return faculties.stream()
                .parallel()
                .map(Faculty::getName)
                .max(Comparator.comparing(String::length))
                .orElse("");
    }

    @Override
    public Faculty findOne(Specification<Faculty> specification) {
        logger.info("Был вызван метод поиска факультета по спецификации");
        Optional.ofNullable(specification).orElseThrow(
                () -> {
                    String msq = "Ошибка в методе поиска факультета по спецификации. specification = null";
                    logger.error(msq);
                    return new IllegalArgumentException(msq);
                }
        );
        return repository.findOne(specification).orElseThrow(
                () -> {
                    String msq = "При поиске факультета по спецификации произошла ошибка";
                    logger.error(msq);
                    return new NoSuchElementException(msq);
                }
        );
    }

}

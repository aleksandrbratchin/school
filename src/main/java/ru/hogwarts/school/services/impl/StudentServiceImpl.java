package ru.hogwarts.school.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.dto.faculty.FacultyResponseDto;
import ru.hogwarts.school.dto.student.StudentAddRequestDto;
import ru.hogwarts.school.dto.student.StudentResponseDto;
import ru.hogwarts.school.dto.student.StudentUpdateRequestDto;
import ru.hogwarts.school.mapper.ResponseMapper;
import ru.hogwarts.school.model.faculty.Faculty;
import ru.hogwarts.school.model.student.Student;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.services.api.FacultyService;
import ru.hogwarts.school.services.api.StudentService;
import ru.hogwarts.school.specifications.FacultySpecification;
import ru.hogwarts.school.specifications.StudentSpecification;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repository;
    private final FacultyService facultyService;
    private final ResponseMapper<Student, StudentResponseDto> studentResponseMapper;
    private final ResponseMapper<Faculty, FacultyResponseDto> facultyResponseMapper;
    private Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    public StudentServiceImpl(
            StudentRepository studentRepository,
            FacultyService facultyServiceImpl,
            ResponseMapper<Student, StudentResponseDto> studentResponseMapper,
            ResponseMapper<Faculty, FacultyResponseDto> facultyResponseMapper
    ) {
        this.repository = studentRepository;
        this.facultyService = facultyServiceImpl;
        this.studentResponseMapper = studentResponseMapper;
        this.facultyResponseMapper = facultyResponseMapper;
    }

    @Override
    public Student create(Student student) {
        logger.info("Был вызван метод создания студента");
        Optional.ofNullable(student).orElseThrow(
                () -> {
                    String msq = "Ошибка в методе создания студента. student = null";
                    logger.error(msq);
                    return new IllegalArgumentException(msq);
                }
        );
        student.setId(null);
        return repository.save(student);
    }

    @Override
    public StudentResponseDto create(StudentAddRequestDto studentDto) {
        logger.info("Был вызван метод создания студента из dto");
        Optional.ofNullable(studentDto).orElseThrow(
                () -> {
                    String msq = "Ошибка в методе создания студента из dto. studentDto = null";
                    logger.error(msq);
                    return new IllegalArgumentException(msq);
                }
        );
        Faculty faculty = studentDto.facultyId() == null ? null : facultyService.findOne(FacultySpecification.idEqual(studentDto.facultyId()));
        Student student = Student.builder()
                .name(studentDto.name())
                .age(studentDto.age())
                .faculty(faculty)
                .build();
        return studentResponseMapper.toDto(create(student));
    }

    @Override
    public Student update(Student student) {
        logger.info("Был вызван метод изменения студента");
        Optional.ofNullable(student).orElseThrow(
                () -> {
                    String msq = "Ошибка в методе изменения студента. student = null";
                    logger.error(msq);
                    return new IllegalArgumentException(msq);
                }
        );
        UUID id = student.getId();
        if (!repository.existsById(id)) {
            String msq = "Ошибка при попытке изменения студента! " +
                    "Студент с id = '" + id + "' не найден.";
            logger.error(msq);
            throw new NoSuchElementException(msq);
        }
        return repository.save(student);
    }

    @Override
    public StudentResponseDto update(StudentUpdateRequestDto studentDto) {
        logger.info("Был вызван метод изменения студента из dto");
        Optional.ofNullable(studentDto).orElseThrow(
                () -> {
                    String msq = "Ошибка в методе изменения студента из dto. studentDto = null";
                    logger.error(msq);
                    return new IllegalArgumentException(msq);
                }
        );
        UUID id = studentDto.id();
        Faculty faculty = studentDto.facultyId() == null ?
                null :
                facultyService.findOne(FacultySpecification.idEqual(studentDto.facultyId()));
        Student student = findOne(StudentSpecification.idEqual(id));
        student.setName(studentDto.name());
        student.setAge(studentDto.age());
        student.setFaculty(faculty);
        return studentResponseMapper.toDto(
                update(student)
        );
    }

    @Override
    public Student delete(UUID id) {
        logger.info("Был вызван метод удаления студента по id");
        Optional.ofNullable(id).orElseThrow(
                () -> {
                    String msq = "Ошибка в методе удаления студента по id. id = null";
                    logger.error(msq);
                    return new IllegalArgumentException(msq);
                }
        );
        Student old = repository.findOne(StudentSpecification.idEqual(id))
                .orElseThrow(
                        () -> {
                            String msq = "Ошибка при попытке удаления студента! " +
                                    "Студент с id = '" + id + "' не найден.";
                            logger.error(msq);
                            return new NoSuchElementException(msq);
                        }
                );
        repository.deleteById(id);
        logger.debug(old.toString());
        return old;
    }

    @Override
    public StudentResponseDto deleteById(UUID id) {
        logger.info("Был вызван метод удаления студента по id");
        return studentResponseMapper.toDto(delete(id));
    }

    @Override
    public List<Student> findAll(Specification<Student> specification) {
        logger.info("Был вызван метод фильтрации студентов по спецификации");
        Optional.ofNullable(specification).orElseThrow(
                () -> {
                    String msq = "Ошибка в методе фильтрации студентов по спецификации. specification = null";
                    logger.error(msq);
                    return new IllegalArgumentException(msq);
                }
        );
        return repository.findAll(specification);
    }


    @Override
    public List<Student> findAll() {
        logger.info("Был вызван метод получения всех студентов");
        return repository.findAll();
    }

    @Override
    public List<StudentResponseDto> findAllDto() {
        logger.info("Был вызван метод получения всех студентов");
        return repository.findAll().stream()
                .map(studentResponseMapper::toDto)
                .toList();
    }
    @Override
    public List<StudentResponseDto> findAllDto(PageRequest pageRequest) {
        logger.info("Был вызван метод получения всех студентов постранично");
        return repository.findAll(pageRequest).stream()
                .map(studentResponseMapper::toDto)
                .toList();
    }

    @Override
    public Student findOne(Specification<Student> specification) {
        logger.info("Был вызван метод поиска студента по спецификации");
        Optional.ofNullable(specification).orElseThrow(
                () -> {
                    String msq = "Ошибка в методе поиска студентов по спецификации. specification = null";
                    logger.error(msq);
                    return new IllegalArgumentException(msq);
                }
        );
        return repository.findOne(specification).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<StudentResponseDto> filterByAge(Integer age) {
        logger.info("Был вызван метод фильтрации студентов по возрасту");
        Optional.ofNullable(age).orElseThrow(
                () -> {
                    String msq = "Ошибка в методе фильтрации студентов по возрасту. age = null";
                    logger.error(msq);
                    return new IllegalArgumentException(msq);
                }
        );
        return findAll(StudentSpecification.ageEqual(age))
                .stream()
                .map(studentResponseMapper::toDto)
                .toList();
    }

    @Override
    public FacultyResponseDto getFacultyById(UUID id) {
        logger.info("Был вызван метод получения факультета по id студента");
        Optional.ofNullable(id).orElseThrow(
                () -> {
                    String msq = "Ошибка в методе получения факультета по id студента. id = null";
                    logger.error(msq);
                    return new IllegalArgumentException(msq);
                }
        );
        return facultyResponseMapper.toDto(
                findOne(StudentSpecification.idEqual(id)).getFaculty()
        );
    }

    @Override
    public List<StudentResponseDto> findByAgeBetween(Integer min, Integer max) {
        logger.info("Был вызван метод фильтрации студентов по возрасту в диапазоне");
        Optional.ofNullable(min).orElseThrow(
                () -> {
                    String msq = "Ошибка в методе фильтрации студентов по возрасту в диапазоне. min = null";
                    logger.error(msq);
                    return new IllegalArgumentException(msq);
                }
        );
        Optional.ofNullable(max).orElseThrow(
                () -> {
                    String msq = "Ошибка в методе получения факультета по id студента. max = null";
                    logger.error(msq);
                    return new IllegalArgumentException(msq);
                }
        );
        return findAll(StudentSpecification.ageInBetween(min, max))
                .stream()
                .map(studentResponseMapper::toDto)
                .toList();
    }

    @Override
    public Double getAverageAge() {
        logger.info("Был вызван метод получения среднего возраста студентов");
        return repository.getAverageAge();
    }

    @Override
    public Integer getCountStudents() {
        logger.info("Был вызван метод получения количества студентов");
        return repository.getCountStudents();
    }

    @Override
    public List<StudentResponseDto> getLastFiveOldStudents() {
        logger.info("Был вызван метод получения 5ти последних студентов");
        return repository.getLastFiveOldStudents().stream()
                .map(studentResponseMapper::toDto)
                .toList();
    }

    @Override
    public List<String> nameStartsWithLetterA() {
        List<Student> faculties = findAll();
        return faculties.stream()
                .parallel()
                .map(student -> student.getName().toUpperCase())
                .filter(field -> field.startsWith("А"))
                .sorted()
                .toList();
    }

    @Override
    public Double getAverageAgeWithStream() {
        List<Student> faculties = findAll();
        return faculties.stream()
                .parallel()
                .mapToDouble(Student::getAge)
                .average()
                .orElse(Double.NaN);
    }
}

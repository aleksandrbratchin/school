package ru.hogwarts.school.services.impl;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.dto.faculty.FacultyResponseDto;
import ru.hogwarts.school.dto.student.StudentAddRequestDto;
import ru.hogwarts.school.dto.student.StudentResponseDto;
import ru.hogwarts.school.dto.student.StudentUpdateRequestDto;
import ru.hogwarts.school.mapper.RequestMapper;
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
        Optional.ofNullable(student).orElseThrow(IllegalArgumentException::new);
        student.setId(null);
        return repository.save(student);
    }

    @Override
    public StudentResponseDto create(StudentAddRequestDto dto) {
        Optional.ofNullable(dto).orElseThrow(IllegalArgumentException::new);
        Faculty faculty = dto.facultyId() == null ? null : facultyService.findOne(FacultySpecification.idEqual(dto.facultyId()));
        Student student = Student.builder()
                .name(dto.name())
                .age(dto.age())
                .faculty(faculty)
                .build();
        return studentResponseMapper.toDto(create(student));
    }

    @Override
    public Student update(Student student) {
        Optional.ofNullable(student).orElseThrow(IllegalArgumentException::new);
        UUID id = student.getId();
        if (!repository.existsById(id)) {
            throw new NoSuchElementException(
                    "Ошибка при попытке изменения студента! " +
                            "Студент с id = '" + id + "' не найден."
            );
        }
        return repository.save(student);
    }

    @Override
    public StudentResponseDto update(StudentUpdateRequestDto studentDto) {
        Optional.ofNullable(studentDto).orElseThrow(IllegalArgumentException::new);
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
        Optional.ofNullable(id).orElseThrow(IllegalArgumentException::new);
        Student old = repository.findOne(StudentSpecification.idEqual(id))
                .orElseThrow(() -> new NoSuchElementException(
                                "Ошибка при попытке удаления студента! " +
                                        "Студент с id = '" + id + "' не найден."
                        )
                );
        repository.deleteById(id);
        return old;
    }

    @Override
    public StudentResponseDto deleteById(UUID id) {
        return studentResponseMapper.toDto(delete(id));
    }

    @Override
    public List<Student> findAll(Specification<Student> specification) {
        Optional.ofNullable(specification).orElseThrow(IllegalArgumentException::new);
        return repository.findAll(specification);
    }


    @Override
    public List<Student> findAll() {
        return repository.findAll();
    }

    @Override
    public List<StudentResponseDto> findAllDto() {
        return repository.findAll().stream()
                .map(studentResponseMapper::toDto)
                .toList();
    }

    @Override
    public Student findOne(Specification<Student> specification) {
        Optional.ofNullable(specification).orElseThrow(IllegalArgumentException::new);
        return repository.findOne(specification).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<StudentResponseDto> filterByAge(Integer age) {
        return findAll(StudentSpecification.ageEqual(age))
                .stream()
                .map(studentResponseMapper::toDto)
                .toList();
    }

    @Override
    public FacultyResponseDto getFacultyById(UUID id) {
        return facultyResponseMapper.toDto(
                findOne(StudentSpecification.idEqual(id)).getFaculty()
        );
    }

    @Override
    public List<StudentResponseDto> findByAgeBetween(Integer min, Integer max) {
        return findAll(StudentSpecification.ageInBetween(min, max))
                .stream()
                .map(studentResponseMapper::toDto)
                .toList();
    }

    @Override
    public Double getAverageAge() {
        return repository.getAverageAge();
    }

    @Override
    public Integer getCountStudents() {
        return repository.getCountStudents();
    }

    @Override
    public List<StudentResponseDto> getLastFiveOldStudents() {
        return repository.getLastFiveOldStudents().stream()
                .map(studentResponseMapper::toDto)
                .toList();
    }
}

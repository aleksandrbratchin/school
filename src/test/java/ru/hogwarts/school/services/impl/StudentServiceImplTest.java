package ru.hogwarts.school.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.hogwarts.school.mapper.faculty.FacultyAddRequestMapper;
import ru.hogwarts.school.mapper.faculty.FacultyInfoMapper;
import ru.hogwarts.school.mapper.faculty.FacultyResponseMapper;
import ru.hogwarts.school.mapper.student.StudentResponseMapper;
import ru.hogwarts.school.model.student.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.specifications.StudentSpecification;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository repository;
    @Mock
    private FacultyRepository facultyRepository;
    @Spy
    private FacultyInfoMapper facultyInfoMapper;
    @Spy
    private FacultyAddRequestMapper facultyAddRequestMapper;
    @Spy
    @InjectMocks
    private StudentResponseMapper studentResponseMapper;
    @Spy
    @InjectMocks
    private FacultyResponseMapper facultyResponseMapper;
    @Spy
    @InjectMocks
    private FacultyServiceImpl facultyService;
    @InjectMocks
    private StudentServiceImpl service;


    @Nested
    class Success {

        @Test
        void nameStartsWithLetterA() {
            Mockito.when(repository.findAll())
                    .thenReturn(
                            List.of(
                                    new Student("Юра", 1, null),
                                    new Student("Вася", 2, null),
                                    new Student("Коля", 3, null),
                                    new Student("Александр", 4, null),
                                    new Student("Никита", 5, null),
                                    new Student("Гена", 6, null),
                                    new Student("Антон", 7, null),
                                    new Student("Мирон", 8, null),
                                    new Student("Андрей", 9, null),
                                    new Student("Вадим", 10, null)
                            )
                    );

            List<String> strings = service.nameStartsWithLetterA();

            assertThat(strings.size()).isEqualTo(3);
        }

        @Test
        void getAverageAgeWithStream() {
            Mockito.when(repository.findAll())
                    .thenReturn(
                            List.of(
                                    new Student(null, 1, null),
                                    new Student(null, 2, null),
                                    new Student(null, 3, null),
                                    new Student(null, 4, null),
                                    new Student(null, 5, null),
                                    new Student(null, 6, null),
                                    new Student(null, 7, null),
                                    new Student(null, 8, null),
                                    new Student(null, 9, null),
                                    new Student(null, 10, null)
                            )
                    );

            Double age = service.getAverageAgeWithStream();

            assertThat(age).isEqualTo(5.5);
        }
        @Test
        void getAverageAge() {
            Mockito.when(repository.getAverageAge())
                    .thenReturn(11.5);

            Double age = service.getAverageAge();

            assertThat(age).isEqualTo(11.5);
        }

        @Test
        void getCountStudents() {
            Mockito.when(repository.getCountStudents())
                    .thenReturn(99);

            Integer count = service.getCountStudents();

            assertThat(count).isEqualTo(99);
        }

        @Test
        void create() {
            Student potter = new Student("Гарри Джеймс Поттер", 11, null);
            Mockito.when(repository.exists(any(Specification.class)))
                    .thenReturn(false);
            Student newStudent = new Student(UUID.randomUUID(), potter.getName(), potter.getAge(), null);
            Mockito.when(repository.save(any(Student.class)))
                    .thenReturn(newStudent);

            Student created = service.create(potter);

            assertThat(created.getName()).isEqualTo(potter.getName());
            assertThat(created.getId()).isNotNull();
        }

        @Nested
        class RepositoryIsEmpty {

            @Test
            void findAll() {
                Mockito.when(repository.findAll())
                        .thenReturn(new ArrayList<>());

                List<Student> results = service.findAll();

                assertThat(results.size()).isEqualTo(0);
            }

            @Test
            void findAllSpecification() {
                Mockito.when(repository.findAll(any(Specification.class)))
                        .thenReturn(new ArrayList<>());

                List<Student> results = service.findAll(StudentSpecification.ageEqual(11));

                assertThat(results.size()).isEqualTo(0);
            }

        }

        @Nested
        class RepositoryIsNotEmpty {
            private List<Student> students = new ArrayList<>();
            private Student malfoy = null;

            @BeforeEach
            public void initEach() {
                Student granger = new Student(UUID.randomUUID(), "Гермиона Джин Грейнджер", 11, null);
                malfoy = new Student(UUID.randomUUID(), "Драко Люциус Малфой", 12, null);
                Student lovegood = new Student(UUID.randomUUID(), "Полумна Лавгуд", 11, null);
                students = Arrays.asList(granger, malfoy, lovegood);
            }

            @Test
            void delete() {
                Mockito.when(repository.findOne(any(Specification.class)))
                        .thenReturn(Optional.of(malfoy));
                Mockito.doNothing().when(repository).deleteById(any(UUID.class));

                Student deleted = service.delete(malfoy.getId());

                assertThat(deleted.getName()).isEqualTo("Драко Люциус Малфой");
            }

            @Test
            void update() {
                Student slytherin = new Student(malfoy.getId(), "Гарри Джеймс Поттер", 11, null);
                Mockito.when(repository.existsById(any(UUID.class)))
                        .thenReturn(true);
                Mockito.when(repository.save(any(Student.class)))
                        .thenReturn(slytherin);

                Student updated = service.update(slytherin);

                assertThat(updated.getName()).isEqualTo(slytherin.getName());
            }

            @Test
            void findAll() {
                Mockito.when(repository.findAll())
                        .thenReturn(students);

                List<Student> results = service.findAll();

                assertThat(results.size()).isEqualTo(3);
            }

            @Test
            void findAllSpecification() {
                Mockito.when(repository.findAll(any(Specification.class)))
                        .thenReturn(students);

                List<Student> results = service.findAll(StudentSpecification.ageEqual(12));

                assertThat(results.size()).isEqualTo(3);
            }

            @Test
            void findOne() {
                Mockito.when(repository.findOne(any(Specification.class)))
                        .thenReturn(Optional.of(malfoy));

                Student result = service.findOne(StudentSpecification.ageEqual(12));

                assertThat(result.getName()).isEqualTo("Драко Люциус Малфой");
            }

        }

    }

    @Nested
    class Error {

        @Test
        void delete() {
            Mockito.when(repository.findOne(any(Specification.class)))
                    .thenReturn(Optional.empty());

            Throwable thrown = catchThrowable(() -> service.delete(UUID.randomUUID()));

            assertThat(thrown).isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void update() {
            Mockito.when(repository.existsById(any(UUID.class)))
                    .thenReturn(false);
            Student potter = new Student(UUID.randomUUID(), "Гарри Джеймс Поттер", 11, null);

            Throwable thrown = catchThrowable(() -> service.update(potter));

            assertThat(thrown).isInstanceOf(NoSuchElementException.class).hasMessageContaining(
                    potter.getId().toString()
            );
        }

        @Test
        void findOne() {
            Mockito.when(repository.findOne(any(Specification.class)))
                    .thenReturn(Optional.empty());

            Throwable thrown = catchThrowable(() -> service.findOne(StudentSpecification.nameEqual("Гарри Джеймс Поттер")));

            assertThat(thrown).isInstanceOf(NoSuchElementException.class);
        }

        @Nested
        class ParametersIsNull {

            @Test
            void delete() {

                Throwable thrown = catchThrowable(() -> service.delete(null));

                assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void findOne() {

                Throwable thrown = catchThrowable(() -> service.findOne(null));

                assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void findAll() {

                Throwable thrown = catchThrowable(() -> service.findAll(null));

                assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

}
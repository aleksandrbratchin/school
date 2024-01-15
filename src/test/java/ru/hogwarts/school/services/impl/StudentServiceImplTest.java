package ru.hogwarts.school.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.hogwarts.school.model.student.Student;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.specifications.StudentSpecification;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class StudentServiceImplTest {
    private StudentServiceImpl service;

    @Mock
    private StudentRepository repository;

    @BeforeEach
    void setUp() {
        service = new StudentServiceImpl(repository);
    }

    @Nested
    class Success {
        @Test
        void create() {
            Student potter = new Student("Гарри Джеймс Поттер", 11);
            Mockito.when(repository.exists(any(Specification.class)))
                    .thenReturn(false);
            Student newStudent = new Student(UUID.randomUUID(), potter.getName(), potter.getAge());
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
                Student granger = new Student(UUID.randomUUID(), "Гермиона Джин Грейнджер", 11);
                malfoy = new Student(UUID.randomUUID(), "Драко Люциус Малфой", 12);
                Student lovegood = new Student(UUID.randomUUID(), "Полумна Лавгуд", 11);
                students = Arrays.asList(granger, malfoy, lovegood);
            }

            @Test
            void delete() {
                Mockito.when(repository.findOne(any(Specification.class)))
                        .thenReturn(Optional.of(malfoy));

                Student deleted = service.delete(malfoy.getId());

                assertThat(deleted.getName()).isEqualTo("Драко Люциус Малфой");
            }

            @Test
            void update() {
                Student slytherin = new Student(malfoy.getId(), "Гарри Джеймс Поттер", 11);
                Mockito.when(repository.existsById(any(UUID.class)))
                        .thenReturn(true);
                Mockito.when(repository.exists(any(Specification.class)))
                        .thenReturn(false);
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
            Student potter = new Student(UUID.randomUUID(), "Гарри Джеймс Поттер", 11);

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
            void create() {

                Throwable thrown = catchThrowable(() -> service.create(null));

                assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void update() {

                Throwable thrown = catchThrowable(() -> service.update(null));

                assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
            }

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
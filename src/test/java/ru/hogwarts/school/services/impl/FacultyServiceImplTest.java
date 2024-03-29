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
import ru.hogwarts.school.model.faculty.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.specifications.FacultySpecification;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class FacultyServiceImplTest {

    @Mock
    private FacultyRepository repository;
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
    @InjectMocks
    private FacultyServiceImpl service;

    @Nested
    class Success {

        @Test
        void create() {
            Faculty slytherin = new Faculty("Слизерин", "зелёный, серебряный", new ArrayList<>());
            Mockito.when(repository.exists(any(Specification.class)))
                    .thenReturn(false);
            Faculty newSlytherin = new Faculty(UUID.randomUUID(), "Слизерин", "зелёный, серебряный", new ArrayList<>());
            Mockito.when(repository.save(any(Faculty.class)))
                    .thenReturn(newSlytherin);

            Faculty created = service.create(slytherin);

            assertThat(created.getName()).isEqualTo(slytherin.getName());
            assertThat(created.getId()).isNotNull();
        }

        @Nested
        class RepositoryIsEmpty {

            @Test
            void findAll() {
                Mockito.when(repository.findAll())
                        .thenReturn(new ArrayList<>());

                List<Faculty> results = service.findAll();

                assertThat(results.size()).isEqualTo(0);
            }

            @Test
            void findAllSpecification() {
                Mockito.when(repository.findAll(any(Specification.class)))
                        .thenReturn(new ArrayList<>());

                List<Faculty> results = service.findAll(FacultySpecification.colorLike("синий"));

                assertThat(results.size()).isEqualTo(0);
            }

        }

        @Nested
        class RepositoryIsNotEmpty {
            private List<Faculty> faculties = new ArrayList<>();
            private Faculty ravenclaw = null;

            @BeforeEach
            public void initEach() throws IllegalAccessException {
                Faculty gryffindor = new Faculty(UUID.randomUUID(), "Гриффиндор", "алый, золотой", new ArrayList<>());
                ravenclaw = new Faculty(UUID.randomUUID(), "Когтевран", "синий, бронзовый", new ArrayList<>());
                Faculty hufflepuff = new Faculty(UUID.randomUUID(), "Пуффендуй", "канареечно-жёлтый, чёрный", new ArrayList<>());
                faculties = Arrays.asList(gryffindor, ravenclaw, hufflepuff);
            }

            @Test
            void delete() {
                Mockito.when(repository.findOne(any(Specification.class)))
                        .thenReturn(Optional.of(ravenclaw));

                Faculty deleted = service.delete(ravenclaw.getId());

                assertThat(deleted.getName()).isEqualTo("Когтевран");
            }

            @Test
            void update() {
                Faculty slytherin = new Faculty(ravenclaw.getId(), "Слизерин", "зелёный, серебряный", new ArrayList<>());
                Mockito.when(repository.findById(any(UUID.class)))
                        .thenReturn(Optional.of(ravenclaw));
                Mockito.when(repository.exists(any(Specification.class)))
                        .thenReturn(false);
                Mockito.when(repository.save(any(Faculty.class)))
                        .thenReturn(slytherin);

                Faculty updated = service.update(slytherin);

                assertThat(updated.getName()).isEqualTo(slytherin.getName());
            }

            @Test
            void findAll() {
                Mockito.when(repository.findAll())
                        .thenReturn(faculties);

                List<Faculty> results = service.findAll();

                assertThat(results.size()).isEqualTo(3);
            }

            @Test
            void longestFacultyName() {
                Faculty gryffindor = new Faculty(UUID.randomUUID(), "Гриффиндор", "алый, золотой", new ArrayList<>());
                ravenclaw = new Faculty(UUID.randomUUID(), "Когтевран", "синий, бронзовый", new ArrayList<>());
                Faculty hufflepuff = new Faculty(UUID.randomUUID(), "Пуффендуй", "канареечно-жёлтый, чёрный", new ArrayList<>());
                Faculty gryffindok = new Faculty(UUID.randomUUID(), "Гриффиндок", "", new ArrayList<>());
                faculties = Arrays.asList(gryffindor, ravenclaw, hufflepuff, gryffindok);
                Mockito.when(repository.findAll())
                        .thenReturn(faculties);

                String results = service.longestFacultyName();

                assertThat(results).isEqualTo("Гриффиндор");
            }

            @Test
            void findAllSpecification() {
                Mockito.when(repository.findAll(any(Specification.class)))
                        .thenReturn(faculties);

                List<Faculty> results = service.findAll(FacultySpecification.colorLike("синий"));

                assertThat(results.size()).isEqualTo(3);
            }

            @Test
            void findByColor() {
                Mockito.when(repository.findAll(any(Specification.class)))
                        .thenReturn(List.of(ravenclaw));

                List<Faculty> result = service.findAll(FacultySpecification.colorLike("синий"));

                assertThat(result.size()).isEqualTo(1);
            }
        }

    }

    @Nested
    class Error {

        @Test
        void create() {
            Faculty slytherin = new Faculty("Слизерин", "зелёный, серебряный", new ArrayList<>());
            Mockito.when(repository.exists(any(Specification.class)))
                    .thenReturn(true);

            Throwable thrown = catchThrowable(() -> service.create(slytherin));

            assertThat(thrown).isInstanceOf(IllegalArgumentException.class).hasMessageContaining(
                    slytherin.getName()
            );
        }

        @Test
        void delete() {
            Mockito.when(repository.findOne(any(Specification.class)))
                    .thenReturn(Optional.empty());

            Throwable thrown = catchThrowable(() -> service.delete(UUID.randomUUID()));

            assertThat(thrown).isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void updateNameIsPresent() {
            UUID id = UUID.randomUUID();
            Faculty slytherin = new Faculty(id, "Слизерин", "зелёный, серебряный", new ArrayList<>());
            Faculty hufflepuff = new Faculty(id, "Пуффендуй", "зелёный, серебряный", new ArrayList<>());
            Mockito.when(repository.findById(any(UUID.class)))
                    .thenReturn(Optional.of(slytherin));
            Mockito.when(repository.exists(any(Specification.class)))
                    .thenReturn(true);

            Throwable thrown = catchThrowable(() -> service.update(hufflepuff));

            assertThat(thrown).isInstanceOf(IllegalArgumentException.class).hasMessageContaining(
                    hufflepuff.getName()
            );
        }

        @Test
        void updateIdIsNotPresent() {
            Mockito.when(repository.existsById(any(UUID.class)))
                    .thenReturn(false);
            Faculty hufflepuff = new Faculty(UUID.randomUUID(), "Пуффендуй", "зелёный, серебряный", new ArrayList<>());

            Throwable thrown = catchThrowable(() -> service.update(hufflepuff));

            assertThat(thrown).isInstanceOf(NoSuchElementException.class).hasMessageContaining(
                    hufflepuff.getId().toString()
            );
        }

        @Test
        void findOne() {
            Mockito.when(repository.findOne(any(Specification.class)))
                    .thenReturn(Optional.empty());

            Throwable thrown = catchThrowable(() -> service.findOne(FacultySpecification.colorLike("зелёный")));

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
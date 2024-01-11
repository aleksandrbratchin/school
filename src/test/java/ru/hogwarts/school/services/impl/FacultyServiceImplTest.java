package ru.hogwarts.school.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.hogwarts.school.model.faculty.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.specifications.FacultySpecification;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(SpringExtension.class)
class FacultyServiceImplTest {

    private FacultyServiceImpl service;

    @Mock
    private FacultyRepository repository;

    @BeforeEach
    void setUp() {
        service = new FacultyServiceImpl(repository);
    }


    @Nested
    class Success {

        @Nested
        class RepositoryIsEmpty {

            @Test
            void create() {
                Faculty slytherin = new Faculty("Слизерин", "зелёный, серебряный");
                Mockito.when(repository.exists(any(Specification.class)))
                        .thenReturn(false);
                Faculty newSlytherin = new Faculty(UUID.randomUUID(), "Слизерин", "зелёный, серебряный");
                Mockito.when(repository.save(any(Faculty.class)))
                        .thenReturn(newSlytherin);

                Faculty created = service.create(slytherin);

                assertThat(created.getName()).isEqualTo(slytherin.getName());
                assertThat(created.getId()).isNotNull();
            }

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
/*            @BeforeEach
            public void initEach() throws IllegalAccessException {
                service = new FacultyServiceImpl();
                Faculty gryffindor = new Faculty(0L, "Гриффиндор", "алый, золотой");
                Faculty ravenclaw = new Faculty(1L, "Когтевран", "синий, бронзовый");
                Faculty hufflepuff = new Faculty(2L, "Пуффендуй", "канареечно-жёлтый, чёрный");
                Map<Long, Faculty> testFaculties = new HashMap<>(
                        Map.of(0L, gryffindor,
                                1L, ravenclaw,
                                2L, hufflepuff)
                );

                fieldMap.set(service, testFaculties);
                fieldId.set(service, 3);
            }

            @Test
            void create() throws IllegalAccessException {
                Faculty slytherin = new Faculty("Слизерин", "зелёный, серебряный");

                Faculty created = service.create(slytherin);
                var faculties = (Map<Long, Faculty>) fieldMap.get(service);

                assertThat(created.getName()).isEqualTo(slytherin.getName());
                assertThat(faculties.size()).isEqualTo(4);
                assertThat(
                        faculties.values().stream().map(Faculty::getName).toList()
                ).contains(slytherin.getName());
            }

            @Test
            void update() throws IllegalAccessException {
                Faculty slytherin = new Faculty(0L, "Слизерин", "зелёный, серебряный");

                Faculty oldValue = service.update(slytherin);
                var faculties = (Map<Long, Faculty>) fieldMap.get(service);

                assertThat(oldValue.getName()).isEqualTo("Гриффиндор");
                assertThat(faculties.size()).isEqualTo(3);
                assertThat(
                        faculties.values().stream().map(Faculty::getName).toList()
                ).contains(slytherin.getName());
            }

            @Test
            void delete() throws IllegalAccessException {
                Long id = 1L;

                Faculty deleted = service.delete(id);
                var faculties = (Map<Long, Faculty>) fieldMap.get(service);

                assertThat(deleted.getName()).isEqualTo("Когтевран");
                assertThat(faculties.size()).isEqualTo(2);
                assertThat(
                        faculties.values().stream().map(Faculty::getName).toList()
                ).doesNotContain("Когтевран");
            }

            @Test
            void findAll() {

                Map<Long, Faculty> facultyMap = service.findAll();

                assertThat(facultyMap.size()).isEqualTo(3);
            }

            @Test
            void findAllSpecification() {

                Map<Long, Faculty> results = service.findAll(new FacultyContainsColorSpecification("синий"));

                assertThat(results.size()).isEqualTo(1);
                assertThat(results.values().stream().map(Faculty::getName)).contains("Когтевран");
            }

            @Test
            void findOne() {

                Faculty result = service.findOne(new FacultyContainsColorSpecification("синий"));

                assertThat(result.getName()).isEqualTo("Когтевран");
            }*/
        }

    }

    @Nested
    class Error {

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


        @Nested
        class RepositoryIsEmpty {
/*            @BeforeEach
            public void initEach() throws IllegalAccessException {
                service = new FacultyServiceImpl();
                fieldId.set(service, 0);
            }

            @Test
            void update() {
                Faculty hufflepuff = new Faculty(1L, "Пуффендуй", "зелёный, серебряный");

                Throwable thrown = catchThrowable(() -> service.update(hufflepuff));

                assertThat(thrown).isInstanceOf(NotFoundElementException.class).hasMessageContaining(
                        hufflepuff.getId().toString()
                );
            }

            @Test
            void updateIdIsNull() {
                Faculty hufflepuff = new Faculty("Пуффендуй", "зелёный, серебряный");

                Throwable thrown = catchThrowable(() -> service.update(hufflepuff));

                assertThat(thrown).isInstanceOf(NotFoundElementException.class).hasMessageContaining(
                        "null"
                );
            }

            @Test
            void delete() {
                Long id = 0L;

                Throwable thrown = catchThrowable(() -> service.delete(id));

                assertThat(thrown).isInstanceOf(NotFoundElementException.class).hasMessageContaining(
                        id.toString()
                );
            }*/


        }

        @Nested
        class RepositoryIsNotEmpty {
/*            private List<Faculty> faculties = new ArrayList<>();

            @BeforeEach
            public void initEach() throws IllegalAccessException {
                Faculty gryffindor = new Faculty(UUID.randomUUID(), "Гриффиндор", "алый, золотой");
                Faculty ravenclaw = new Faculty(UUID.randomUUID(), "Когтевран", "синий, бронзовый");
                Faculty hufflepuff = new Faculty(UUID.randomUUID(), "Пуффендуй", "канареечно-жёлтый, чёрный");
                faculties = Arrays.asList(gryffindor, ravenclaw, hufflepuff);
            }*/

            @Test
            void create() {
                Faculty slytherin = new Faculty("Слизерин", "зелёный, серебряный");
                Mockito.when(repository.exists(any(Specification.class)))
                        .thenReturn(true);

                Throwable thrown = catchThrowable(() -> service.create(slytherin));

                assertThat(thrown).isInstanceOf(IllegalArgumentException.class).hasMessageContaining(
                        slytherin.getName()
                );
            }
            @Test
            void updateNameIsPresent() {
                Mockito.when(repository.existsById(any(UUID.class)))
                        .thenReturn(true);
                Mockito.when(repository.exists(any(Specification.class)))
                        .thenReturn(true);
                Faculty hufflepuff = new Faculty(UUID.randomUUID(), "Пуффендуй", "зелёный, серебряный");

                Throwable thrown = catchThrowable(() -> service.update(hufflepuff));

                assertThat(thrown).isInstanceOf(IllegalArgumentException.class).hasMessageContaining(
                        hufflepuff.getName()
                );
            }
            @Test
            void updateIdIsNotPresent() {
                Mockito.when(repository.existsById(any(UUID.class)))
                        .thenReturn(false);
                Faculty hufflepuff = new Faculty(UUID.randomUUID(), "Пуффендуй", "зелёный, серебряный");

                Throwable thrown = catchThrowable(() -> service.update(hufflepuff));

                assertThat(thrown).isInstanceOf(NoSuchElementException.class).hasMessageContaining(
                        hufflepuff.getId().toString()
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
            void findOne() {
                Mockito.when(repository.findOne(any(Specification.class)))
                        .thenReturn(Optional.empty());

                Throwable thrown = catchThrowable(() -> service.findOne(FacultySpecification.colorLike("зелёный")));

                assertThat(thrown).isInstanceOf(NoSuchElementException.class);
            }


        }

    }

}
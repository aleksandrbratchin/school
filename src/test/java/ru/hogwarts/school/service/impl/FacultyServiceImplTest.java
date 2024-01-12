package ru.hogwarts.school.service.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.hogwarts.school.exception.MyIllegalArgumentException;
import ru.hogwarts.school.exception.NotFoundElementException;
import ru.hogwarts.school.model.faculty.Faculty;
import ru.hogwarts.school.specification.FacultyContainsColorSpecification;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;


class FacultyServiceImplTest {

    private FacultyServiceImpl service;
    private static Field fieldMap;
    private static Field fieldId;

    @BeforeAll
    public static void setup() throws NoSuchFieldException {
        fieldMap = FacultyServiceImpl.class.getDeclaredField("facultyMap");
        fieldMap.setAccessible(true);
        fieldId = FacultyServiceImpl.class.getDeclaredField("id");
        fieldId.setAccessible(true);
    }

    @Nested
    class Success {

        @Nested
        class MapIsEmpty {
            @BeforeEach
            public void initEach() throws IllegalAccessException {
                service = new FacultyServiceImpl();
                fieldId.set(service, 0);
            }

            @Test
            void create() throws IllegalAccessException {
                Faculty slytherin = new Faculty("Слизерин", "зелёный, серебряный");

                Faculty created = service.create(slytherin);
                var faculties = (Map<Long, Faculty>) fieldMap.get(service);

                assertThat(created.getName()).isEqualTo(slytherin.getName());
                assertThat(faculties.size()).isEqualTo(1);
                assertThat(
                        faculties.values().stream().map(Faculty::getName).toList()
                ).contains(slytherin.getName());
            }

            @Test
            void findAll() {

                Map<Long, Faculty> results = service.findAll();

                assertThat(results.size()).isEqualTo(0);
            }
            @Test
            void findAllSpecification() {

                Map<Long, Faculty> results = service.findAll(new FacultyContainsColorSpecification("синий"));

                assertThat(results.size()).isEqualTo(0);
            }

        }

        @Nested
        class MapIsNotEmpty {
            @BeforeEach
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
            }
        }

    }

    @Nested
    class Error {

        @Nested
        class ParametersIsNull {

            private final Faculty nullValue = null;

            @Test
            void create() {

                Throwable thrown = catchThrowable(() -> service.create(nullValue));

                assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessageContaining("null");
            }

            @Test
            void update() {

                Throwable thrown = catchThrowable(() -> service.update(nullValue));

                assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessageContaining("null");
            }

            @Test
            void delete() {

                Throwable thrown = catchThrowable(() -> service.delete(null));

                assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessageContaining("null");
            }

            @Test
            void findOne() {

                Throwable thrown = catchThrowable(() -> service.findOne(null));

                assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessageContaining("null");
            }

            @Test
            void findAll() {

                Throwable thrown = catchThrowable(() -> service.findAll(null));

                assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessageContaining("null");
            }
        }


        @Nested
        class MapIsEmpty {
            @BeforeEach
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
            }


        }

        @Nested
        class MapIsNotEmpty {
            @BeforeEach
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
            void create() {
                Faculty hufflepuff = new Faculty("Пуффендуй", "зелёный, серебряный");

                Throwable thrown = catchThrowable(() -> service.create(hufflepuff));

                assertThat(thrown).isInstanceOf(MyIllegalArgumentException.class).hasMessageContaining(
                        hufflepuff.getName()
                );
            }

            @Test
            void updateNameIsPresent() {
                Faculty hufflepuff = new Faculty(1L, "Пуффендуй", "зелёный, серебряный");

                Throwable thrown = catchThrowable(() -> service.update(hufflepuff));

                assertThat(thrown).isInstanceOf(MyIllegalArgumentException.class).hasMessageContaining(
                        hufflepuff.getName()
                );
            }

            @Test
            void updateIdIsNotPresent() {
                Faculty slytherin = new Faculty(10L, "Слизерин", "зелёный, серебряный");

                Throwable thrown = catchThrowable(() -> service.update(slytherin));

                assertThat(thrown).isInstanceOf(NotFoundElementException.class).hasMessageContaining(
                        slytherin.getId().toString()
                );
            }

            @Test
            void delete() {
                Long id = 10L;

                Throwable thrown = catchThrowable(() -> service.delete(id));

                assertThat(thrown).isInstanceOf(NotFoundElementException.class).hasMessageContaining(
                        id.toString()
                );
            }

            @Test
            void findOne() {

                Throwable thrown = catchThrowable(() -> service.findOne(new FacultyContainsColorSpecification("зелёный")));

                assertThat(thrown).isInstanceOf(NotFoundElementException.class);
            }


        }

    }

}
package ru.hogwarts.school.service.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.hogwarts.school.exception.MyIllegalArgumentException;
import ru.hogwarts.school.exception.NotFoundElementException;
import ru.hogwarts.school.model.student.Student;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class StudentServiceImplTest {
    private StudentServiceImpl service;
    private static Field fieldMap;
    private static Field fieldId;

    @BeforeAll
    public static void setup() throws NoSuchFieldException {
        fieldMap = StudentServiceImpl.class.getDeclaredField("studentMap");
        fieldMap.setAccessible(true);
        fieldId = StudentServiceImpl.class.getDeclaredField("id");
        fieldId.setAccessible(true);
    }

    @Nested
    class Success {

        @Nested
        class MapIsEmpty {
            @BeforeEach
            public void initEach() throws IllegalAccessException {
                service = new StudentServiceImpl();
                fieldId.set(service, 0);
            }

            @Test
            void create() throws IllegalAccessException {
                Student potter = new Student("Гарри Джеймс Поттер", 11);

                Student created = service.create(potter);
                var students = (Map<Long, Student>) fieldMap.get(service);

                assertThat(created.getName()).isEqualTo(potter.getName());
                assertThat(students.size()).isEqualTo(1);
                assertThat(
                        students.values().stream().map(Student::getName).toList()
                ).contains(potter.getName());
            }


        }

        @Nested
        class MapIsNotEmpty {
            @BeforeEach
            public void initEach() throws IllegalAccessException {
                service = new StudentServiceImpl();
                Student granger = new Student(0L, "Гермиона Джин Грейнджер", 11);
                Student malfoy = new Student(1L, "Драко Люциус Малфой", 11);
                Student lovegood = new Student(2L, "Полумна Лавгуд", 11);
                Map<Long, Student> testFaculties = new HashMap<>(
                        Map.of(0L, granger,
                                1L, malfoy,
                                2L, lovegood)
                );

                fieldMap.set(service, testFaculties);
                fieldId.set(service, 3);
            }

            @Test
            void create() throws IllegalAccessException {
                Student potter = new Student("Гарри Джеймс Поттер", 11);

                Student created = service.create(potter);
                var students = (Map<Long, Student>) fieldMap.get(service);

                assertThat(created.getName()).isEqualTo(potter.getName());
                assertThat(students.size()).isEqualTo(4);
                assertThat(
                        students.values().stream().map(Student::getName).toList()
                ).contains(potter.getName());
            }

            @Test
            void update() throws IllegalAccessException {
                Student potter = new Student(0L, "Гарри Джеймс Поттер", 11);

                Student oldValue = service.update(potter);
                var students = (Map<Long, Student>) fieldMap.get(service);

                assertThat(oldValue.getName()).contains("Грейнджер");
                assertThat(students.size()).isEqualTo(3);
                assertThat(
                        students.values().stream().map(Student::getName).toList()
                ).contains(potter.getName());
            }

            @Test
            void delete() throws IllegalAccessException {
                Long id = 1L;

                Student deleted = service.delete(id);
                var students = (Map<Long, Student>) fieldMap.get(service);

                assertThat(deleted.getName()).contains("Малфой");
                assertThat(students.size()).isEqualTo(2);
                assertThat(
                        students.values().stream().map(Student::getName).toList()
                ).doesNotContain("Драко Люциус Малфой");
            }

            @Test
            void findAll() {

                Map<Long, Student> facultyMap = service.findAll();

                assertThat(facultyMap.size()).isEqualTo(3);
            }
        }
    }

    @Nested
    class Error {

        @Nested
        class ParametersIsNull {

            @Test
            void create() {

                Throwable thrown = catchThrowable(() -> service.create(null));

                assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessageContaining("null");
            }

            @Test
            void update() {

                Throwable thrown = catchThrowable(() -> service.update(null));

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
        }


        @Nested
        class MapIsEmpty {
            @BeforeEach
            public void initEach() throws IllegalAccessException {
                service = new StudentServiceImpl();
                fieldId.set(service, 0);
            }

            @Test
            void update() {
                Student malfoy = new Student(2L, "Драко Люциус Малфой", 11);

                Throwable thrown = catchThrowable(() -> service.update(malfoy));

                assertThat(thrown).isInstanceOf(NotFoundElementException.class).hasMessageContaining(
                        malfoy.getId().toString()
                );
            }

            @Test
            void updateIdIsNull() {
                Student malfoy = new Student("Драко Люциус Малфой", 11);

                Throwable thrown = catchThrowable(() -> service.update(malfoy));

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
                service = new StudentServiceImpl();
                Student granger = new Student(0L, "Гермиона Джин Грейнджер", 11);
                Student malfoy = new Student(1L, "Драко Люциус Малфой", 11);
                Student lovegood = new Student(2L, "Полумна Лавгуд", 11);
                Map<Long, Student> testFaculties = new HashMap<>(
                        Map.of(0L, granger,
                                1L, malfoy,
                                2L, lovegood)
                );

                fieldMap.set(service, testFaculties);
                fieldId.set(service, 3);
            }

            @Test
            void create() {
                Student lovegood = new Student("Полумна Лавгуд", 11);

                Throwable thrown = catchThrowable(() -> service.create(lovegood));

                assertThat(thrown).isInstanceOf(MyIllegalArgumentException.class).hasMessageContaining(
                        lovegood.getName()
                );
            }

            @Test
            void updateStudentIsPresent() {
                Student granger = new Student(1L, "Гермиона Джин Грейнджер", 11);

                Throwable thrown = catchThrowable(() -> service.update(granger));

                assertThat(thrown).isInstanceOf(MyIllegalArgumentException.class).hasMessageContaining(
                        granger.getName()
                );
            }

            @Test
            void updateIdIsNotPresent() {
                Student malfoy = new Student(10L, "Гарри Джеймс Поттер", 11);

                Throwable thrown = catchThrowable(() -> service.update(malfoy));

                assertThat(thrown).isInstanceOf(NotFoundElementException.class).hasMessageContaining(
                        malfoy.getId().toString()
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
        }
    }

}
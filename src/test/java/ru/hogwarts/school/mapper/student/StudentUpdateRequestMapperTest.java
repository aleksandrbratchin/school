package ru.hogwarts.school.mapper.student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.hogwarts.school.dto.student.StudentUpdateRequestDto;
import ru.hogwarts.school.model.faculty.Faculty;
import ru.hogwarts.school.model.student.Student;
import ru.hogwarts.school.services.impl.FacultyServiceImpl;
import ru.hogwarts.school.services.impl.StudentServiceImpl;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class StudentUpdateRequestMapperTest {

    private StudentUpdateRequestMapper mapper;
    @Mock
    private StudentServiceImpl studentService;
    @Mock
    private FacultyServiceImpl facultyService;

    @BeforeEach
    void setUp() {
        mapper = new StudentUpdateRequestMapper(studentService, facultyService);
    }

    @Test
    void studentIsCorrect() {
        Faculty slytherin = new Faculty(UUID.fromString("4fa85f64-5717-4562-b3fc-2c963f66afa6"), "Слизерин", "зелёный, серебряный", new ArrayList<>());
        Student malfoy = new Student(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Драко Люциус Малфой", 12, slytherin);
        StudentUpdateRequestDto dto = new StudentUpdateRequestDto(malfoy.getId(), malfoy.getName(), malfoy.getAge(), slytherin.getId());
        Mockito.when(studentService.findOne(any()))
                .thenReturn(malfoy);
        Mockito.when(facultyService.findOne(any()))
                .thenReturn(slytherin);

        Student student = mapper.fromDto(dto);

        assertThat(student.getId()).isEqualTo(malfoy.getId());
        assertThat(student.getName()).isEqualTo(malfoy.getName());
        assertThat(student.getAge()).isEqualTo(malfoy.getAge());
        assertThat(student.getFaculty().getName()).isEqualTo(slytherin.getName());
    }

    @Test
    void studentIsNull() {
        Throwable thrown = catchThrowable(() -> mapper.fromDto(null));

        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }
}
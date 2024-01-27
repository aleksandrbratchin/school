package ru.hogwarts.school.mapper.student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.hogwarts.school.dto.student.StudentAddRequestDto;
import ru.hogwarts.school.model.faculty.Faculty;
import ru.hogwarts.school.model.student.Student;
import ru.hogwarts.school.services.impl.FacultyServiceImpl;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class StudentAddRequestMapperTest {
    private StudentAddRequestMapper mapper;
    @Mock
    private FacultyServiceImpl facultyService;

    @BeforeEach
    void setUp() {
        mapper = new StudentAddRequestMapper(facultyService);
    }

    @Test
    void dtoIsCorrect() {
        StudentAddRequestDto malfoy = new StudentAddRequestDto("Драко Люциус Малфой", 12, UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"));
        Mockito.when(facultyService.findOne(any()))
                .thenReturn(
                        new Faculty(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Слизерин", "зелёный, серебряный", new ArrayList<>())
                );

        Student dto = mapper.fromDto(malfoy);

        assertThat(dto.getName()).isEqualTo(malfoy.name());
        assertThat(dto.getAge()).isEqualTo(malfoy.age());
        assertThat(dto.getFaculty().getName()).isEqualTo("Слизерин");
    }

    @Test
    void dtoIsNull() {
        Throwable thrown = catchThrowable(() -> mapper.fromDto(null));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }
}
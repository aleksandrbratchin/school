package ru.hogwarts.school.mapper.student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.hogwarts.school.dto.faculty.FacultyInfoDto;
import ru.hogwarts.school.dto.student.StudentResponseDto;
import ru.hogwarts.school.mapper.faculty.FacultyInfoMapper;
import ru.hogwarts.school.model.faculty.Faculty;
import ru.hogwarts.school.model.student.Student;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class StudentResponseMapperTest {
    private StudentResponseMapper mapper;
    @Mock
    private FacultyInfoMapper facultyInfoMapper;

    @BeforeEach
    void setUp() {
        mapper = new StudentResponseMapper(facultyInfoMapper);
    }

    @Test
    void studentIsCorrect() {
        FacultyInfoDto slytherin = new FacultyInfoDto(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Слизерин", "зелёный, серебряный");
        Student malfoy = new Student(
                UUID.fromString("4fa85f64-5717-4562-b3fc-2c963f66afa6"),
                "Драко Люциус Малфой",
                12,
                new Faculty(
                        UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"),
                        "Слизерин",
                        "зелёный, серебряный",
                        new ArrayList<>()
                )
        );
        Mockito.when(facultyInfoMapper.toDto(any()))
                .thenReturn(slytherin);

        StudentResponseDto dto = mapper.toDto(malfoy);

        assertThat(dto.id()).isEqualTo(malfoy.getId());
        assertThat(dto.name()).isEqualTo(malfoy.getName());
        assertThat(dto.age()).isEqualTo(malfoy.getAge());
        assertThat(dto.faculty().name()).isEqualTo(malfoy.getFaculty().getName());
    }

    @Test
    void studentIsCorrectFacultyNull() {
        FacultyInfoDto slytherin = new FacultyInfoDto(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Слизерин", "зелёный, серебряный");
        Student malfoy = new Student(
                UUID.fromString("4fa85f64-5717-4562-b3fc-2c963f66afa6"),
                "Драко Люциус Малфой",
                12,
                null
        );
        Mockito.when(facultyInfoMapper.toDto(any()))
                .thenReturn(slytherin);

        StudentResponseDto dto = mapper.toDto(malfoy);

        assertThat(dto.id()).isEqualTo(malfoy.getId());
        assertThat(dto.name()).isEqualTo(malfoy.getName());
        assertThat(dto.age()).isEqualTo(malfoy.getAge());
        assertThat(dto.faculty()).isNull();
    }

    @Test
    void studentIsNull() {
        Throwable thrown = catchThrowable(() -> mapper.toDto(null));

        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }
}
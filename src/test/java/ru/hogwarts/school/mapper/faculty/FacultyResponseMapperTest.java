package ru.hogwarts.school.mapper.faculty;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.hogwarts.school.dto.faculty.FacultyResponseDto;
import ru.hogwarts.school.mapper.student.StudentResponseMapper;
import ru.hogwarts.school.model.faculty.Faculty;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class FacultyResponseMapperTest {
    private FacultyResponseMapper mapper;
    @Mock
    private StudentResponseMapper studentResponseMapper;
    @BeforeEach
    void setUp() {
        mapper = new FacultyResponseMapper(studentResponseMapper);
    }
    @Test
    void facultyIsCorrect() {
        Faculty slytherin = new Faculty(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Слизерин", "зелёный, серебряный", new ArrayList<>());
        Mockito.when(studentResponseMapper.toDto(any()))
                .thenReturn(null);

        FacultyResponseDto dto = mapper.toDto(slytherin);

        assertThat(dto.name()).isEqualTo(slytherin.getName());
        assertThat(dto.color()).isEqualTo(slytherin.getColor());
        assertThat(dto.id()).isEqualTo(slytherin.getId());
        assertThat(dto.student().size()).isEqualTo(0);
    }
    @Test
    void facultyIsNull() {
        Throwable thrown = catchThrowable(() -> mapper.toDto(null));

        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }
}
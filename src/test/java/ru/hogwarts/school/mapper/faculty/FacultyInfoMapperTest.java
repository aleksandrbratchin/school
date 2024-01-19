package ru.hogwarts.school.mapper.faculty;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.hogwarts.school.dto.faculty.FacultyInfoDto;
import ru.hogwarts.school.model.faculty.Faculty;
import ru.hogwarts.school.services.impl.FacultyServiceImpl;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class FacultyInfoMapperTest {
    private FacultyInfoMapper mapper;
    @Mock
    private FacultyServiceImpl facultyService;

    @BeforeEach
    void setUp() {
        mapper = new FacultyInfoMapper(facultyService);
    }

    @Test
    void dtoIsCorrect() {
        Faculty slytherin = new Faculty(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Слизерин", "зелёный, серебряный", new ArrayList<>());
        FacultyInfoDto dto = new FacultyInfoDto(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Слизерин", "зелёный, серебряный");
        Mockito.when(facultyService.findOne(any()))
                .thenReturn(slytherin);

        Faculty faculty = mapper.fromDto(dto);

        assertThat(faculty.getId()).isEqualTo(dto.id());
        assertThat(faculty.getName()).isEqualTo(dto.name());
        assertThat(faculty.getColor()).isEqualTo(dto.color());
        assertThat(faculty.getStudents().size()).isEqualTo(0);

    }

    @Test
    void dtoIsNull() {
        Throwable thrown = catchThrowable(() -> mapper.fromDto(null));

        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }

    @Test
    void facultyIsCorrect() {
        Faculty slytherin = new Faculty(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Слизерин", "зелёный, серебряный", new ArrayList<>());

        FacultyInfoDto dto = mapper.toDto(slytherin);

        assertThat(dto.name()).isEqualTo(slytherin.getName());
        assertThat(dto.color()).isEqualTo(slytherin.getColor());
        assertThat(dto.id()).isEqualTo(slytherin.getId());
    }

    @Test
    void facultyIsNull() {
        Throwable thrown = catchThrowable(() -> mapper.toDto(null));

        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }

}
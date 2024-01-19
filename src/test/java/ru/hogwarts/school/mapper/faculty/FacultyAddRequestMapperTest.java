package ru.hogwarts.school.mapper.faculty;

import org.junit.jupiter.api.Test;
import ru.hogwarts.school.dto.faculty.FacultyAddRequestDto;
import ru.hogwarts.school.model.faculty.Faculty;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class FacultyAddRequestMapperTest {
    private final FacultyAddRequestMapper mapper = new FacultyAddRequestMapper();
    @Test
    void dtoIsCorrect() {
        FacultyAddRequestDto dto = new FacultyAddRequestDto("Слизерин", "зелёный, серебряный");

        Faculty faculty = mapper.fromDto(dto);

        assertThat(faculty.getName()).isEqualTo(dto.name());
        assertThat(faculty.getColor()).isEqualTo(dto.color());
        assertThat(faculty.getId()).isNull();
        assertThat(faculty.getStudents().size()).isEqualTo(0);

    }
    @Test
    void dtoIsNull() {
        Throwable thrown = catchThrowable(() -> mapper.fromDto(null));

        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }
}
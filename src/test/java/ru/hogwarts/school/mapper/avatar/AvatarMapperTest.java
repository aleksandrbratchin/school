package ru.hogwarts.school.mapper.avatar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hogwarts.school.dto.avatar.AvatarDto;
import ru.hogwarts.school.mapper.faculty.FacultyInfoMapper;
import ru.hogwarts.school.model.avatar.Avatar;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AvatarMapperTest {

    private AvatarMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new AvatarMapper();
    }
    @Test
    void toDto() {
        Avatar avatar = new Avatar(UUID.randomUUID(), "C:\\IMG1.JPG", 0, "image/jpeg", new byte[0], null);

        AvatarDto dto = mapper.toDto(avatar);

        assertThat(dto.id()).isNotNull();
        assertThat(dto.filePath()).isEqualTo(avatar.getFilePath());
        assertThat(dto.mediaType()).isEqualTo(avatar.getMediaType());
        assertThat(dto.fileSize()).isEqualTo(avatar.getFileSize());
    }
}
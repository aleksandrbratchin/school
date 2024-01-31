package ru.hogwarts.school.mapper.avatar;

import org.springframework.stereotype.Component;
import ru.hogwarts.school.dto.avatar.AvatarDto;
import ru.hogwarts.school.mapper.ResponseMapper;
import ru.hogwarts.school.model.avatar.Avatar;

@Component
public class AvatarMapper implements ResponseMapper<Avatar, AvatarDto> {
    @Override
    public AvatarDto toDto(Avatar obj) {
        return new AvatarDto(
                obj.getId(),
                obj.getFilePath(),
                obj.getFileSize(),
                obj.getMediaType()
        );
    }
}

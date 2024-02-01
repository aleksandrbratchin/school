package ru.hogwarts.school.dto.avatar;

import java.util.UUID;

public record AvatarDto(
        UUID id,
        String filePath,
        long fileSize,
        String mediaType) {}

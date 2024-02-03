package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.dto.avatar.AvatarDto;
import ru.hogwarts.school.mapper.ResponseMapper;
import ru.hogwarts.school.mapper.avatar.AvatarMapper;
import ru.hogwarts.school.model.avatar.Avatar;
import ru.hogwarts.school.services.impl.AvatarService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("student/avatar")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(@Qualifier("avatarService") AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/{studentId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAvatar(
            @PathVariable UUID studentId,
            @RequestParam MultipartFile avatar
    ) {
        try {
            avatarService.uploadAvatar(studentId, avatar);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/{studentId}/avatar-from-db")
    public ResponseEntity<?> downloadAvatarFromDB(
            @PathVariable UUID studentId
    ) {
        try {
            Avatar avatar = avatarService.findByStudentId(studentId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
            headers.setContentLength(avatar.getData().length);
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping(value = "/{studentId}/avatar-from-file")
    public ResponseEntity<?> downloadAvatarFromFile(
            @PathVariable UUID studentId
    ) {
        try {
            Avatar avatar = avatarService.findByStudentId(studentId);
            Resource file = avatarService.getAvatarFromFile(avatar);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
            responseHeaders.setContentLength((int) avatar.getFileSize());
            return new ResponseEntity<>(file, responseHeaders, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/All")
    public ResponseEntity<?> All(
            @RequestParam("page") Integer pageNumber,
            @RequestParam("size") Integer pageSize
    ) {
        try {
            List<AvatarDto> avatars = avatarService.findAll(pageNumber, pageSize);
            return ResponseEntity.ok(
                    avatars
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}

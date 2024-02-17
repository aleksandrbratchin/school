package ru.hogwarts.school.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.dto.avatar.AvatarDto;
import ru.hogwarts.school.mapper.ResponseMapper;
import ru.hogwarts.school.mapper.avatar.AvatarMapper;
import ru.hogwarts.school.model.avatar.Avatar;
import ru.hogwarts.school.model.student.Student;
import ru.hogwarts.school.repositories.AvatarRepository;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.services.api.StorageService;
import ru.hogwarts.school.services.impl.filestorage.FileSystemStorageService;
import ru.hogwarts.school.specifications.AvatarSpecification;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;


@Service
public class AvatarService {

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;
    private final StorageService fileSystemStorageService;
    private final ResponseMapper<Avatar, AvatarDto> avatarMapper;
    private Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    public AvatarService(
            StudentRepository studentRepository,
            AvatarRepository avatarRepository,
            FileSystemStorageService fileSystemStorageService,
            AvatarMapper avatarMapper
    ) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
        this.fileSystemStorageService = fileSystemStorageService;
        this.avatarMapper = avatarMapper;
    }

    public void uploadAvatar(UUID studentId, MultipartFile avatarFile) throws IOException {
        logger.info("Был вызван метод сохранения аватара в файл и БД");
        Student student = studentRepository.getReferenceById(studentId);
        Path filePath = Path.of(avatarsDir, studentId.toString(), Objects.requireNonNull(avatarFile.getOriginalFilename())).normalize().toAbsolutePath();
        fileSystemStorageService.deleteAll(filePath.getParent());
        fileSystemStorageService.save(filePath, avatarFile);
        Avatar avatar = avatarRepository.findOne(
                        AvatarSpecification.findByIdStudent(studentId)
                )
                .orElse(
                        Avatar.builder()
                                .student(student)
                                .build()
                );
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());
        avatarRepository.save(avatar);
    }

    public Resource getAvatarFromFile(Avatar avatar) {
        logger.info("Был вызван метод получения аватара из файла");
        Optional.ofNullable(avatar).orElseThrow(
                () -> {
                    String msq = "Ошибка в методе получения аватара из файла. avatar = null";
                    logger.error(msq);
                    return new IllegalArgumentException(msq);
                }
        );
        Path filePath = Path.of(avatar.getFilePath());
        return fileSystemStorageService.loadAsResource(filePath);
    }

    public Avatar findByStudentId(UUID studentId) {
        logger.info("Был вызван метод получения аватара из БД по id студента");
        Optional.ofNullable(studentId).orElseThrow(
                () -> {
                    String msq = "Ошибка в методе получения аватара из из БД по id студента. studentId = null";
                    logger.error(msq);
                    return new IllegalArgumentException(msq);
                }
        );
        return avatarRepository.findOne(AvatarSpecification.findByIdStudent(studentId))
                .orElseThrow(() -> {
                    String msq = "Ошибка в методе получения аватара из из БД по id студента. Не удалось найти аватар по studentId = " + studentId;
                    logger.error(msq);
                    return new NoSuchElementException(msq);
                });
    }

    public Avatar findById(UUID id) {
        logger.info("Был вызван метод получения аватара из БД по id");
        Optional.ofNullable(id).orElseThrow(
                () -> {
                    String msq = "Ошибка в методе получения аватара из из БД по id. studentId = null";
                    logger.error(msq);
                    return new IllegalArgumentException(msq);
                }
        );
        return avatarRepository.findOne(AvatarSpecification.idEqual(id))
                .orElseThrow(
                        () -> {
                            String msq = "Ошибка в методе получения аватара из из БД по id. Не удалось найти аватар по id = " + id;
                            logger.error(msq);
                            return new NoSuchElementException(msq);
                        }
                );
    }

    public List<AvatarDto> findAll(Integer pageNumber, Integer pageSize) {
        logger.info("Был вызван метод получения аватаров постранично");
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent().stream()
                .map(avatarMapper::toDto)
                .toList();
    }
}

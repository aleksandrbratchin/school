package ru.hogwarts.school.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.hogwarts.school.model.avatar.Avatar;
import ru.hogwarts.school.repositories.AvatarRepository;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.services.impl.filestorage.FileSystemStorageService;
import ru.hogwarts.school.specifications.AvatarSpecification;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class AvatarServiceTest {
    private AvatarService service;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private AvatarRepository avatarRepository;
    @Mock
    private FileSystemStorageService fileSystemStorageService;

    @BeforeEach
    void setUp() {
        service = new AvatarService(studentRepository, avatarRepository, fileSystemStorageService);
    }

    @Nested
    class Success {
        @Test
        void findById() {
            Mockito.when(avatarRepository.findOne(any(Specification.class)))
                    .thenReturn(Optional.of(
                            Avatar.builder()
                                    .id(UUID.randomUUID())
                                    .build()
                    ));

            Avatar result = service.findById(UUID.randomUUID());

            assertThat(result).isNotNull();
        }

        @Test
        void findByStudentId() {
            Mockito.when(avatarRepository.findOne(any(Specification.class)))
                    .thenReturn(Optional.of(
                            Avatar.builder()
                                    .id(UUID.randomUUID())
                                    .build()
                    ));

            Avatar result = avatarRepository.findOne(AvatarSpecification.findByIdStudent(UUID.randomUUID())).get();

            assertThat(result).isNotNull();
        }
    }

    @Nested
    class Error {
        @Test
        void findById() {

            Throwable thrown = catchThrowable(() -> service.findById(null));

            assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void findByStudentId() {

            Throwable thrown = catchThrowable(() -> service.findByStudentId(null));

            assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
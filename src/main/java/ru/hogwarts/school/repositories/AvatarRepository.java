package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.hogwarts.school.model.avatar.Avatar;

import java.util.UUID;

public interface AvatarRepository extends JpaRepository<Avatar, UUID>, JpaSpecificationExecutor<Avatar> {
}

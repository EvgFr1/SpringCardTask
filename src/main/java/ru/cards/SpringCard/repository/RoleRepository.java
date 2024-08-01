package ru.cards.SpringCard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cards.SpringCard.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}

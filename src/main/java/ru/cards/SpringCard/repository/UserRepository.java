package ru.cards.SpringCard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cards.SpringCard.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

package com.example.cardealershipwebsite.repository;

import com.example.cardealershipwebsite.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** UserRep. */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Здесь можно добавлять дополнительные запросы, если нужно
    // Например, поиск пользователя по имени или email
    /** Empty now. */
    Optional<User> findByEmail(String email);
}

package com.example.cardealershipwebsite.repository;

import com.example.cardealershipwebsite.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** UserRep. */
@Repository
public interface    UserRepository extends JpaRepository<User, Long> {
    // Здесь можно добавлять дополнительные запросы, если нужно
    // Например, поиск пользователя по имени или email

    /** Поиск по айди. */
    @Query("SELECT c.id FROM User u JOIN u.orders c WHERE u.id = :userId")
    List<Long> findUserOrderCarIds(@Param("userId") Long userId);
}

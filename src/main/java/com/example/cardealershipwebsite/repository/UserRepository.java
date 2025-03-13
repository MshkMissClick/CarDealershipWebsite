package com.example.cardealershipwebsite.repository;

import com.example.cardealershipwebsite.model.Car;
import com.example.cardealershipwebsite.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** UserRep. */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /** Найти всех у кого у в избранном. */
    List<User> findAllByFavoritesContains(Car car);
}

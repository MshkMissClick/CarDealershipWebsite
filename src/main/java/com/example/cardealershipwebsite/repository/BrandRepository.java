package com.example.cardealershipwebsite.repository;

import com.example.cardealershipwebsite.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Brand repos. */
@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    /** Find. */
    Brand findByName(String name);
}

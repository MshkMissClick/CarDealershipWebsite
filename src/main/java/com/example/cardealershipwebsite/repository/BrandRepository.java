package com.example.cardealershipwebsite.repository;

import com.example.cardealershipwebsite.model.Brand;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Brand repos. */
@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Brand findByName(String name);

    // JPQL
    @Query("SELECT b FROM Brand b WHERE b.name LIKE %:name%")
    List<Brand> findBrandsByNameContains(@Param("name") String name);

    // Native Query
    @Query(value = "SELECT * FROM brands WHERE name ILIKE %:name%", nativeQuery = true)
    List<Brand> findBrandsNative(@Param("name") String name);
}

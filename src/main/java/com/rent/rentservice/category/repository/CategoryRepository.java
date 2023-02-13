package com.rent.rentservice.category.repository;

import com.rent.rentservice.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryValue(String value);
}

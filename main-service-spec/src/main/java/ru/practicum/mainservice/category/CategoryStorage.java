package ru.practicum.mainservice.category;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryStorage extends JpaRepository<Category, Long> {

    @Query("select c from Category c")
    List<Category> findAllCategories(Pageable pageable);
}

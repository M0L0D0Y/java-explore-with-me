package ru.practicum.mainservice.compilation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompilationStorage extends JpaRepository<Compilation, Long> {

    @Query("select c from Compilation c where c.pinned = ?1")
    List<Compilation> findAllCompilations(boolean pinned, Pageable pageable);

    @Query("select c from Compilation c")
    List<Compilation> findAllCompilations(Pageable pageable);
}

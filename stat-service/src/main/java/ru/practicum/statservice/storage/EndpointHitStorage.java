package ru.practicum.statservice.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.statservice.model.EndpointHit;

@Repository
public interface EndpointHitStorage extends JpaRepository<EndpointHit, Long> {
}

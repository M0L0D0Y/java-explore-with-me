package ru.practicum.ewm.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.EndpointHit;

@Repository
public interface EndpointHitStorage extends JpaRepository<EndpointHit, Long> {
}

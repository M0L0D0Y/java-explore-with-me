package ru.practicum.ewm.endpiont;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EndpointHitStorage extends JpaRepository<EndpointHit, Long> {
}

package ru.practicum.mainservice.participationRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationRequestStorage extends JpaRepository<ParticipationRequest, Long> {
    @Query("select r from ParticipationRequest r  where r.event.id = ?2 and r.requester.id = ?1")
    Optional<ParticipationRequest> findByEventAndUserId(long userId, long eventId);

    List<ParticipationRequest> findByRequesterId(long userId);

    List<ParticipationRequest> findByEventId(long eventId);

}

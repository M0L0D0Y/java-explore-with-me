package ru.practicum.ewm.participationRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.event.Event;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ParticipationRequestStorage extends JpaRepository<ParticipationRequest, Long> {
    @Query("select r from ParticipationRequest r where r.event.id = ?2 and r.requester.id = ?1")
    Optional<ParticipationRequest> findByEventAndUserId(long userId, long eventId);

    List<ParticipationRequest> findByRequesterId(long userId);

    List<ParticipationRequest> findByEventId(long eventId);

    @Query("select distinct r.event from ParticipationRequest r " +
            "where r.requester.id = ?1 " +
            "and r.status =?2")
    List<Event> findEventsByFriendId(long userId, String status);

    @Query("select distinct r.event from ParticipationRequest r " +
            "where r.requester.id in :userIds " +
            "and r.status in :status")
    List<Event> findEventsAllFriends(String status, List<Long> userIds);

}

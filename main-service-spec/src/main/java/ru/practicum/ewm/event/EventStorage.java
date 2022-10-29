package ru.practicum.ewm.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface EventStorage extends JpaRepository<Event, Long> {

    List<Event> findByInitiatorId(long id, Pageable pageable);

    @Query("select e from Event e where e.id in :eventsId")
    Set<Event> findEventsForCompilation(List<Long> eventsId);

    List<Event> functionName(long userId, long friendId, String status);

}

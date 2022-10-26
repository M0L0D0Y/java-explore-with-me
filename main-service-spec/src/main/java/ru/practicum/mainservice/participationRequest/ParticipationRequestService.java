package ru.practicum.mainservice.participationRequest;

import java.util.List;

public interface ParticipationRequestService {
    ParticipationRequest addRequestForEvent(long userId, long eventId);

    List<ParticipationRequest> getRequestInfoForUser(long userId);

    ParticipationRequest cancelRequestForEvent(long userId, long requestsId);
}

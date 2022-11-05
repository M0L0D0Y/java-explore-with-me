package ru.practicum.ewm.participationRequest.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.common.CommonMethods;
import ru.practicum.ewm.participationRequest.ParticipationRequest;

@Service
public class ParticipationRequestMapper {
    private final CommonMethods commonMethods;

    @Autowired
    public ParticipationRequestMapper(CommonMethods commonMethods) {
        this.commonMethods = commonMethods;
    }

    public ParticipationRequestDto toParRequestDto(ParticipationRequest parRequest) {
        ParticipationRequestDto parRequestDto = new ParticipationRequestDto();
        String created = commonMethods.toString(parRequest.getCreated());
        parRequestDto.setCreated(created);
        parRequestDto.setRequester(parRequest.getRequester().getId());
        parRequestDto.setEvent(parRequest.getEvent().getId());
        parRequestDto.setStatus(parRequest.getStatus());
        parRequestDto.setId(parRequest.getId());
        return parRequestDto;
    }
}

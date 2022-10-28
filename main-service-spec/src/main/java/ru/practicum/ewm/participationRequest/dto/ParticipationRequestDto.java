package ru.practicum.ewm.participationRequest.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ParticipationRequestDto {
    @NotBlank(message = "Нет даты создания")
    @Size(max = 25)
    private String created;
    private Long event;
    private Long id;
    private Long requester;
    @NotBlank(message = "Нет статуса")
    @Size(max = 10)
    private String status;
}

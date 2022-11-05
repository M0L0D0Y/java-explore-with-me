package ru.practicum.ewm.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ViewStats {
    @NotBlank
    @Size(max = 100, message = "Превышен лимит символов")
    private String app;
    @NotBlank
    @Size(max = 50, message = "Превышен лимит символов")
    private String uri;
    @NotNull
    private Long hits;
}

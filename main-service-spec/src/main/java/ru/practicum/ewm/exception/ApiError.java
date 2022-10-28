package ru.practicum.ewm.exception;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ApiError {
    private List<String> errors;
    @NotBlank
    private String message;
    @NotBlank
    private String reason;
    @NotBlank
    private String status;
    @NotBlank
    private String timestamp;

    public ApiError(List<String> errors, String message, String reason, String status, String timestamp) {
        this.errors = errors;
        this.message = message;
        this.reason = reason;
        this.status = status;
        this.timestamp = timestamp;
    }
}

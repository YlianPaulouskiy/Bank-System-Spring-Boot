package by.aston.bank.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;


import java.math.BigDecimal;

public record AccountWriteDto(
        @Length(max = 15, min = 1)
        String number,
        @NotNull(message = "Cash can't be null")
        BigDecimal cash,
        @NotNull(message = "Bank id can't be null")
        @Positive(message = "Bank id can be positive")
        Long bankId,
        @NotNull(message = "User id can't be null")
        @Positive(message = "User id can be positive")
        Long userId) {
}

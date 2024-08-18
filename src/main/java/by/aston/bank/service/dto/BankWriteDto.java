package by.aston.bank.service.dto;

import jakarta.validation.constraints.*;

public record BankWriteDto(
        @NotNull(message = "Title is require field")
        @NotBlank(message = "Title can't be empty")
        String title) {
}

package by.aston.bank.service.dto;

import java.util.List;

public record BankReadDto(
        Long id,
        String title,
        List<UserDto> users) {
}

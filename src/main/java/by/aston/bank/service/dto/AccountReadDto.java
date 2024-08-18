package by.aston.bank.service.dto;

import java.math.BigDecimal;

public record AccountReadDto(
        Long id,
        String number,
        BigDecimal cash,
        BankUserDto bank,
        UserDto user) {
}

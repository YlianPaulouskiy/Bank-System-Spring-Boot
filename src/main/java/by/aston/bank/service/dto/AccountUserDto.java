package by.aston.bank.service.dto;

import java.math.BigDecimal;

public record AccountUserDto(
        Long id,
        String number,
        BigDecimal cash,
        BankUserDto bank) {
}

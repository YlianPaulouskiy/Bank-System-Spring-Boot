package by.aston.bank.util;

import by.aston.bank.model.*;
import by.aston.bank.service.dto.*;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public final class CommonTestData {

    public static Account createAccount() {
        return new Account(1L,
                "12376589567",
                BigDecimal.valueOf(70.77),
                createUser(),
                createBank());
    }

    public static User createUser() {
        return new Client(1L,
                "Maxim",
                "Yarosh",
                'C',
                new ArrayList<>(),
                new ArrayList<>());
    }

    public static Bank createBank() {
        return new Bank(1L,
                "Bank of America",
                new ArrayList<>(),
                new ArrayList<>());
    }

    public static Client createClient() {
        return new Client(1L,
                "Maxim",
                "Yarosh",
                'C',
                new ArrayList<>(),
                new ArrayList<>());
    }

    public static UserWriteDto createClientWriteDto() {
        return new UserWriteDto("Maxim",
                "Yarosh",
                null,
                null);
    }

    public static UserWriteDto createManagerWriteDto() {
        return new UserWriteDto("Maxim",
                "Yarosh",
                "2024-07-16",
                null);
    }

    public static Manager createManager() {
        return new Manager(1L,
                "Maxim",
                "Yarosh",
                'M',
                "2024-07-16",
                null);
    }

    public static BankReadDto createBankReadDto() {
        return new BankReadDto(1L,
                "Bank of America",
                List.of(createUserDto()));
    }

    public static BankWriteDto createBankWriteDto() {
        return new BankWriteDto("Bank of America");
    }

    public static AccountReadDto createAccountReadDto() {
        return new AccountReadDto(1L,
                "12376589567",
                BigDecimal.valueOf(70.77),
                createBankUserDto(),
                createUserDto());
    }

    public static BankUserDto createBankUserDto() {
        return new BankUserDto(1L,
                "Bank of America");
    }

    public static UserDto createUserDto() {
        return new UserCReadDto(1L,
                "Maxim",
                "Yarosh",
                createAccountListUserDto());
    }

    public static List<AccountUserDto> createAccountListUserDto() {
        return List.of(createAccountUserDto());
    }

    public static AccountUserDto createAccountUserDto() {
        return new AccountUserDto(1L,
                "12376589567",
                BigDecimal.valueOf(70.77),
                createBankUserDto());
    }

    public static AccountWriteDto createAccountWriteDto() {
        return new AccountWriteDto("12376589567",
                BigDecimal.valueOf(70.77),
                1L,
                1L);
    }

}

package by.aston.bank.service.mapper;

import by.aston.bank.model.*;
import by.aston.bank.service.dto.*;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    public abstract Manager toManagerEntity(UserWriteDto writeDto);

    public abstract Client toClientEntity(UserWriteDto writeDto);

    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }
        Long id = null;
        String name = null;
        String lastName = null;

        id = user.getId();
        name = user.getName();
        lastName = user.getLastName();

        UserDto userDto = new UserDto(id, name, lastName);

        if (user instanceof Manager) {
            userDto = new UserMReadDto(id, name, lastName, ((Manager) user).getStartWork(), ((Manager) user).getEndWork());
        } else if (user instanceof Client) {
            List<AccountUserDto> accounts = ((Client) user).getAccounts().stream().map(this::accountUserDto).toList();
            userDto = new UserCReadDto(id, name, lastName, accounts);
        }

        return userDto;
    }

    private AccountUserDto accountUserDto(Account account) {
        if (account == null) {
            return null;
        }

        Long id = null;
        String number = null;
        BigDecimal cash = null;
        BankUserDto bank = null;

        id = account.getId();
        number = account.getNumber();
        cash = account.getCash();
        bank = bankUserDto(account.getBank());

        return new AccountUserDto(id, number, cash, bank);
    }

    private BankUserDto bankUserDto(Bank bank) {
        if (bank == null) {
            return null;
        }

        Long id = null;
        String title = null;

        id = bank.getId();
        title = bank.getTitle();

        return new BankUserDto(id, title);
    }

}

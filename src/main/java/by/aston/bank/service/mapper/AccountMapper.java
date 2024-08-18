package by.aston.bank.service.mapper;

import by.aston.bank.model.Account;
import by.aston.bank.service.dto.AccountReadDto;
import by.aston.bank.service.dto.AccountUserDto;
import by.aston.bank.service.dto.AccountWriteDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, BankMapper.class})
public interface AccountMapper {

    Account toEntity(AccountWriteDto writeDto);

    AccountReadDto toReadDto(Account account);

    AccountUserDto toAccountDto(Account account);

}

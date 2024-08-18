package by.aston.bank.service.mapper;

import by.aston.bank.model.Bank;
import by.aston.bank.service.dto.BankReadDto;
import by.aston.bank.service.dto.BankUserDto;
import by.aston.bank.service.dto.BankWriteDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, AccountMapper.class})
public interface BankMapper {

    Bank toEntity(BankWriteDto writeDto);

    BankReadDto toReadDto(Bank bank);

    BankUserDto toUserDto(Bank bank);

}

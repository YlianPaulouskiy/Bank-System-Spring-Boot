package by.aston.bank.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class UserCReadDto extends UserDto {

    private List<AccountUserDto> accounts;

    public UserCReadDto(Long id, String name, String lastName, List<AccountUserDto> accounts) {
        super(id, name, lastName);
        this.accounts = accounts;
    }

}


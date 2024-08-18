package by.aston.bank.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {

    private final Long id;
    private final String name;
    private final String lastName;

}

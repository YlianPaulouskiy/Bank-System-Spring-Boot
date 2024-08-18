package by.aston.bank.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserMReadDto extends UserDto {

    private String startWork;
    private String endWork;

    public UserMReadDto(Long id, String name, String lastName, String startWork, String endWork) {
        super(id, name, lastName);
        this.startWork = startWork;
        this.endWork = endWork;
    }

}

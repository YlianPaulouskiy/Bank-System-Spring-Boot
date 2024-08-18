package by.aston.bank.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@DiscriminatorValue("C")
@EqualsAndHashCode(callSuper = true)
public class Client extends User {

    public Client(Long id, String name, String lastName, Character userType, List<Account> accounts, List<Bank> banks) {
        super(id, name, lastName, userType);
        this.accounts = accounts;
        this.banks = banks;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Account> accounts = new ArrayList<>();


    @ManyToMany(mappedBy = "users")
    private List<Bank> banks = new ArrayList<>();

}

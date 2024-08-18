package by.aston.bank.model;

import by.aston.bank.model.converter.DateConverter;
import lombok.*;
import jakarta.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@DiscriminatorValue("M")
@EqualsAndHashCode(callSuper = true)
public class Manager extends User {

    @Convert(converter = DateConverter.class)
    @Column(name = "start_work", nullable = false)
    private String startWork;
    @Convert(converter = DateConverter.class)
    @Column(name = "end_work")
    private String endWork;

    public Manager(Long id, String name, String lastName, Character userType, String startWork, String endWork) {
        super(id, name, lastName, userType);
        this.startWork = startWork;
        this.endWork = endWork;
    }
}

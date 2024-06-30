package co.develhope.crudTest.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Column
    @GeneratedValue
    @Id
    private long id;
    @Column
    private String name;
    @Column
    private String surname;
    @Column
    private boolean isWorking;
}

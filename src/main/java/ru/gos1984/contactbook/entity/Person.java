package ru.gos1984.contactbook.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "person")
@Data
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    @Getter
    @Setter
    private String firstName;

    @Column(name = "last_name")
    @Getter
    @Setter
    private String lastName;

    @Column(name = "phone")
    @Getter
    @Setter
    private String phone;

    @Column(name = "email")
    @Getter
    @Setter
    private String email;

    @Column(name = "avatar")
    @Getter
    @Setter
    private String avatar;

    public Person() {}

    public Person(String firstName, String lastName, String phone, String email, String avatar) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.avatar = avatar;
    }

}

package com.student.management.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "STUDENT")
@Setter
@Getter
public class Student extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @Size(min = 1, max = 50)
    @Column(name="USERNAME", length = 50, unique = true, nullable = false)
    private String username;

    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "PASSWORD", length = 60, nullable = false)
    private String password;

    @Size(max = 50)
    @Column(name = "FIRST_NAME", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "LAST_NAME", length = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    private String email;

    public Student(Long id) {
        super(id);
    }

    public Student() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student student)) return false;

        return id.equals(student.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

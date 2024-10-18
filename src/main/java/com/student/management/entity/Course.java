package com.student.management.entity;


import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "COURSE")
@Setter
@Getter
public class Course extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "INSTRUCTOR")
    private String instructor;

    @Column(name = "DURATION")
    private Integer duration;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @OneToMany(mappedBy = "course")
    private Set<Schedule> schedules = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "STUDENT_COURSES",

            joinColumns = @JoinColumn(name = "STUDENT_ID"),
            inverseJoinColumns = @JoinColumn(name = "COURSE_ID")
    )
    private Set<Student> students = new HashSet<>();

}

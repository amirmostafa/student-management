package com.student.management.entity;

import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "SCHEDULE")
@Setter
@Getter
public class Schedule extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "SCHEDULE_DATE", nullable = false)
    private LocalDate date;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "LOCATION")
    private String location;


    @Column(name = "DURATION")
    private Integer duration;

    @ManyToOne
    private Course course;
}

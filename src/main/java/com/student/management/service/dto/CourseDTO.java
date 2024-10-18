package com.student.management.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class CourseDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private String instructor;

    private Integer duration;

    private LocalDate startDate;

    private LocalDate endDate;
}

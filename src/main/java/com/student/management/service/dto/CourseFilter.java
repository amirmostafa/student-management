package com.student.management.service.dto;

import lombok.Data;

@Data
public class CourseFilter {
    private String name;
    private int page = 0;
    private int size = 10;
}

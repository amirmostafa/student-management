package com.student.management.service.mapper;

import com.student.management.entity.Course;
import com.student.management.service.dto.CourseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Course} and its DTO {@link CourseDTO}.
 */
@Mapper(componentModel = "spring")
public interface CourseMapper extends EntityMapper<CourseDTO, Course> {}

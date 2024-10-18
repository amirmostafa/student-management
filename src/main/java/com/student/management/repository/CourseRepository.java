package com.student.management.repository;

import com.student.management.entity.Course;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends PagingAndSortingRepository<Course, Long>, JpaSpecificationExecutor<Course> {}

package com.student.management.service;

import com.student.management.entity.Course;
import com.student.management.entity.Student;
import com.student.management.repository.CourseRepository;
import com.student.management.repository.CourseSpecification;
import com.student.management.repository.StudentRepository;
import com.student.management.security.SecurityUtils;
import com.student.management.service.dto.AbstractListModel;
import com.student.management.service.dto.CourseDTO;
import com.student.management.service.dto.CourseFilter;
import com.student.management.service.mapper.CourseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CourseService {

    private final Logger log = LoggerFactory.getLogger(CourseService.class);

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final PDFGenerationService pdfService;
    private final CourseMapper courseMapper;

    public CourseService(CourseRepository courseRepository,
                         PDFGenerationService pdfService,
                         StudentRepository studentRepository,
                         CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.pdfService = pdfService;
        this.studentRepository = studentRepository;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "courses", key = "#filter.name + '_' + #filter.page + '_' + #filter.size")
    public AbstractListModel<CourseDTO> findAll(CourseFilter filter) {
        log.debug("Request to get all Courses");
        Pageable pageRequest = PageRequest.of(filter.getPage(), filter.getSize(), Sort.Direction.ASC, "name");
        Page<CourseDTO> coursePage = courseRepository.findAll(new CourseSpecification(filter), pageRequest)
                .map(courseMapper::toDto);
        return new AbstractListModel<>(coursePage.getContent(), coursePage.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "student-courses", key = "#filter.name + '_' + #filter.page + '_' + #filter.size")
    public AbstractListModel<CourseDTO> findStudentIdCourses(CourseFilter filter) {
        log.debug("Request to get all Courses");
        Pageable pageRequest = PageRequest.of(filter.getPage(), filter.getSize(), Sort.Direction.ASC, "name");
        Page<CourseDTO> coursePage = courseRepository.findAll(new CourseSpecification(filter, true), pageRequest)
                .map(courseMapper::toDto);
        return new AbstractListModel<>(coursePage.getContent(), coursePage.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "course")
    public CourseDTO findOne(Long id) {
        log.debug("Request to get Course : {}", id);
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return courseMapper.toDto(course);
    }

    @CacheEvict(cacheNames = {"student-courses"}, allEntries = true)
    public void register(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        Student student = new Student(SecurityUtils.getCurrentUserId());
        course.getStudents().add(student);
        courseRepository.save(course);
    }

    @CacheEvict(cacheNames = {"student-courses"}, allEntries = true)
    public void cancel(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        Student student = new Student(SecurityUtils.getCurrentUserId());
        course.getStudents().remove(student);
        courseRepository.save(course);
    }

    public byte[] generateSchedule(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID"));

        List<String> schedules = course.getSchedules().stream()
                .map(schedule -> "Date: " + schedule.getDate() +
                        ", Duration: " + schedule.getDuration() +
                        " hours, Location: " + schedule.getLocation())
                .collect(Collectors.toList());

        return pdfService.generateCourseSchedulePdf(course.getName(), schedules);
    }
}

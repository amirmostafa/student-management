package com.student.management.controller;

import com.student.management.service.CourseService;
import com.student.management.service.dto.AbstractListModel;
import com.student.management.service.dto.CourseDTO;

import com.student.management.service.dto.CourseFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("")
    public ResponseEntity<AbstractListModel<CourseDTO>> getAllCourses(CourseFilter filter) {
        return ResponseEntity.ok(courseService.findAll(filter));
    }

    @GetMapping("/my-courses")
    public ResponseEntity<AbstractListModel<CourseDTO>> findUserCourses(CourseFilter filter) {
        return ResponseEntity.ok(courseService.findStudentIdCourses(filter));
    }

    @GetMapping("{id}")
    public ResponseEntity<CourseDTO> getCourse(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.findOne(id));
    }

    @GetMapping("schedule/{id}")
    public ResponseEntity<byte[]> generateSchedule(@PathVariable Long id, HttpServletResponse response) {
        byte[] bytes = courseService.generateSchedule(id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_PDF);
        httpHeaders.setContentDispositionFormData("attach", "course-schedule.pdf");
        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("register/{id}")
    public void register(@PathVariable Long id) {
       courseService.register(id);
    }

    @PostMapping("cancel/{id}")
    public void cancel(@PathVariable Long id) {
        courseService.cancel(id);
    }
}

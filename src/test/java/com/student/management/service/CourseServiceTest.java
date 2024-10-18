package com.student.management.service;

import static org.junit.jupiter.api.Assertions.*;

import com.student.management.entity.Course;
import com.student.management.entity.Schedule;
import com.student.management.entity.Student;
import com.student.management.repository.CourseRepository;
import com.student.management.repository.StudentRepository;
import com.student.management.security.SecurityUtils;
import com.student.management.service.dto.AbstractListModel;
import com.student.management.service.dto.CourseDTO;
import com.student.management.service.dto.CourseFilter;
import com.student.management.service.mapper.CourseMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private PDFGenerationService pdfService;

    @InjectMocks
    private CourseService courseService;

    public static MockedStatic<SecurityUtils> mockedStatic = mockStatic(SecurityUtils.class);

    @Test
    void testFindAll() {
        // Given
        CourseFilter filter = new CourseFilter();
        filter.setPage(0);
        filter.setSize(10);
        filter.setName("SS");

        Course course = new Course();
        course.setName("CS");
        CourseDTO courseDTO = new CourseDTO();
        Page<Course> coursePage = new PageImpl<>(List.of(course));

        when(courseRepository.findAll(any(), any(PageRequest.class))).thenReturn(coursePage);
        when(courseMapper.toDto(any(Course.class))).thenReturn(courseDTO);

        // When
        AbstractListModel<CourseDTO> result = courseService.findAll(filter);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getData().size());
        verify(courseRepository, times(1)).findAll(any(), any(PageRequest.class));
    }

    @Test
    void testFindOne() {
        // Given
        Course course = new Course();
        course.setId(1L);

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(1L);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseMapper.toDto(course)).thenReturn(courseDTO);

        // When
        var result = courseService.findOne(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void testFindOne_NotFound() {
        // Given
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class, () -> courseService.findOne(1L));
    }

    @Test
    void testRegister() {
        // Given
        Course course = new Course();
        course.setId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        mockedStatic.when(SecurityUtils::getCurrentUserId).thenReturn(100L);

        // When
        courseService.register(1L);

        // Then
        verify(courseRepository, times(1)).save(course);
        assertEquals(1, course.getStudents().size());
    }
    @Test
    void testCancel() {
        // Given
        Course course = new Course();
        course.setId(1L);
        Student student = new Student(100L);
        course.getStudents().add(student);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        mockedStatic.when(SecurityUtils::getCurrentUserId).thenReturn(100L);

        // When
        courseService.cancel(1L);

        // Then
        verify(courseRepository, times(1)).save(course);
        assertTrue(course.getStudents().isEmpty());
    }

    @Test
    void testGenerateSchedule() {
        // Given
        Course course = new Course();
        course.setId(1L);
        course.setName("Java 101");

        course.getSchedules().add(new Schedule());

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(pdfService.generateCourseSchedulePdf(anyString(), anyList())).thenReturn(new byte[0]);

        // When
        byte[] result = courseService.generateSchedule(1L);

        // Then
        assertNotNull(result);
        verify(pdfService, times(1)).generateCourseSchedulePdf(any(), anyList());
    }

    @Test
    void testGenerateSchedule_InvalidCourseId() {
        // Given
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> courseService.generateSchedule(1L));
    }
}


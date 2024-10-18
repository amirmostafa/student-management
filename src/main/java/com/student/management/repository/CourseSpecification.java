package com.student.management.repository;

import com.student.management.entity.Course;
import com.student.management.security.SecurityUtils;
import com.student.management.service.dto.CourseFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class CourseSpecification implements Specification<Course> {

	private final CourseFilter filter;
	private final boolean forStudent;

	public CourseSpecification(CourseFilter filter, boolean forStudent) {
		this.filter = filter;
		this.forStudent = forStudent;
	}

	public CourseSpecification(CourseFilter filter) {
		this(filter, false);
	}

	@Override
	public Predicate toPredicate(Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		Predicate p = cb.conjunction();

		if (StringUtils.isNotBlank(filter.getName())) {
			p.getExpressions().add(cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
		}
		if(forStudent) {
			p.getExpressions().add(cb.equal(root.join("students", JoinType.LEFT).get("id"), SecurityUtils.getCurrentUserId()));
		}
		return p;
	}

}

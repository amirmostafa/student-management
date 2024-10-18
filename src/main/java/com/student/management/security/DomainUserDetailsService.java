package com.student.management.security;

import com.student.management.repository.StudentRepository;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final StudentRepository studentRepository;

    public DomainUserDetailsService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) {
        log.debug("Authenticating {}", username);

            return studentRepository
                .findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(user.getId().toString(), user.getPassword(), Collections.emptyList()))
                .orElseThrow(() -> new UsernameNotFoundException("Student with email " + username + " was not found in the database"));
    }

}

package com.utkarsh.ed.repositories;

import com.utkarsh.ed.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmail(String email);
}

package com.utkarsh.ed.repositories;

import com.utkarsh.ed.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

	boolean existsByEmail(String email);
}

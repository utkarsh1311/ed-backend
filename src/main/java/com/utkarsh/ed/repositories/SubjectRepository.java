package com.utkarsh.ed.repositories;

import com.utkarsh.ed.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
	boolean existsByName(String name);
}

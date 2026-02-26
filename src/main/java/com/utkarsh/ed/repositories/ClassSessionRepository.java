package com.utkarsh.ed.repositories;

import com.utkarsh.ed.models.ClassSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClassSessionRepository
        extends JpaRepository<ClassSession, Long>, JpaSpecificationExecutor<ClassSession> {

    @Override
    @EntityGraph(attributePaths = {"teacher", "classSchedule", "classSchedule.student", "classSchedule.subject"})
    Page<ClassSession> findAll(Specification<ClassSession> spec, Pageable pageable);
}

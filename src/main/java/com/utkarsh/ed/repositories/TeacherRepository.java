package com.utkarsh.ed.repositories;

import com.utkarsh.ed.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    boolean existsByBusinessMail(String businessMail);
}

package com.utkarsh.ed.repositories;

import com.utkarsh.ed.models.Teacher;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    boolean existsByBusinessMail(String businessMail);

    Optional<Teacher> findByBusinessEmail(String businessMail);
}

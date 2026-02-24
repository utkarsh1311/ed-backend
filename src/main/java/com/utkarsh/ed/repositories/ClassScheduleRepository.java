package com.utkarsh.ed.repositories;

import com.utkarsh.ed.models.ClassSchedule;
import com.utkarsh.ed.models.WeekDay;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClassScheduleRepository
        extends JpaRepository<ClassSchedule, Long>, JpaSpecificationExecutor<ClassSchedule> {

    @EntityGraph(attributePaths = {"student", "teacher", "subject"})
    Optional<ClassSchedule> findById(Long id);

    @EntityGraph(attributePaths = {"student", "teacher", "subject"})
    List<ClassSchedule> findAll(Specification<ClassSchedule> spec);

    @Query("SELECT COUNT(cs) > 0 FROM ClassSchedule cs "
            + "WHERE cs.dayOfWeek = :dayOfWeek "
            + "AND cs.status = 'ACTIVE' "
            + "AND (cs.teacher.id = :teacherId OR cs.student.id = :studentId) "
            + "AND cs.startTime < :newEndTime "
            + "AND cs.endTime > :newStartTime "
            + "AND cs.id != :excludeId")
    boolean existsOverlappingSchedule(
            @Param("teacherId") Long teacherId,
            @Param("studentId") Long studentId,
            @Param("dayOfWeek") WeekDay dayOfWeek,
            @Param("newStartTime") LocalTime newStartTime,
            @Param("newEndTime") LocalTime newEndTime,
            @Param("excludeId") Long excludeId);
}

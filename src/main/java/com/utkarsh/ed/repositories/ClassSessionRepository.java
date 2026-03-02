package com.utkarsh.ed.repositories;

import com.utkarsh.ed.models.ClassSchedule;
import com.utkarsh.ed.models.ClassSession;
import com.utkarsh.ed.models.SessionStatus;
import java.time.LocalDateTime;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClassSessionRepository
        extends JpaRepository<ClassSession, Long>, JpaSpecificationExecutor<ClassSession> {

    @EntityGraph(attributePaths = {"teacher", "classSchedule", "classSchedule.student", "classSchedule.subject"})
    Page<ClassSession> findAll(Specification<ClassSession> spec, Pageable pageable);

    boolean existsByClassScheduleAndScheduledAt(ClassSchedule schedule, LocalDateTime scheduledAt);

    /**
     * Returns true if the teacher already has a non-excluded session that starts within
     * [windowStart, windowEnd), excluding the session being rescheduled itself.
     */
    @Query(
            """
            SELECT CASE WHEN COUNT(cs) > 0 THEN true ELSE false END
            FROM ClassSession cs
            WHERE cs.id <> :excludeId
              AND cs.teacher.id = :teacherId
              AND cs.status NOT IN :excludedStatuses
              AND cs.scheduledAt >= :windowStart
              AND cs.scheduledAt < :windowEnd
            """)
    boolean existsConflictForTeacher(
            @Param("excludeId") Long excludeId,
            @Param("teacherId") Long teacherId,
            @Param("excludedStatuses") Collection<SessionStatus> excludedStatuses,
            @Param("windowStart") LocalDateTime windowStart,
            @Param("windowEnd") LocalDateTime windowEnd);

    /**
     * Returns true if the student already has a non-excluded session that starts within
     * [windowStart, windowEnd), excluding the session being rescheduled itself.
     */
    @Query(
            """
            SELECT CASE WHEN COUNT(cs) > 0 THEN true ELSE false END
            FROM ClassSession cs
            WHERE cs.id <> :excludeId
              AND cs.classSchedule.student.id = :studentId
              AND cs.status NOT IN :excludedStatuses
              AND cs.scheduledAt >= :windowStart
              AND cs.scheduledAt < :windowEnd
            """)
    boolean existsConflictForStudent(
            @Param("excludeId") Long excludeId,
            @Param("studentId") Long studentId,
            @Param("excludedStatuses") Collection<SessionStatus> excludedStatuses,
            @Param("windowStart") LocalDateTime windowStart,
            @Param("windowEnd") LocalDateTime windowEnd);
}

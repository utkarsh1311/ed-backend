package com.utkarsh.ed.repositories;

import com.utkarsh.ed.dto.ClassSession.ClassSessionFilter;
import com.utkarsh.ed.models.ClassSchedule;
import com.utkarsh.ed.models.ClassSession;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class ClassSessionSpecification {

    public static Specification<ClassSession> build(ClassSessionFilter filter) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Join to ClassSchedule only if needed
            Join<ClassSession, ClassSchedule> scheduleJoin = root.join("classSchedule", JoinType.INNER);

            if (filter.teacherIds() != null && !filter.teacherIds().isEmpty()) {
                predicates.add(root.get("teacher").get("id").in(filter.teacherIds()));
            }

            if (filter.studentIds() != null && !filter.studentIds().isEmpty()) {
                predicates.add(scheduleJoin.get("student").get("id").in(filter.studentIds()));
            }

            if (filter.subjectIds() != null && !filter.subjectIds().isEmpty()) {
                predicates.add(scheduleJoin.get("subject").get("id").in(filter.subjectIds()));
            }

            // Filter by the actual day of the session's scheduledAt, NOT the schedule's
            // recurring dayOfWeek. This correctly handles rescheduled sessions that
            // moved to a different day.
            if (filter.weekdays() != null && !filter.weekdays().isEmpty()) {
                Expression<Integer> dayOfWeek =
                        cb.function("EXTRACT", Integer.class, cb.literal("DOW"), root.get("scheduledAt"));
                // PostgreSQL DOW: Sunday=0 .. Saturday=6
                // java.time.DayOfWeek: Monday=1 .. Sunday=7
                // Map Java DayOfWeek values to PostgreSQL DOW values
                List<Integer> pgDowValues = filter.weekdays().stream()
                        .map(d -> d.getValue() % 7) // Mon=1, Tue=2 ... Sat=6, Sun=0
                        .toList();
                predicates.add(dayOfWeek.in(pgDowValues));
            }

            if (filter.sessionStatuses() != null && !filter.sessionStatuses().isEmpty()) {
                predicates.add(root.get("status").in(filter.sessionStatuses()));
            }

            if (filter.scheduledFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("scheduledAt"), filter.scheduledFrom()));
            }

            if (filter.scheduledTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("scheduledAt"), filter.scheduledTo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

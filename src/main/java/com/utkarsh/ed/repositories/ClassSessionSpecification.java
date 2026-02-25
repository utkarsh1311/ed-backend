package com.utkarsh.ed.repositories;

import com.utkarsh.ed.dto.ClassSession.ClassSessionFilter;
import com.utkarsh.ed.models.ClassSchedule;
import com.utkarsh.ed.models.ClassSession;
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

            if (filter.weekdays() != null && !filter.weekdays().isEmpty()) {
                predicates.add(scheduleJoin.get("dayOfWeek").in(filter.weekdays()));
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

package com.utkarsh.ed.repositories;

import com.utkarsh.ed.dto.ClassSchedule.ClassScheduleFilter;
import com.utkarsh.ed.models.ClassSchedule;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class ClassScheduleSpecification {

    private ClassScheduleSpecification() {
    }

    public static Specification<ClassSchedule> build(ClassScheduleFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter != null) {
                if (filter.teacherId() != null) {
                    predicates.add(cb.equal(root.get("teacher").get("id"), filter.teacherId()));
                }
                if (filter.studentId() != null) {
                    predicates.add(cb.equal(root.get("student").get("id"), filter.studentId()));
                }
                if (filter.subjectId() != null) {
                    predicates.add(cb.equal(root.get("subject").get("id"), filter.subjectId()));
                }
                if (filter.dayOfWeek() != null) {
                    predicates.add(cb.equal(root.get("dayOfWeek"), filter.dayOfWeek()));
                }
                if (filter.status() != null) {
                    predicates.add(cb.equal(root.get("status"), filter.status()));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

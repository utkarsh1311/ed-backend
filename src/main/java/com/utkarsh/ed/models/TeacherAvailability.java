package com.utkarsh.ed.models;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalTime;

@Entity
@SQLDelete(sql = "UPDATE teacher_availability SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class TeacherAvailability extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    private LocalTime startTime;
    private LocalTime endTime;

    // Constructors
    public TeacherAvailability() {}

    // Getters and Setters
    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }

    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
}

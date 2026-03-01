package com.utkarsh.ed.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLDelete(sql = "UPDATE class_sessions SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Table(name = "class_sessions")
public class ClassSession extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_schedule_id", nullable = false)
    private ClassSchedule classSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name = "actual_start_at")
    private LocalDateTime actualStartAt;

    @Column(name = "actual_end_at")
    private LocalDateTime actualEndAt;

    @Enumerated(EnumType.STRING)
    private SessionStatus status = SessionStatus.SCHEDULED;

    @Column(columnDefinition = "TEXT", name = "feedback_text")
    private String feedbackText;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    /**
     * Stores the scheduledAt value from just before the most recent reschedule.
     * null  → session was never rescheduled.
     * non-null → session has been rescheduled at least once; this is the last time it was moved FROM.
     * Overwritten on every successive reschedule so it always reflects the immediately-previous slot.
     */
    @Column(name = "original_scheduled_at")
    private LocalDateTime originalScheduledAt;

    @Column(name = "is_test")
    private boolean isTest = false;

    @Column(name = "test_score")
    private Double testScore;

    public ClassSession() {}

    public ClassSession(
            ClassSchedule classSchedule,
            Teacher teacher,
            LocalDateTime scheduledAt,
            LocalDateTime actualStartAt,
            LocalDateTime actualEndAt,
            SessionStatus status,
            String feedbackText,
            String cancellationReason,
            boolean isTest,
            Double testScore) {
        this.classSchedule = classSchedule;
        this.teacher = teacher;
        this.scheduledAt = scheduledAt;
        this.actualStartAt = actualStartAt;
        this.actualEndAt = actualEndAt;
        this.status = status;
        this.feedbackText = feedbackText;
        this.cancellationReason = cancellationReason;
        this.isTest = isTest;
        this.testScore = testScore;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public LocalDateTime getActualStartAt() {
        return actualStartAt;
    }

    public void setActualStartAt(LocalDateTime actualStartAt) {
        this.actualStartAt = actualStartAt;
    }

    public LocalDateTime getActualEndAt() {
        return actualEndAt;
    }

    public void setActualEndAt(LocalDateTime actualEndAt) {
        this.actualEndAt = actualEndAt;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public LocalDateTime getOriginalScheduledAt() {
        return originalScheduledAt;
    }

    public void setOriginalScheduledAt(LocalDateTime originalScheduledAt) {
        this.originalScheduledAt = originalScheduledAt;
    }

    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }

    public Double getTestScore() {
        return testScore;
    }

    public void setTestScore(Double testScore) {
        this.testScore = testScore;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public ClassSchedule getClassSchedule() {
        return classSchedule;
    }

    public void setClassSchedule(ClassSchedule classSchedule) {
        this.classSchedule = classSchedule;
    }
}

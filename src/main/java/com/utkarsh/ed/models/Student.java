package com.utkarsh.ed.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.util.Objects;

@Entity
public class Student extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "parent_email")
    private String parentEmail;

    @Column(name = "parent_phone")
    private String parentPhone;

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "mother_name")
    private String motherName;

    public Student() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student student)) return false;
        return Objects.equals(getName(), student.getName()) && Objects.equals(getEmail(), student.getEmail()) && Objects.equals(getParentEmail(), student.getParentEmail()) && Objects.equals(getParentPhone(), student.getParentPhone()) && Objects.equals(getFatherName(), student.getFatherName()) && Objects.equals(getMotherName(), student.getMotherName()) && Objects.equals(getGrade(), student.getGrade());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getEmail(), getParentEmail(), getParentPhone(), getFatherName(), getMotherName(), getGrade());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParentEmail() {
        return parentEmail;
    }

    public void setParentEmail(String parentEmail) {
        this.parentEmail = parentEmail;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    @Column(name = "grade")
    private Integer grade;
}

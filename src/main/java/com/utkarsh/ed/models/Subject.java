package com.utkarsh.ed.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Subject extends BaseEntity {

    @Column(unique = true, name = "name")
    private String name;

    @Column(name = "is_special")
    private Boolean isSpecial;

    public Subject() {}

    public Subject(String name, Boolean isSpecial) {
        this.name = name;
        this.isSpecial = isSpecial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsSpecial() {
        return isSpecial;
    }

    public void setIsSpecial(Boolean special) {
        isSpecial = special;
    }
}

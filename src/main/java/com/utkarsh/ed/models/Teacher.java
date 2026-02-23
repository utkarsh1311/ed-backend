package com.utkarsh.ed.models;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import java.util.ArrayList;
import java.util.List;

@Entity
@SQLDelete(sql = "UPDATE teacher SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Teacher extends BaseEntity {

    private String name;

    @Column(unique = true)
    private String personalEmail;

    @Column(unique = true)
    private String businessMail;

    private String phone;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<TeacherAvailability> availabilities = new ArrayList<>();

    // Constructors
    public Teacher() {}

    public Teacher(String name, String email, String phone) {
        this.name = name;
        this.personalEmail = email;
        this.phone = phone;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPersonalEmail() { return personalEmail; }
    public void setPersonalEmail(String email) { this.personalEmail = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public List<TeacherAvailability> getAvailabilities() { return availabilities; }
    public void setAvailabilities(List<TeacherAvailability> availabilities) { this.availabilities = availabilities; }

	public String getBusinessMail() {
		return businessMail;
	}

	public void setBusinessMail(String businessMail) {
		this.businessMail = businessMail;
	}
}

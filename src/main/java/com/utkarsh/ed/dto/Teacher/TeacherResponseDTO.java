package com.utkarsh.ed.dto.Teacher;

public record TeacherResponseDTO(
	Long id,
	String name,
	String personalEmail,
	String businessMail,
	String phone
) {
}

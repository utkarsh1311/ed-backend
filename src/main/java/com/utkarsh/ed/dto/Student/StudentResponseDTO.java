package com.utkarsh.ed.dto.Student;

public record StudentResponseDTO(
        Long id,
        String name,
        String email,
        String parentEmail,
        String parentPhone,
        String fatherName,
        String motherName,
        Integer grade) {}

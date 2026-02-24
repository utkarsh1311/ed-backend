package com.utkarsh.ed.services;

import com.utkarsh.ed.dto.Student.StudentRequestDTO;
import com.utkarsh.ed.dto.Student.StudentResponseDTO;
import com.utkarsh.ed.exceptions.DuplicateResourceException;
import com.utkarsh.ed.exceptions.ResourceNotFoundException;
import com.utkarsh.ed.mappers.StudentMapper;
import com.utkarsh.ed.models.Student;
import com.utkarsh.ed.repositories.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

	private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
	private final StudentRepository studentRepository;
	private final StudentMapper studentMapper;

	public StudentService(StudentRepository studentRepository, StudentMapper studentMapper) {
		this.studentRepository = studentRepository;
		this.studentMapper = studentMapper;
	}

	// Create a new student
	public StudentResponseDTO createStudent(StudentRequestDTO studentRequestDTO) {

		// check if email already exists
		if (studentRepository.existsByEmail(studentRequestDTO.email())) {
			logger.warn(
					"Attempt to create student with existing email: {}", studentRequestDTO.email());
			throw new DuplicateResourceException(
					"Student with email " + studentRequestDTO.email() + " already exists.");
		}

		Student student = studentMapper.toEntity(studentRequestDTO);
		Student savedStudent = studentRepository.save(student);
		return studentMapper.toResponse(savedStudent);
	}

	// Get all students
	@Transactional(readOnly = true)
	public Page<StudentResponseDTO> getAllStudents(Pageable pageable) {
		return studentRepository.findAll(pageable).map(studentMapper::toResponse);
	}

	// Get student by ID
	public StudentResponseDTO getStudentById(Long id) {
		Student student =
				studentRepository
						.findById(id)
						.orElseThrow(
								() ->
										new ResourceNotFoundException(
												"Student not found with ID: " + id));
		return studentMapper.toResponse(student);
	}

	// Update student (PATCH style)
	public StudentResponseDTO updateStudent(Long id, StudentRequestDTO studentRequestDTO) {
		Student existingStudent =
				studentRepository
						.findById(id)
						.orElseThrow(
								() ->
										new ResourceNotFoundException(
												"Student not found with ID: " + id));
		studentMapper.updateFromDto(studentRequestDTO, existingStudent);
		Student updatedStudent = studentRepository.save(existingStudent);
		return studentMapper.toResponse(updatedStudent);
	}

	// Delete student
	@Transactional
	public void deleteStudent(Long id) {
		if (!studentRepository.existsById(id)) {
			logger.warn("Attempt to delete non-existent student with ID: {}", id);
			throw new ResourceNotFoundException("Student not found with ID: " + id);
		}
		studentRepository.deleteById(id);
		logger.info("Student with ID: {} deleted successfully", id);
	}
}

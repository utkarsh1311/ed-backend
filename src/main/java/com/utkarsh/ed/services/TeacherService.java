package com.utkarsh.ed.services;

import com.utkarsh.ed.dto.Teacher.TeacherRequestDTO;
import com.utkarsh.ed.dto.Teacher.TeacherResponseDTO;
import com.utkarsh.ed.exceptions.DuplicateResourceException;
import com.utkarsh.ed.exceptions.ResourceNotFoundException;
import com.utkarsh.ed.mappers.TeacherMapper;
import com.utkarsh.ed.models.Teacher;
import com.utkarsh.ed.repositories.TeacherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeacherService {

	private static final Logger logger = LoggerFactory.getLogger(TeacherService.class);

	private final TeacherRepository teacherRepository;
	private final TeacherMapper teacherMapper;

	public TeacherService(TeacherRepository teacherRepository, TeacherMapper teacherMapper) {
		this.teacherRepository = teacherRepository;
		this.teacherMapper = teacherMapper;
	}

	// Create Teacher
	public TeacherResponseDTO createTeacher(TeacherRequestDTO teacherRequestDTO) {
		if (teacherRepository.existsByBusinessMail(teacherRequestDTO.businessMail())) {
			logger.warn(
					"Attempt to create teacher with existing email {}",
					teacherRequestDTO.businessMail());
			throw new DuplicateResourceException(
					"Teacher with email " + teacherRequestDTO.businessMail() + " already exists.");
		}

		Teacher teacher = teacherMapper.toEntity(teacherRequestDTO);
		Teacher savedTeacher = teacherRepository.save(teacher);
		return teacherMapper.toResponse(savedTeacher);
	}

	// Get All Teachers
	@Transactional(readOnly = true)
	public Page<TeacherResponseDTO> getAllTeachers(Pageable pageable) {
		return teacherRepository.findAll(pageable).map(teacherMapper::toResponse);
	}

	// Get teacher by id
	public TeacherResponseDTO getTeacherById(Long id) {
		Teacher teacher =
				teacherRepository
						.findById(id)
						.orElseThrow(
								() ->
										new ResourceNotFoundException(
												"Teacher not found with ID: " + id));
		return teacherMapper.toResponse(teacher);
	}

	// update teacher
	public TeacherResponseDTO updateTeacher(Long id, TeacherRequestDTO teacherRequestDTO) {
		Teacher existingTeacher =
				teacherRepository
						.findById(id)
						.orElseThrow(
								() ->
										new ResourceNotFoundException(
												"Teacher not found with ID: " + id));
		teacherMapper.updateFromDto(teacherRequestDTO, existingTeacher);
		Teacher updatedTeacher = teacherRepository.save(existingTeacher);
		return teacherMapper.toResponse(updatedTeacher);
	}

	// Delete Teacher
	@Transactional
	public void deleteTeacher(Long id) {
		if (!teacherRepository.existsById(id)) {
			logger.warn("Attempt to delete non-existent teacher with ID: {}", id);
			throw new ResourceNotFoundException("Teacher not found with ID: " + id);
		}
		teacherRepository.deleteById(id);
		logger.info("Teacher with ID: {} deleted successfully", id);
	}
}

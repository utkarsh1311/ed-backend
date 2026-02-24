package com.utkarsh.ed.services;

import com.utkarsh.ed.dto.Subject.SubjectRequestDTO;
import com.utkarsh.ed.dto.Subject.SubjectResponseDTO;
import com.utkarsh.ed.exceptions.DuplicateResourceException;
import com.utkarsh.ed.exceptions.ResourceNotFoundException;
import com.utkarsh.ed.mappers.SubjectMapper;
import com.utkarsh.ed.models.Subject;
import com.utkarsh.ed.repositories.SubjectRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubjectService {

    private static final Logger logger = LoggerFactory.getLogger(SubjectService.class);

    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;

    public SubjectService(SubjectRepository subjectRepository, SubjectMapper subjectMapper) {
        this.subjectRepository = subjectRepository;
        this.subjectMapper = subjectMapper;
    }

    // Create a new subject
    public SubjectResponseDTO createSubject(SubjectRequestDTO requestDTO) {
        if (subjectRepository.existsByName(requestDTO.name())) {
            logger.warn("Attempt to create subject with existing name: {}", requestDTO.name());
            throw new DuplicateResourceException("Subject with name '" + requestDTO.name() + "' already exists.");
        }
        Subject subject = subjectMapper.toEntity(requestDTO);
        Subject saved = subjectRepository.save(subject);
        logger.info("Subject created with ID: {}", saved.getId());
        return subjectMapper.toResponse(saved);
    }

    // Get all subjects (paginated)
    @Transactional(readOnly = true)
    public List<SubjectResponseDTO> getAllSubjects() {
        List<Subject> allSubjects = subjectRepository.findAll();
        return subjectMapper.toResponseList(allSubjects);
    }

    // Get subject by ID
    @Transactional(readOnly = true)
    public SubjectResponseDTO getSubjectById(Long id) {
        Subject subject = subjectRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + id));
        return subjectMapper.toResponse(subject);
    }

    // Update subject (PATCH style)
    public SubjectResponseDTO updateSubject(Long id, SubjectRequestDTO requestDTO) {
        Subject existing = subjectRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + id));
        subjectMapper.updateFromDto(requestDTO, existing);
        Subject updated = subjectRepository.save(existing);
        logger.info("Subject with ID: {} updated successfully", id);
        return subjectMapper.toResponse(updated);
    }

    // Delete subject
    @Transactional
    public void deleteSubject(Long id) {
        if (!subjectRepository.existsById(id)) {
            logger.warn("Attempt to delete non-existent subject with ID: {}", id);
            throw new ResourceNotFoundException("Subject not found with ID: " + id);
        }
        subjectRepository.deleteById(id);
        logger.info("Subject with ID: {} deleted successfully", id);
    }
}

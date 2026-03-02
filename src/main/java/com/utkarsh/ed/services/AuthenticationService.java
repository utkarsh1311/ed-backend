package com.utkarsh.ed.services;

import com.utkarsh.ed.dto.Auth.AuthenticationRequest;
import com.utkarsh.ed.dto.Auth.AuthenticationResponse;
import com.utkarsh.ed.dto.Auth.RegisterRequest;
import com.utkarsh.ed.exceptions.DuplicateResourceException;
import com.utkarsh.ed.exceptions.ResourceNotFoundException;
import com.utkarsh.ed.models.AppUser;
import com.utkarsh.ed.models.Role;
import com.utkarsh.ed.models.Teacher;
import com.utkarsh.ed.repositories.AppUserRepository;
import com.utkarsh.ed.repositories.TeacherRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final AppUserRepository appUserRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtService jwtService,
            AppUserRepository appUserRepository,
            TeacherRepository teacherRepository,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.appUserRepository = appUserRepository;
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        if (appUserRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email already registered: " + request.getEmail());
        }

        AppUser appUser =
                new AppUser(request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getRole());

        if (request.getRole() == Role.TEACHER) {
            if (request.getTeacherId() == null) {
                throw new IllegalArgumentException("teacherId is required when role is TEACHER");
            }
            Teacher teacher = teacherRepository
                    .findById(request.getTeacherId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Teacher not found with id: " + request.getTeacherId()));
            appUserRepository.save(appUser);
            teacher.setAppUser(appUser);
            teacherRepository.save(teacher);
        } else {
            appUserRepository.save(appUser);
        }

        String token = jwtService.generateToken(appUser);
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtService.generateToken(userDetails);
        return new AuthenticationResponse(token);
    }
}

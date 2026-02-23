package com.utkarsh.ed.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PagedResponse<T>(
        List<T> data,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
    public static <T> PagedResponse<T> from(Page<T> page) {
        return new PagedResponse<>(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}


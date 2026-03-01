package com.utkarsh.ed.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.data.domain.Page;

@Schema(description = "Generic paginated response wrapper")
public record PagedResponse<T>(
        @Schema(description = "List of items in the current page") List<T> data,
        @Schema(description = "Total number of items across all pages", example = "42") long totalElements,
        @Schema(description = "Total number of pages", example = "3") int totalPages,
        @Schema(description = "Whether this is the first page", example = "true") boolean first,
        @Schema(description = "Whether this is the last page", example = "false") boolean last) {
    public static <T> PagedResponse<T> from(Page<T> page) {
        return new PagedResponse<>(
                page.getContent(), page.getTotalElements(), page.getTotalPages(), page.isFirst(), page.isLast());
    }
}

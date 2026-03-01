package com.utkarsh.ed.swagger;

import com.utkarsh.ed.exceptions.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 204 No Content + 404 Not Found */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
    @ApiResponse(responseCode = "204", description = "Deleted successfully"),
    @ApiResponse(responseCode = "404", description = "Resource not found",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
})
public @interface ApiResponsesDelete {}

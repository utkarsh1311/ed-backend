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

/** 201 Created + 400 Validation + 409 Conflict */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
    @ApiResponse(responseCode = "201", description = "Created successfully"),
    @ApiResponse(responseCode = "400", description = "Validation failed",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(responseCode = "409", description = "Resource already exists",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
})
public @interface ApiResponsesCreate {}

package com.website.web.dto.request.category;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateSubcategoryRequest {
    @NotNull
    private Long categoryId;
    @NotBlank
    private String name;
    @NotBlank
    private String nameKor;
}

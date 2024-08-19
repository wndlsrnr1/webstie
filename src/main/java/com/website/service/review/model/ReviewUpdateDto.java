package com.website.service.review.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewUpdateDto {
    private Long userId;
    private Long itemId;
    private Integer star;
    private String content;
}

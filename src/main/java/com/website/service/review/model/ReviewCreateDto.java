package com.website.service.review.model;

import com.website.repository.model.item.Item;
import com.website.repository.model.user.User;
import com.website.repository.review.model.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewCreateDto {
    private Long itemId;
    private Long userId;
    private Integer star;
    private String content;

}

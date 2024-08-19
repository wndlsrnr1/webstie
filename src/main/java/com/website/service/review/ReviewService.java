package com.website.service.review;

import com.website.exception.ClientException;
import com.website.exception.ErrorCode;
import com.website.repository.common.PageResult;
import com.website.repository.item.ItemRepository;
import com.website.repository.model.item.Item;
import com.website.repository.model.user.User;
import com.website.repository.review.ReviewRepository;
import com.website.repository.review.model.Review;
import com.website.repository.review.model.ReviewSearchCriteria;
import com.website.repository.user.UserRepository;
import com.website.service.common.model.PageResultDto;
import com.website.service.item.ItemValidator;
import com.website.service.review.model.*;
import com.website.service.user.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserValidator userValidator;
    private final ItemValidator itemValidator;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    // create
    @Transactional
    public ReviewDto registerReview(ReviewCreateDto dto) {
        User user = userValidator.validateAndGet(dto.getUserId());
        Item item = itemValidator.validateAndGet(dto.getItemId());

        Review review = Review.builder()
                .item(item)
                .user(user)
                .star(dto.getStar())
                .content(dto.getContent())
                .build();

        Review savedReview = reviewRepository.save(review);
        return ReviewDto.of(savedReview);
    }

    // read
    @Transactional(readOnly = true)
    public ReviewDto getReviewById(Long reviewId) {
        if (reviewId == null) {
            throw new ClientException(ErrorCode.BAD_REQUEST, "reviewId is null");
        }

        Review foundReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ClientException(ErrorCode.BAD_REQUEST, "review not found. reviewId = " + reviewId));

        return ReviewDto.of(foundReview);
    }

    @Transactional(readOnly = true)
    public ReviewDto getReviewByUserIdAndItemId(Long userId, Long itemId) {
        User user = userValidator.validateAndGet(userId);
        Item item = itemValidator.validateAndGet(itemId);

        Review foundReview = reviewRepository.findByUserAndItem(user, item).orElseThrow(() ->
                new ClientException(ErrorCode.BAD_REQUEST, "review not found. userId = " + userId + ", itemId = " + itemId));

        return ReviewDto.of(foundReview);
    }

    // update
    @Transactional
    public ReviewDto updateReview(ReviewUpdateDto dto) {
        User user = userValidator.validateAndGet(dto.getUserId());
        Item item = itemValidator.validateAndGet(dto.getItemId());

        Review review = reviewRepository.findByUserAndItem(user, item).orElseThrow(
                () -> new ClientException(ErrorCode.BAD_REQUEST, "review not found userId = " + user.getId() + ", itemId = " + item.getId())
        );

        review.setContent(dto.getContent());
        review.setStar(dto.getStar());
        Review updatedReview = reviewRepository.save(review);
        return ReviewDto.of(updatedReview);
    }

    //delete
    @Transactional
    public void removeReview(Long userId, Long itemId) {
        User user = userValidator.validateAndGet(userId);
        Item item = itemValidator.validateAndGet(itemId);

        Review foundReview = reviewRepository.findByUserAndItem(user, item).orElseThrow(() ->
                new ClientException(ErrorCode.BAD_REQUEST, "review not found. userId = " + userId + ", itemId = " + itemId));

        reviewRepository.delete(foundReview);
    }

    @Transactional(readOnly = true)
    public PageResultDto<ReviewDto> searchReview(ReviewSearchRequestDto reviewSearchRequestDto) {

        ReviewSearchCriteria criteria = reviewSearchRequestDto.toCriteria();
        PageResult<Review> findResult = reviewRepository.search(criteria);

        return PageResultDto.<ReviewDto>builder()
                .items(findResult.getItems().stream().map(ReviewDto::of).collect(Collectors.toList()))
                .totalCount(findResult.getTotalCount())
                .nextSearchAfter(findResult.getGetNextSearchAfter())
                .build();
    }


}

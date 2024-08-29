package com.website.service.user;

import com.website.exception.ClientException;
import com.website.exception.CustomValidationException;
import com.website.exception.ErrorCode;
import com.website.repository.user.model.User;
import com.website.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindingResultUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    /**
     * checks null and exists. and return User
     * @param userId
     * @return Entity of User
     */
    public User validateAndGet(Long userId) {
        if (userId == null) {
            throw new ClientException(ErrorCode.BAD_REQUEST, "userId is null");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ClientException(ErrorCode.BAD_REQUEST, "user not found. userId=" + userId)
                );
        return user;
    }

    public void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ClientException(ErrorCode.BAD_REQUEST, "user not found. userId = " + userId);
        }
    }


    public void validatePasswordConfirmation(Long userId, String newPassword, String confirmPassword) {
        if (newPassword == null || confirmPassword == null || !newPassword.equals(confirmPassword)) {
            throw new CustomValidationException(ErrorCode.UNAUTHORIZED,
                    "newPassword not match to confirmPassword. userId = " + userId,
                    Map.of("newPassword", "새 비밀번호와 확인 비밀번호가 일치하지 않습니다. 두 비밀번호가 모두 동일한지 확인하세요"));
        }
    }
}

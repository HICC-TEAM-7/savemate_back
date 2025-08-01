package com.hicc.budget.global.exception.custom;

import com.hicc.budget.global.exception.BusinessException;
import com.hicc.budget.global.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}

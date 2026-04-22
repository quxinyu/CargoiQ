package com.efreight.base.common.core.utils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.Set;

/**
 * 手动校验类，快速失败模式
 * @author alex
 * @date 2023/04/19
 */
public class ValidateUtil {
    private static final Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验实体类
     */
    public static <T> void validate(T t) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
        if (constraintViolations.size() > 0) {
            throw new ValidationException(constraintViolations.iterator().next().getMessage());
        }
    }

    /**
     * 通过组来校验实体类
     */
    public static <T> void validate(T t, Class<?>... groups) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t, groups);
        if (constraintViolations.size() > 0) {
            throw new ValidationException(constraintViolations.iterator().next().getMessage());
        }
    }
}

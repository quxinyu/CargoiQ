package com.efreight.base.common.core.utils.validate.annotation;

import com.efreight.base.common.core.utils.validate.validator.XssValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义xss校验注解
 *
 * @author efreight
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Constraint(validatedBy = {XssValidator.class})
public @interface Xss {
    String message()

            default "不允许任何脚本运行";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

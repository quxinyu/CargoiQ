package com.efreight.base.common.core.utils.validate.annotation;

import cn.hutool.core.util.StrUtil;
import com.efreight.base.common.core.utils.validate.validator.ValidStringArrayValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

/**
 * String类型的数组/集合判断，例如：abc,cad,sss，多个用,隔开的情况
 * @author alex
 * @date 2023/05/23
 */
@Constraint(validatedBy = {ValidStringArrayValidator.class})
@Target({METHOD, FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidStringArray {
    /**
     * 分隔符
     * @return
     */
    String split() default StrUtil.COMMA;

    /**
     * 分割后最大长度，-1表示不限制
     * @return
     */
    int maxLength() default  -1;

    /**
     * 分割后，每项的正则验证，任意一项没通过则全部不通过
     * @return
     */
    String regexp() default StrUtil.EMPTY;

    String message() default "格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

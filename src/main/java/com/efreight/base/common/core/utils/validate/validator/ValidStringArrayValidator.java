package com.efreight.base.common.core.utils.validate.validator;


import cn.hutool.core.util.ReUtil;
import com.efreight.base.common.core.utils.StringUtils;
import com.efreight.base.common.core.utils.validate.annotation.ValidStringArray;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 自定义校验规则验证器
 *
 * @author alex
 * @date 2020/11/25
 */
public class ValidStringArrayValidator implements ConstraintValidator<ValidStringArray, String> {

    private String split;

    private Integer maxLength;

    private String regexp;

    @Override
    public void initialize(ValidStringArray constraintAnnotation) {
        this.split = constraintAnnotation.split();
        this.maxLength = constraintAnnotation.maxLength();
        this.regexp = constraintAnnotation.regexp();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }
        String[] array = value.split(this.split);
        if(this.maxLength > 0 && array.length > this.maxLength) {
            return false;
        }
        if(StringUtils.isNotEmpty(regexp)) {
            for (String s : array) {
                if(!ReUtil.isMatch(this.regexp, s)){
                    return false;
                }
            }
        }
        return true;
    }
}

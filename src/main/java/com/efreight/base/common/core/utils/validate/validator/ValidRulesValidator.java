package com.efreight.base.common.core.utils.validate.validator;


import cn.hutool.core.lang.Validator;
import com.efreight.base.common.core.utils.StringUtils;
import com.efreight.base.common.core.utils.validate.Constant.ValidRuleType;
import com.efreight.base.common.core.utils.validate.annotation.ValidRules;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义校验规则验证器
 *
 * @author alex
 * @date 2020/11/25
 */
public class ValidRulesValidator implements ConstraintValidator<ValidRules, Serializable> {

    private ValidRuleType[] types;

    @Override
    public void initialize(ValidRules constraintAnnotation) {
        this.types = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Serializable value, ConstraintValidatorContext context) {
        String val = null;
        if (value != null) {
            val = value.toString();
        }
        if (StringUtils.isEmpty(val) || this.types.length == 0) {
            return true;
        }
        List<Boolean> validStatus = new ArrayList<>();
        for (ValidRuleType type : types) {
            switch (type) {
                case VALID_MOBILE:
                    validStatus.add(Validator.isMobile(val));
                    break;
                case VALID_ZIP_CODE:
                    validStatus.add(Validator.isZipCode(val));
                    break;
                case VALID_CITIZEN_ID:
                    validStatus.add(Validator.isCitizenId(val));
                    break;
                case VALID_GENERAL:
                    validStatus.add(Validator.isGeneral(val));
                    break;
                case VALID_IP:
                    validStatus.add(Validator.isIpv4(val));
                    break;
                case VALID_PLATE_NUMBER:
                    validStatus.add(Validator.isPlateNumber(val));
                    break;
                case VALID_URL:
                    validStatus.add(Validator.isUrl(val));
                    break;
                case VALID_CHINESE:
                    validStatus.add(Validator.isChinese(val));
                    break;
            }
        }
        return !validStatus.contains(false);
    }
}

package com.efreight.base.module.one.record.neone.global;

import com.efreight.base.common.core.enmus.ResultStatusCode;
import com.efreight.base.common.core.exception.EftException;
import com.efreight.base.common.core.exception.EftGlobalException;
import com.efreight.base.common.core.exception.ErrorCode;
import com.efreight.base.common.core.exception.OneRecordServerException;
import com.efreight.base.common.core.model.Result;
import com.efreight.base.common.core.utils.StringUtils;
import com.efreight.base.module.one.record.neone.ex.*;
import com.efreight.base.module.one.record.neone.exception.SubjectNotFoundException;
import com.efreight.base.module.one.record.neone.utils.ResponseEntityBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局异常处理中心
 *
 * @author alex
 * @date 2020/06/06
 */
@Slf4j
@RestControllerAdvice
public class OneRecordServiceGlobalExceptionHandler {

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error(">>>>>>>>>>>>>>>>>>> HttpMessageNotReadableException: ", e);
        return new Result<>(ResultStatusCode.BAD_REQUEST.getCode(), "请求体解析失败");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error(">>>>>>>>>>>>>>>>>>> MissingServletRequestParameterException: ", e);
        return new Result<>(ResultStatusCode.BAD_REQUEST.getCode(), "缺少请求参数: " + e.getParameterName());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException e) {
        log.error(">>>>>>>>>>>>>>>>>>> BindException: ", e);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> errors = fieldErrors.stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());
        return new Result<>(ResultStatusCode.BAD_REQUEST.getCode(), errors.toString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(">>>>>>>>>>>>>>>>>>> MethodArgumentNotValidException: ", e);
        List<String> errors = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());
        return new Result<>(ResultStatusCode.BAD_REQUEST.getCode(), errors.toString());
    }

    /**
     * 401 - 未授权访问异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public ResponseEntity<?> unauthorizedException(HttpMediaTypeNotSupportedException e) {

        log.info(">>>>>>>>>>>>>>>>>>Content-Type 非法: ", e);
        ResponseEntity.BodyBuilder builder = ResponseEntityBuilder.create(415);
        return builder.body("Unsupported Content Type");
    }

    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("不支持当前请求方法", e);

        ResponseEntity.BodyBuilder builder = ResponseEntityBuilder.create(416);
        return builder.body("Unsupported Request Method");
    }


    /**
     * 500 - System Error
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error("服务运行异常", e);

        ResponseEntity.BodyBuilder builder = ResponseEntityBuilder.fail();
        return builder.body("Internal Server Error");
    }

    /**
     * 业务异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler({EftException.class})
    public ResponseEntity<?> handleEftException(EftException e) {
        log.error(">>>>>>>>>>>>>>>>>全局业务异常：", e);

        ResponseEntity.BodyBuilder builder = ResponseEntityBuilder.fail();
        if (StringUtils.isNotEmpty(e.getCode())) {
            return builder.body(Result.result(e.getCode(), e.getMessage()));
        } else {
            return builder.body(Result.result(ResultStatusCode.BUSINESS_ERROR.getCode(), e.getMessage()));
        }
    }

    /**
     * 业务异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler({EftGlobalException.class})
    public ResponseEntity<?> eftGlobalException(EftGlobalException e) {
        log.error(">>>>>>>>>>>>>>>>>>>>全局业务异常", e);
        ErrorCode errorCode = e.getErrorCode();
        if (errorCode != null) {
            String code = errorCode.getCode();
            String msg = errorCode.getMsg();
            ResponseEntity.BodyBuilder builder = ResponseEntityBuilder.create(Integer.parseInt(code));
            return builder.body(msg);
        }

        ResponseEntity.BodyBuilder builder = ResponseEntityBuilder.fail();
        return builder.body(Result.result(ResultStatusCode.BUSINESS_ERROR.getCode(), e.getMessage()));
    }


    @ExceptionHandler({OneRecordServerException.class})
    public ResponseEntity<?> handleOneRecordServerException(OneRecordServerException e) {

        ResponseEntity.BodyBuilder builder = ResponseEntityBuilder.create(401);
        ResultStatusCode statusCode = e.getStatusCode();
        if (statusCode != null) {
            return builder.body(Result.result(statusCode));
        }

        Integer code = e.getCode();
        if (null != code) {
            return builder.body(e.getMessage());
        }

        return builder.body(Result.fail(e.getMessage()));
    }

    @ExceptionHandler({NeoneRequestBodyException.class})
    public ResponseEntity<?> handleNeoneRequestBodyException(NeoneRequestBodyException e) {
        log.error(">>>>>>>>>>>>>>>> 请求的结构非法 ：", e);
        return ResponseEntityBuilder.create(400).body("Bad Request(" + e.getMessage() + ")");
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public Result<?> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(">>>>>>>>>>>>>>>> 参数错误 ：", e);
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler({ActionRequestException.class})
    public ResponseEntity<?> handleActionRequestException(ActionRequestException e) {
        log.error(">>>>>>>>>>>>>>>> 请求的结构非法 ：", e);
        return ResponseEntityBuilder.create(e.getCode()).body(e.getBody());
    }

    @ExceptionHandler({StatusMissingException.class})
    public ResponseEntity<?> handleStatusMissingException(StatusMissingException e) {
        log.error(">>>>>>>>>>>>>>>> action request 没有发现更新状态 ：", e);
        return ResponseEntityBuilder.fail().body("status missing");
    }

    @ExceptionHandler({NotSupportActionRequestStatusException.class})
    public ResponseEntity<?> handleNotSupportActionRequestStatusException(NotSupportActionRequestStatusException e) {
        log.error(">>>>>>>>>>>>>>>> 不支持的action request status ：", e);
        return ResponseEntityBuilder.fail().body("not supported action request patch status");
    }

    @ExceptionHandler({LogisticsObjectException.class})
    public ResponseEntity<?> handleLogisticsObjectException(LogisticsObjectException e) {
        log.error(">>>>>>>>>>>>>>>> 操作物流对象发生错误 ：", e);
        return ResponseEntityBuilder.create(e.getCode()).body(e.getSubject());
    }

    @ExceptionHandler({NeOneCompanyHolderException.class})
    public ResponseEntity<?> handleNeOneCompanyHolderException(NeOneCompanyHolderException e) {
        log.error(">>>>>>>>>>>>>>>> neone server company error ：", e);
        return ResponseEntityBuilder.fail().body(e.getMessage());
    }


    @ExceptionHandler({SubscribeNotifyException.class})
    public Result<?> handleNeOneCompanyHolderException(SubscribeNotifyException e) {
        log.error(">>>>>>>>>>>>>>>> 内部订阅通知发生错误 ：", e);
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler({NeOneRequestWrapperException.class})
    public Result<?> handleNeOneRequestWrapperException(NeOneRequestWrapperException e) {
        log.error(">>>>>>>>>>>>>>>> neone request wrapper error ：", e);
        return Result.fail(e.getMessage());
    }


    @ExceptionHandler({TopicMissingException.class})
    public ResponseEntity<?> handleNotSupportLogisticsObjectTypeException(TopicMissingException e) {
        log.error(">>>>>>>>>>>>>>>>> topic 缺失：", e);
        ResponseEntity.BodyBuilder builder = ResponseEntityBuilder.create(400);
        return builder.body("{\n" +
                "    \"@context\": {\n" +
                "       \"api\": \"https://onerecord.iata.org/ns/api#\",\n" +
                "       \"@language\": \"en-US\"\n" +
                "    },\n" +
                "    \"@type\": \"api:Error\",\n" +
                "    \"@id\": \"_:b0\",\n" +
                "    \"api:hasTitle\": \"Missing query parameter\",\n" +
                "    \"api:hasErrorDetail\": [{\n" +
                "       \"@type\": \"api:ErrorDetail\",\n" +
                "       \"@id\": \"_:b1\",\n" +
                "       \"api:hasCode\": \"400\",\n" +
                "       \"api:hasMessage\": \"The required query parameter `topic` is missing.\"\n" +
                "    }]\n" +
                " }");
    }

    @ExceptionHandler({NotSupportLogisticsObjectTypeException.class})
    public ResponseEntity<?> handleNotSupportLogisticsObjectTypeException(NotSupportLogisticsObjectTypeException e) {
        log.error(">>>>>>>>>>>>>>>>不支持的物流对象类型：", e);
        ResponseEntity.BodyBuilder builder = ResponseEntityBuilder.create(400);
        return builder.body("{\n" +
                "    \"@context\": {\n" +
                "       \"api\": \"https://onerecord.iata.org/ns/api#\",\n" +
                "       \"@language\": \"en-US\"\n" +
                "    },\n" +
                "    \"@type\": \"api:Error\",\n" +
                "    \"@id\": \"_:b0\",\n" +
                "    \"api:hasTitle\": \"Logistics Object Type not supported\",\n" +
                "    \"api:hasErrorDetail\": [{\n" +
                "       \"@type\": \"api:ErrorDetail\",\n" +
                "       \"@id\": \"_:b1\",\n" +
                "       \"api:hasCode\": \"400\",\n" +
                "       \"api:hasMessage\": \"Provided Logistics Object Type is not supported\"\n" +
                "    }]\n" +
                " }");
    }


    @ExceptionHandler({LogisticsObjectPatchException.class})
    public ResponseEntity<?> handleLogisticsObjectPatchException(LogisticsObjectPatchException e) {
        log.error(">>>>>>>>>>>>>>>>PATCH物流对象时不匹配：", e);
        ResponseEntity.BodyBuilder builder = ResponseEntityBuilder.create(400);
        return builder.body("{\n" +
                "    \"@context\": {\n" +
                "       \"api\": \"https://onerecord.iata.org/ns/api#\",\n" +
                "       \"@language\": \"en-US\"\n" +
                "    },\n" +
                "    \"@type\": \"api:Error\",\n" +
                "    \"@id\": \"_:b0\",\n" +
                "    \"api:hasTitle\": \"Logistics Object URI does not match\",\n" +
                "    \"api:hasErrorDetail\": [{\n" +
                "       \"@type\": \"api:ErrorDetail\",\n" +
                "       \"@id\": \"_:b1\",\n" +
                "       \"api:hasCode\": \"400\",\n" +
                "       \"api:hasMessage\": \"LogisticsObject URI in Change does not match requested Logistics Object URI\",\n" +
                "       \"api:hasResource\": \" " + e.getSubject() + "\"\n" +
                "    }]\n" +
                " }");
    }

    @ExceptionHandler({RuntimeException.class})
    public Result<?> handleRuntimeException(RuntimeException e) {
        log.error(">>>>>>>>>>>>>>>>系统发生RuntimeException：", e);
        return Result.fail(ResultStatusCode.SYSTEM_ERR);
    }

    @ExceptionHandler({NeOneAuthException.class})
    public Result<?> handleNeOneAuthException(NeOneAuthException e) {
        log.error(">>>>>>>>>>>>>>>>系统发生RuntimeException：", e);
        return Result.fail(e.getMessage());
    }


    @ExceptionHandler({SubjectNotFoundException.class})
    public ResponseEntity<?> handleException(SubjectNotFoundException e) {
        ResponseEntity.BodyBuilder builder = ResponseEntityBuilder.create(500);
        ResultStatusCode statusCode = e.getStatusCode();
        if (statusCode != null) {
            return builder.body(Result.result("500", statusCode.getMsg()));
        }
        Integer code = e.getCode();
        if (null != code) {
            return builder.body(e.getMessage());
        }
        return builder.body(Result.result("500", e.getMessage()));
    }
}

package com.efreight.base.common.mvc.controller;

import com.efreight.base.common.core.model.Result;
import com.efreight.base.common.mvc.entity.BaseEntity;
import com.efreight.base.common.mvc.entity.PageEntity;
import com.efreight.base.common.poi.excel.ExportExcel;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 导入导出
 *
 * @param <Entity>    实体
 * @param <Query>     查询对象
 * @author efreight
 * @date 2022-09-21
 */
public interface PoiController<Entity extends BaseEntity, Query extends PageEntity<Entity>> extends QueryController<Entity, Query> {
    /**
     * 获取实体的类型
     *
     * @return 实体的类型
     */
    default Class<Entity> getExcelClass() {
        return getEntityClass();
    }


    /**
     * 导出Excel
     *
     */
    @ApiOperation(value = "导出Excel")
    @RequestMapping(value = "/export", method = RequestMethod.GET, produces = "application/octet-stream")
    default void exportExcel(@RequestBody @Validated Query query, HttpServletResponse response) {
        ExportExcel.write(response, query(query).getData(), getExcelClass());
    }


    /**
     * 导入模板
     *
     * @return 预览html
     */
    @ApiOperation(value = "预览Excel")
    @RequestMapping(value = "/import/template", method = RequestMethod.GET)
    default void importTemplate() {

    }

    /**
     * 使用自动生成的实体+注解方式导入
     * 建议自建实体使用
     *
     * @param simpleFile 上传文件
     * @return 是否导入成功
     * @throws Exception 异常
     */
    @ApiOperation(value = "导入Excel")
    @PostMapping(value = "/import")
    default Result importExcel(@RequestParam(value = "file") MultipartFile simpleFile) {
        // TODO
        return Result.ok();
    }

}

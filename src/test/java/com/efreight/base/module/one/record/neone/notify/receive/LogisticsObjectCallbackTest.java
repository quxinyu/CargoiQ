package com.efreight.base.module.one.record.neone.notify.receive;

import com.alibaba.fastjson2.JSON;
import com.efreight.base.common.core.enmus.LoModuleType;
import com.efreight.base.common.core.enmus.OneRecordParseVersionType;
import com.efreight.base.module.one.record.neone.enums.FromType;
import com.efreight.base.module.one.record.neone.enums.NotifyEventType;
import com.efreight.base.module.one.record.neone.helper.IriGenerator;
import com.efreight.base.module.one.record.neone.model.entity.NeOneLogisticsObjects;
import com.efreight.base.module.one.record.neone.model.entity.NeOneServerCompanyHolder;
import com.efreight.base.module.one.record.neone.model.entity.NeOneSubscriptionRequest;
import com.efreight.base.module.one.record.neone.service.NeOneCompanyService;
import com.efreight.base.module.one.record.neone.service.NeOneLogisticsObjectsService;
import com.efreight.base.module.one.record.neone.service.OneRecordSubscriptionsNotifyService;
import com.efreight.base.module.one.record.neone.utils.HttpClientUtil;
import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * LogisticsObjectCallback 测试类
 *
 * @author test
 * @since 2024-12-23
 */
@Slf4j
@SpringBootTest
@TestPropertySource(properties = {
        "neone.dataModel={'海航生产':'OneRecord_HU','国航生产':'OneRecord_CA'}",
        "neone.defaultModel=OneRecord_HU"
})
class LogisticsObjectCallbackTest {

    @Autowired
    private LogisticsObjectCallback logisticsObjectCallback;

    @MockBean
    private NeOneCompanyService neOneCompanyService;

    @MockBean
    private NeOneLogisticsObjectsService neOneLogisticsObjectsService;

    @MockBean
    private OneRecordSubscriptionsNotifyService oneRecordSubscriptionsNotifyService;

    @MockBean
    private HttpClientUtil httpClientUtil;

    @MockBean
    private EventBus asyncNeOneEventBus;

    @MockBean
    private IriGenerator iriGenerator;

    @Value("#{${neone.data-model}}")
    private Object dataModelMapConfig;

    private NeOneObjectNotifications testNotification;
    private NeOneSubscriptionRequest testSubscriptionRequest;
    private NeOneServerCompanyHolder testCompanyHolder;

    @BeforeEach
    void setUp() {
        log.info("==================== 测试初始化开始 ====================");

        // 准备测试通知对象
        testNotification = new NeOneObjectNotifications();
        testNotification.setLoId("test-lo-id-001");
        testNotification.setNotifyFrom("test-company");
        testNotification.setNotifyEventType(NotifyEventType.LOGISTICS_OBJECT_CREATED.name());
        testNotification.setCreateTime(LocalDateTime.now());

        // 构造通知体 JSON
        String notifyBody = "{\n" +
                "  \"@context\": {\n" +
                "    \"api\": \"https://onerecord.iata.org/ns/api#\",\n" +
                "    \"cargo\": \"https://onerecord.iata.org/ns/cargo#\"\n" +
                "  },\n" +
                "  \"api:isTriggeredBy\": {\n" +
                "    \"@id\": \"http://example.com/action-requests/test-action-123\"\n" +
                "  },\n" +
                "  \"api:hasLogisticsObject\": {\n" +
                "    \"@id\": \"http://example.com/logistics-objects/test-lo-001\"\n" +
                "  },\n" +
                "  \"api:hasLogisticsObjectType\": {\n" +
                "    \"@value\": \"https://onerecord.iata.org/ns/cargo#Waybill\"\n" +
                "  }\n" +
                "}";
        testNotification.setNotifyBody(notifyBody);

        // 准备测试订阅请求
        testSubscriptionRequest = new NeOneSubscriptionRequest();
        testSubscriptionRequest.setId(1L);
        testSubscriptionRequest.setActionRequestIri("http://example.com/action-requests/test-action-123");
        testSubscriptionRequest.setTargetCompanyId("company-001");
        testSubscriptionRequest.setCreateTime(LocalDateTime.now());

        // 准备测试公司信息
        testCompanyHolder = new NeOneServerCompanyHolder();
        testCompanyHolder.setId(1L);
        testCompanyHolder.setCompanyId("company-001");
        testCompanyHolder.setCompanyName("测试航空公司");
//        testCompanyHolder.setServerUrl("http://example.com");
//        testCompanyHolder.setAccessToken("test-token-12345");

        // Mock IriGenerator
        when(iriGenerator.generateLogisticsObjectLoId(anyString()))
                .thenReturn("http://localhost:8080/neone/logistics-objects/test-lo-id-001");

        log.info("==================== 测试初始化完成 ====================");
    }

    /**
     * 测试配置注入
     */
    @Test
    void testConfigInjection() {
        log.info("==================== 测试配置注入 ====================");
        log.info("dataModelMap配置: {}", dataModelMapConfig);
        assertNotNull(dataModelMapConfig, "dataModelMap配置不能为null");
    }

    /**
     * 测试正常回调流程
     */
    @Test
    void testToCallback_Success() {
        log.info("==================== 测试正常回调流程 ====================");

        // Mock 订阅请求查询
        when(oneRecordSubscriptionsNotifyService.getOne(any()))
                .thenReturn(testSubscriptionRequest);

        // Mock 公司信息查询
        when(neOneCompanyService.getOne(any()))
                .thenReturn(testCompanyHolder);

        // Mock HTTP 响应 - 返回完整的物流对象
        String responseBody = "{\n" +
                "  \"@id\": \"http://example.com/logistics-objects/test-lo-001\",\n" +
                "  \"@type\": \"cargo:Waybill\",\n" +
                "  \"cargo:waybillNumber\": \"12345678\",\n" +
                "  \"cargo:waybillPrefix\": \"999\",\n" +
                "  \"cargo:departureLocation\": {\n" +
                "    \"@type\": \"cargo:Location\",\n" +
                "    \"cargo:locationCode\": \"PEK\"\n" +
                "  },\n" +
                "  \"cargo:arrivalLocation\": {\n" +
                "    \"@type\": \"cargo:Location\",\n" +
                "    \"cargo:locationCode\": \"CAN\"\n" +
                "  }\n" +
                "}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
        when(httpClientUtil.get(anyString(), any(), eq(String.class)))
                .thenReturn(responseEntity);

        // Mock 保存操作
        when(neOneLogisticsObjectsService.save(any(NeOneLogisticsObjects.class)))
                .thenReturn(true);

        // 执行测试
        logisticsObjectCallback.toCallback(testNotification);

        // 验证调用
        verify(oneRecordSubscriptionsNotifyService, times(1)).getOne(any());
        verify(neOneCompanyService, times(1)).getOne(any());
        verify(httpClientUtil, times(1)).get(anyString(), any(), eq(String.class));
        verify(neOneLogisticsObjectsService, times(1)).save(any(NeOneLogisticsObjects.class));
        verify(asyncNeOneEventBus, times(1)).post(any());

        // 捕获保存的参数
        ArgumentCaptor<NeOneLogisticsObjects> captor = ArgumentCaptor.forClass(NeOneLogisticsObjects.class);
        verify(neOneLogisticsObjectsService).save(captor.capture());
        NeOneLogisticsObjects savedObject = captor.getValue();

        // 验证保存的数据
        assertEquals("test-lo-id-001", savedObject.getLoId());
        assertEquals("Waybill", savedObject.getContextType());
        assertEquals(LoModuleType.LOGISTICS_OBJECT.name(), savedObject.getLoModuleType());
        assertEquals(FromType.SUBSCRIPTION_NOTIFY.name(), savedObject.getLoFromType());
        assertEquals(OneRecordParseVersionType.V3.getName(), savedObject.getVersion());
        assertNotNull(savedObject.getBodyText());
        assertNotNull(savedObject.getMawbCode());

        log.info("✅ 正常回调流程测试通过");
        log.info("保存的运单号: {}", savedObject.getMawbCode());
    }

    /**
     * 测试订阅记录不存在的情况
     */
    @Test
    void testToCallback_SubscriptionNotFound() {
        log.info("==================== 测试订阅记录不存在 ====================");

        // Mock 订阅请求查询返回空
        when(oneRecordSubscriptionsNotifyService.getOne(any()))
                .thenReturn(null);

        // 执行测试并验证异常
        Exception exception = assertThrows(Exception.class, () -> {
            logisticsObjectCallback.toCallback(testNotification);
        });

        assertTrue(exception.getMessage().contains("未匹配到订阅记录"),
                "异常信息应包含'未匹配到订阅记录'");

        log.info("✅ 订阅记录不存在测试通过，异常信息: {}", exception.getMessage());
    }

    /**
     * 测试公司信息不存在的情况
     */
    @Test
    void testToCallback_CompanyNotFound() {
        log.info("==================== 测试公司信息不存在 ====================");

        // Mock 订阅请求查询
        when(oneRecordSubscriptionsNotifyService.getOne(any()))
                .thenReturn(testSubscriptionRequest);

        // Mock 公司信息查询返回空
        when(neOneCompanyService.getOne(any()))
                .thenReturn(null);

        // 执行测试并验证异常
        Exception exception = assertThrows(Exception.class, () -> {
            logisticsObjectCallback.toCallback(testNotification);
        });

        assertTrue(exception.getMessage().contains("未匹配到服务地址"),
                "异常信息应包含'未匹配到服务地址'");

        log.info("✅ 公司信息不存在测试通过，异常信息: {}", exception.getMessage());
    }

    /**
     * 测试获取物流对象失败的情况
     */
    @Test
    void testToCallback_GetLogisticsObjectFailed() {
        log.info("==================== 测试获取物流对象失败 ====================");

        // Mock 订阅请求查询
        when(oneRecordSubscriptionsNotifyService.getOne(any()))
                .thenReturn(testSubscriptionRequest);

        // Mock 公司信息查询
        when(neOneCompanyService.getOne(any()))
                .thenReturn(testCompanyHolder);

        // Mock HTTP 响应返回错误状态码
        ResponseEntity<String> responseEntity = new ResponseEntity<>(
                "Not Found", HttpStatus.NOT_FOUND
        );
        when(httpClientUtil.get(anyString(), any(), eq(String.class)))
                .thenReturn(responseEntity);

        // 执行测试并验证异常
        Exception exception = assertThrows(Exception.class, () -> {
            logisticsObjectCallback.toCallback(testNotification);
        });

        assertTrue(exception.getMessage().contains("lo对象失败"),
                "异常信息应包含'lo对象失败'");

        log.info("✅ 获取物流对象失败测试通过，异常信息: {}", exception.getMessage());
    }

    /**
     * 测试获取物流对象时HTTP异常的情况
     */
    @Test
    void testToCallback_HttpClientException() {
        log.info("==================== 测试HTTP客户端异常 ====================");

        // Mock 订阅请求查询
        when(oneRecordSubscriptionsNotifyService.getOne(any()))
                .thenReturn(testSubscriptionRequest);

        // Mock 公司信息查询
        when(neOneCompanyService.getOne(any()))
                .thenReturn(testCompanyHolder);

        // Mock HTTP 客户端抛出异常
        when(httpClientUtil.get(anyString(), any(), eq(String.class)))
                .thenThrow(new RuntimeException("Connection timeout"));

        // 执行测试并验证异常
        Exception exception = assertThrows(Exception.class, () -> {
            logisticsObjectCallback.toCallback(testNotification);
        });

        assertTrue(exception.getMessage().contains("lo对象失败"),
                "异常信息应包含'lo对象失败'");

        log.info("✅ HTTP客户端异常测试通过，异常信息: {}", exception.getMessage());
    }

    /**
     * 测试物流对象类型为空的情况
     */
    @Test
    void testToCallback_EmptyLogisticsObjectType() {
        log.info("==================== 测试物流对象类型为空 ====================");

        // 构造没有物流对象类型的通知体
        String notifyBodyWithoutType = "{\n" +
                "  \"@context\": {\n" +
                "    \"api\": \"https://onerecord.iata.org/ns/api#\"\n" +
                "  },\n" +
                "  \"api:isTriggeredBy\": {\n" +
                "    \"@id\": \"http://example.com/action-requests/test-action-123\"\n" +
                "  },\n" +
                "  \"api:hasLogisticsObject\": {\n" +
                "    \"@id\": \"http://example.com/logistics-objects/test-lo-001\"\n" +
                "  }\n" +
                "}";
        testNotification.setNotifyBody(notifyBodyWithoutType);

        // Mock 订阅请求查询
        when(oneRecordSubscriptionsNotifyService.getOne(any()))
                .thenReturn(testSubscriptionRequest);

        // Mock 公司信息查询
        when(neOneCompanyService.getOne(any()))
                .thenReturn(testCompanyHolder);

        // Mock HTTP 响应
        ResponseEntity<String> responseEntity = new ResponseEntity<>("{}", HttpStatus.OK);
        when(httpClientUtil.get(anyString(), any(), eq(String.class)))
                .thenReturn(responseEntity);

        // Mock 保存操作
        when(neOneLogisticsObjectsService.save(any(NeOneLogisticsObjects.class)))
                .thenReturn(true);

        // 执行测试 - 不应抛出异常，但类型可能为空
        logisticsObjectCallback.toCallback(testNotification);

        // 验证仍然保存了记录
        verify(neOneLogisticsObjectsService, times(1)).save(any(NeOneLogisticsObjects.class));

        log.info("✅ 物流对象类型为空测试通过");
    }

    /**
     * 测试通知体中使用@id而不是@value的情况
     */
    @Test
    void testToCallback_LogisticsObjectTypeWithId() {
        log.info("==================== 测试物流对象类型使用@id ====================");

        // 构造使用 @id 而不是 @value 的通知体
        String notifyBodyWithId = "{\n" +
                "  \"@context\": {\n" +
                "    \"api\": \"https://onerecord.iata.org/ns/api#\"\n" +
                "  },\n" +
                "  \"api:isTriggeredBy\": {\n" +
                "    \"@id\": \"http://example.com/action-requests/test-action-123\"\n" +
                "  },\n" +
                "  \"api:hasLogisticsObject\": {\n" +
                "    \"@id\": \"http://example.com/logistics-objects/test-lo-001\"\n" +
                "  },\n" +
                "  \"api:hasLogisticsObjectType\": {\n" +
                "    \"@id\": \"https://onerecord.iata.org/ns/cargo#Waybill\"\n" +
                "  }\n" +
                "}";
        testNotification.setNotifyBody(notifyBodyWithId);

        // Mock 订阅请求查询
        when(oneRecordSubscriptionsNotifyService.getOne(any()))
                .thenReturn(testSubscriptionRequest);

        // Mock 公司信息查询
        when(neOneCompanyService.getOne(any()))
                .thenReturn(testCompanyHolder);

        // Mock HTTP 响应
        ResponseEntity<String> responseEntity = new ResponseEntity<>("{}", HttpStatus.OK);
        when(httpClientUtil.get(anyString(), any(), eq(String.class)))
                .thenReturn(responseEntity);

        // Mock 保存操作
        when(neOneLogisticsObjectsService.save(any(NeOneLogisticsObjects.class)))
                .thenReturn(true);

        // 执行测试
        logisticsObjectCallback.toCallback(testNotification);

        // 捕获保存的参数
        ArgumentCaptor<NeOneLogisticsObjects> captor = ArgumentCaptor.forClass(NeOneLogisticsObjects.class);
        verify(neOneLogisticsObjectsService).save(captor.capture());
        NeOneLogisticsObjects savedObject = captor.getValue();

        // 验证类型被正确解析
        assertEquals("Waybill", savedObject.getContextType());

        log.info("✅ 物流对象类型使用@id测试通过，解析类型: {}", savedObject.getContextType());
    }

    /**
     * 测试获取的bodyText为空的情况
     */
    @Test
    void testToCallback_EmptyBodyText() {
        log.info("==================== 测试获取的bodyText为空 ====================");

        // Mock 订阅请求查询
        when(oneRecordSubscriptionsNotifyService.getOne(any()))
                .thenReturn(testSubscriptionRequest);

        // Mock 公司信息查询
        when(neOneCompanyService.getOne(any()))
                .thenReturn(testCompanyHolder);

        // Mock HTTP 响应返回空字符串
        ResponseEntity<String> responseEntity = new ResponseEntity<>("", HttpStatus.OK);
        when(httpClientUtil.get(anyString(), any(), eq(String.class)))
                .thenReturn(responseEntity);

        // Mock 保存操作
        when(neOneLogisticsObjectsService.save(any(NeOneLogisticsObjects.class)))
                .thenReturn(true);

        // 执行测试
        logisticsObjectCallback.toCallback(testNotification);

        // 捕获保存的参数
        ArgumentCaptor<NeOneLogisticsObjects> captor = ArgumentCaptor.forClass(NeOneLogisticsObjects.class);
        verify(neOneLogisticsObjectsService).save(captor.capture());
        NeOneLogisticsObjects savedObject = captor.getValue();

        // 验证空bodyText被设置为"{}"
        assertEquals("{}", savedObject.getBodyText());
        assertNull(savedObject.getMawbCode());

        log.info("✅ 空bodyText测试通过");
    }
}

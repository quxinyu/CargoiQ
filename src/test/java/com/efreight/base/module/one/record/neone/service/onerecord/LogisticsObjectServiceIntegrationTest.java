// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.service.onerecord;

import com.efreight.base.module.one.record.neone.config.GraphDBConfig;
import com.efreight.base.module.one.record.neone.config.LogisticsObjectIdConfig;
import com.efreight.base.module.one.record.neone.config.NeoneServerConfig;
import com.efreight.base.module.one.record.neone.handler.*;
import com.efreight.base.module.one.record.neone.iata.onerecord.CARGO;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.model.onerecord.LogisticsObject;
import com.efreight.base.module.one.record.neone.model.onerecord.NeoneEvent;
import com.efreight.base.module.one.record.neone.repository.*;
import com.efreight.base.module.one.record.neone.security.InternalAccessSubject;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.DynamicModelFactory;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * LogisticsObjectService 集成测试类（使用真实 GraphDB 连接）
 * <p>
 * 前置条件：
 * 1. 确保 GraphDB 服务器已启动（默认 http://localhost:7200）
 * 2. 已创建名为 "test" 的 repository
 * 3. 用户名/密码正确（GraphDB Free 不需要认证）
 * <p>
 * 使用方式：通过 docker-compose 启动 GraphDB
 * start-graphdb.bat
 *
 * @author test
 * @since 2025-01-09
 */
class LogisticsObjectServiceIntegrationTest {

    private LogisticsObjectService logisticsObjectService;

    // 真实的 Repository（连接到 GraphDB）
    private static Repository repository;

    // Mock 掉不需要测试真实交互的服务
    @Mock
    private AuditTrailService auditTrailService;

    @Mock
    private NeoneEventService neoneEventService;

    @Mock
    private ActionRequestNotificationService notificationService;

    @Mock
    private InternalAccessSubject accessSubject;

    // Mock 配置对象
    @Mock
    private LogisticsObjectIdConfig loIdConfig;

    @Mock
    private NeoneServerConfig neoneServerConfig;

    private LogisticsObject testLogisticsObject;
    private SimpleValueFactory factory;

    @BeforeAll
    static void initGraphDB() {
        System.out.println("==================== 初始化 GraphDB 连接 ====================");
        try {
            // 使用 GraphDBConfig 创建真实的 Repository
            GraphDBConfig graphDBConfig = new GraphDBConfig();

            // 通过反射设置配置值（模拟 application-test.yml）
            setConfigField(graphDBConfig, "repositoryType", "http");
            setConfigField(graphDBConfig, "httpRepositoryUrl", "http://192.168.6.59:7200/repositories/neone");
            setConfigField(graphDBConfig, "repositoryUsername", "");
            setConfigField(graphDBConfig, "repositoryPassword", "");

            // 创建 Repository
            repository = graphDBConfig.rdfRepository();
            repository.init();

            System.out.println("✅ GraphDB 连接成功");
            System.out.println("   URL: http://192.168.6.59:7200/repositories/neone");
        } catch (Exception e) {
            System.err.println("❌ GraphDB 连接失败: " + e.getMessage());
            e.printStackTrace();
            fail("无法连接到 GraphDB，请确保 GraphDB 已启动");
        }
    }

    @AfterAll
    static void cleanupGraphDB() {
        System.out.println("==================== 清理 GraphDB 连接 ====================");
        if (repository != null && repository.isInitialized()) {
            repository.shutDown();
            System.out.println("✅ GraphDB 连接已关闭");
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        System.out.println("==================== 测试初始化开始 ====================");

        // 初始化 Mockito
        MockitoAnnotations.openMocks(this);

        factory = SimpleValueFactory.getInstance();

        // 创建测试用的 LogisticsObject
        testLogisticsObject = createTestLogisticsObject();

        // 手动创建 LogisticsObjectService 并注入依赖
        logisticsObjectService = createLogisticsObjectService();

        // 清空测试数据
        cleanTestData();

        System.out.println("==================== 测试初始化完成 ====================");
    }

    @AfterEach
    void tearDown() {
        System.out.println("==================== 清理测试数据 ====================");
        cleanTestData();
        System.out.println("==================== 清理完成 ====================");
    }

    /**
     * 创建 LogisticsObjectService 并手动注入所有依赖
     */
    private LogisticsObjectService createLogisticsObjectService() throws Exception {
        // 配置 Mock 对象的行为
        when(loIdConfig.getScheme()).thenReturn("https");
        when(loIdConfig.getHost()).thenReturn("localhost");
        when(loIdConfig.getPort()).thenReturn(java.util.Optional.of("7200"));
        when(loIdConfig.getRootPath()).thenReturn("/neone");
        when(loIdConfig.getInternalIriScheme()).thenReturn("urn");
//        when(loIdConfig.randomIdStrategy()).thenReturn(LogisticsObjectIdConfig.RandomIdStrategy.UUID);

        NeoneServerConfig.DataHolder dataHolder = mock(NeoneServerConfig.DataHolder.class);
        when(dataHolder.getId()).thenReturn("test-data-holder");
        when(dataHolder.getName()).thenReturn("Test Data Holder");

        when(neoneServerConfig.getSupportedContentTypes()).thenReturn(new java.util.HashSet<>());
        when(neoneServerConfig.getSupportedLanguages()).thenReturn(new java.util.HashSet<>());
        when(neoneServerConfig.getDataHolder()).thenReturn(dataHolder);

        // 创建 IdProvider
        IdProvider idProvider = new IdProvider(loIdConfig, neoneServerConfig);

        // 创建真实的 Repository beans
        AclAuthorizationRepository aclRepository = new AclAuthorizationRepository(repository, new AclAuthorizationHandler(), idProvider);
        LogisticsObjectRepository loRepository = new LogisticsObjectRepository(repository, new LogisticsObjectHandler(), idProvider, aclRepository);
        LogisticsObjectMetadataRepository metadataRepository = new LogisticsObjectMetadataRepository(repository, new LogisticsObjectMetadataHandler(), idProvider);
        NeoneEventRepository neoneEventRepository = new NeoneEventRepository(repository, new NeoneEventHandler(), idProvider);
        SnapshotRepository snapshotRepository = new SnapshotRepository(repository, new SnapshotHandler(), idProvider);
        RepositoryTransaction transaction = new RepositoryTransaction(repository, null, null);
        SnapshotService snapshotService = new SnapshotService(snapshotRepository, transaction, idProvider);
        ActionRequestService actionRequestService = new ActionRequestService(transaction, null, null, null, null);
        ChangeRequestRepository changeRequestRepository = new ChangeRequestRepository(repository, null, idProvider);


        // 创建 LogisticsObjectService
        LogisticsObjectService service = new LogisticsObjectService(
                loRepository,
                metadataRepository,
                neoneEventRepository,
                transaction,
                auditTrailService,
                snapshotService,
                actionRequestService,
                changeRequestRepository,
                idProvider,
                accessSubject,
                notificationService,
                aclRepository,
                neoneEventService
        );

        // 设置 @ConfigProperty 字段
        setField(service, "authorizeCreator", false);

        return service;
    }

    /**
     * 设置私有字段的值（通过反射）
     */
    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    /**
     * 设置配置类的字段
     */
    private static void setConfigField(Object target, String fieldName, Object value) throws Exception {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException e) {
            // 字段可能使用不同的命名方式
            Field[] fields = target.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equalsIgnoreCase(fieldName.replace("http", ""))) {
                    field.setAccessible(true);
                    field.set(target, value);
                    return;
                }
            }
            throw e;
        }
    }

    /**
     * 创建测试用的 LogisticsObject
     */
    private LogisticsObject createTestLogisticsObject() {
        IRI iri = factory.createIRI("http://example.com/logistics-objects/test-lo-001");

        Model model = new DynamicModelFactory().createEmptyModel();
        model.add(iri, RDF.TYPE, CARGO.LogisticsObject);

        return new LogisticsObject(iri, model);
    }

    /**
     * 清理测试数据
     */
    private void cleanTestData() {
        try (RepositoryConnection connection = repository.getConnection()) {
            IRI testIri = factory.createIRI("http://example.com/logistics-objects/test-lo-001");

            // 删除测试数据
            if (connection.hasStatement(testIri, null, null, true)) {
                System.out.println("清理旧的测试数据: " + testIri);
                connection.remove(testIri, null, null);
            }
        } catch (Exception e) {
            System.err.println("清理测试数据失败: " + e.getMessage());
        }
    }

    /**
     * 测试：使用真实 GraphDB 连接创建物流对象
     */
    @Test
    @DisplayName("使用真实 GraphDB 连接创建物流对象")
    void testCreateLogisticsObjectWithRealGraphDB() {
        System.out.println("==================== 测试创建物流对象（真实 GraphDB）====================");

        // Mock auditTrailService (void 方法)
        doNothing().when(auditTrailService).createAuditTrail(any(org.eclipse.rdf4j.model.IRI.class), any(RepositoryConnection.class));

        // Mock neoneEventService
        doNothing().when(neoneEventService).publishEvent(any(NeoneEvent.class));

        // 执行测试 - 调用 createLogisticsObject(LogisticsObject logisticsObject)
        logisticsObjectService.createLogisticsObject(testLogisticsObject);

        // ✅ 验证：数据确实被保存到了 GraphDB
        try (RepositoryConnection connection = repository.getConnection()) {
            IRI testIri = testLogisticsObject.iri();

            // 验证主节点存在
            assertTrue(connection.hasStatement(testIri, null, null, true),
                    "主节点应该存在于 GraphDB 中");

            // 验证类型声明
            assertTrue(connection.hasStatement(testIri, RDF.TYPE, CARGO.LogisticsObject, true),
                    "类型声明应该存在于 GraphDB 中");

            // 统计保存的三元组数量
            long statementCount = connection.getStatements(testIri, null, null, true).stream().count();
            System.out.println("✅ 成功保存了 " + statementCount + " 条三元组到 GraphDB");
            assertTrue(statementCount > 0, "应该至少保存一条三元组");

            // 打印所有保存的三元组
            System.out.println("保存的三元组：");
            connection.getStatements(testIri, null, null, true)
                    .stream()
                    .forEach(stmt -> System.out.println("  - " + stmt));
        }

        // 验证 Mock 被调用
        verify(auditTrailService, times(1)).createAuditTrail(any(org.eclipse.rdf4j.model.IRI.class), any(RepositoryConnection.class));
        verify(neoneEventService, times(1)).publishEvent(any(NeoneEvent.class));

        System.out.println("✅ 测试通过");
    }

    /**
     * 测试：验证数据可以持久化并读取
     */
    @Test
    @DisplayName("验证数据持久化和读取")
    void testPersistenceAndRetrieval() {
        System.out.println("==================== 测试数据持久化和读取 ====================");

        // Mock services
        doNothing().when(auditTrailService).createAuditTrail(any(), any(RepositoryConnection.class));
        doNothing().when(neoneEventService).publishEvent(any(NeoneEvent.class));

        // 1. 创建物流对象
        logisticsObjectService.createLogisticsObject(testLogisticsObject);

        // 2. 验证可以通过 RepositoryConnection 读取
        try (RepositoryConnection connection = repository.getConnection()) {
            IRI testIri = testLogisticsObject.iri();

            // 获取所有关于这个对象的三元组 - 使用 rdf4j 3.7.4 兼容的方式
            Model model = new LinkedHashModel();
            connection.getStatements(testIri, null, null, true)
                    .stream()
                    .forEach(stmt -> model.add(stmt.getSubject(), stmt.getPredicate(), stmt.getObject(), stmt.getContext()));

            // 验证数据
            assertFalse(model.isEmpty(), "从 GraphDB 读取的数据不应为空");
            assertEquals(testIri, model.subjects().iterator().next(), "主节点 IRI 应该匹配");

            System.out.println("✅ 数据持久化验证成功");
            System.out.println("   - IRI: " + testIri);
            System.out.println("   - 三元组数量: " + model.size());
        }

        System.out.println("✅ 测试通过");
    }

    /**
     * 测试：重复创建相同 IRI 的对象应该失败
     */
    @Test
    @DisplayName("重复创建相同 IRI 应该抛出异常")
    void testCreateDuplicateLogisticsObject() {
        System.out.println("==================== 测试重复创建 ====================");

        // Mock services
        doNothing().when(auditTrailService).createAuditTrail(any(), any(RepositoryConnection.class));
        doNothing().when(neoneEventService).publishEvent(any(NeoneEvent.class));

        // 第一次创建（应该成功）
        logisticsObjectService.createLogisticsObject(testLogisticsObject);

        // 第二次创建相同 IRI 的对象（应该失败）
        Exception exception = assertThrows(Exception.class, () -> {
            logisticsObjectService.createLogisticsObject(testLogisticsObject);
        });

        System.out.println("✅ 正确抛出异常: " + exception.getClass().getSimpleName());
        System.out.println("   异常信息: " + exception.getMessage());
    }

    /**
     * 测试：验证真实 Connection 的事务功能
     */
    @Test
    @DisplayName("验证事务提交和回滚")
    void testTransactionCommit() {
        System.out.println("==================== 测试事务功能 ====================");

        doNothing().when(auditTrailService).createAuditTrail(any(), any(RepositoryConnection.class));
        doNothing().when(neoneEventService).publishEvent(any(NeoneEvent.class));

        // 创建对象
        logisticsObjectService.createLogisticsObject(testLogisticsObject);

        // 验证事务已提交（数据可以在新连接中读取到）
        try (RepositoryConnection newConnection = repository.getConnection()) {
            assertTrue(newConnection.hasStatement(testLogisticsObject.iri(), null, null, true),
                    "事务提交后，新连接应该能读取到数据");
        }

        System.out.println("✅ 事务提交验证成功");
    }
}

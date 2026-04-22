// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.service.onerecord;

import com.efreight.base.module.one.record.neone.iata.onerecord.CARGO;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.model.onerecord.LogisticsObject;
import com.efreight.base.module.one.record.neone.model.onerecord.NeoneEvent;
import com.efreight.base.module.one.record.neone.model.onerecord.Snapshot;
import com.efreight.base.module.one.record.neone.repository.*;
import com.efreight.base.module.one.record.neone.security.InternalAccessSubject;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.DynamicModelFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * LogisticsObjectService 单元测试类
 *
 * @author test
 * @since 2025-01-09
 */
@ExtendWith(MockitoExtension.class)
class LogisticsObjectServiceTest {

    @Mock
    private LogisticsObjectRepository loRepository;

    @Mock
    private LogisticsObjectMetadataRepository metadataRepository;

    @Mock
    private NeoneEventRepository neoneEventRepository;

    @Mock
    private RepositoryTransaction transaction;

    @Mock
    private AuditTrailService auditTrailService;

    @Mock
    private SnapshotService snapshotService;

    @Mock
    private ActionRequestService actionRequestService;

    @Mock
    private ChangeRequestRepository changeRequestRepository;

    @Mock
    private IdProvider idProvider;

    @Mock
    private InternalAccessSubject accessSubject;

    @Mock
    private ActionRequestNotificationService notificationService;

    @Mock
    private AclAuthorizationRepository aclRepository;

    @Mock
    private NeoneEventService neoneEventService;

    @InjectMocks
    private LogisticsObjectService logisticsObjectService;

    private LogisticsObject testLogisticsObject;
    private RepositoryConnection mockConnection;
    private RepositoryTransaction.RepositoryTransactionHooks mockHooks;

    @BeforeEach
    void setUp() throws Exception {
        // 初始化 Mock 对象
        mockConnection = mock(RepositoryConnection.class);
        mockHooks = mock(RepositoryTransaction.RepositoryTransactionHooks.class);

        // 创建测试用的 LogisticsObject
        testLogisticsObject = createTestLogisticsObject();

        // 使用反射设置 @ConfigProperty 注入的字段
        setField(logisticsObjectService, "authorizeCreator", false);
    }

    /**
     * 使用反射设置私有字段
     */
    private void setField(Object target, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    /**
     * 创建测试用的 LogisticsObject
     */
    private LogisticsObject createTestLogisticsObject() {
        // 使用 SimpleValueFactory 创建 IRI (rdf4j 3.7.4 兼容)
        SimpleValueFactory factory = SimpleValueFactory.getInstance();
        IRI iri = factory.createIRI("http://example.com/logistics-objects/test-lo-001");

        // 使用 DynamicModelFactory 创建 Model (rdf4j 3.7.4 兼容)
        Model model = new DynamicModelFactory().createEmptyModel();
        model.add(iri, RDF.TYPE, CARGO.LogisticsObject);

        // 创建 LogisticsObject
        return new LogisticsObject(iri, model);
    }

    /**
     * 测试正常创建物流对象
     */
    @Test
    void testCreateLogisticsObject() {
        System.out.println("==================== 测试创建物流对象 ====================");

        // Mock transaction.transactionallyDo 方法
        // 使用 BiConsumer 来模拟真实调用
        doAnswer(invocation -> {
            java.util.function.BiConsumer<RepositoryConnection, RepositoryTransaction.RepositoryTransactionHooks> callback =
                    (java.util.function.BiConsumer<RepositoryConnection, RepositoryTransaction.RepositoryTransactionHooks>) invocation.getArguments()[0];
            callback.accept(mockConnection, mockHooks);
            return null;
        }).when(transaction).transactionallyDo(any(java.util.function.BiConsumer.class));

        // Mock IdProvider - 使用 doReturn 而不是 when
        SimpleValueFactory factory = SimpleValueFactory.getInstance();
        org.eclipse.rdf4j.model.IRI baseIri = factory.createIRI("http://example.com/logistics-objects");
        org.eclipse.rdf4j.model.IRI internalIri = factory.createIRI("urn:internal:123");

        doReturn(baseIri).when(idProvider).getLogisticsObjectBaseIri();
        doReturn(NeoneId.fromIri(internalIri)).when(idProvider).createInternalIri();

        // Mock loRepository.persist (void 方法)
        doNothing().when(loRepository).persist(eq(testLogisticsObject), eq(mockConnection));

        // Mock metadataRepository (void 方法)
        doNothing().when(metadataRepository).persist(any(com.efreight.base.module.one.record.neone.model.onerecord.LogisticsObjectMetadata.class), eq(mockConnection));

        // Mock snapshotService.createSnapshot (返回 Snapshot 对象)
        Snapshot mockSnapshot = mock(Snapshot.class);
        doReturn(mockSnapshot).when(snapshotService).createSnapshot(eq(testLogisticsObject), eq(1), eq(mockConnection));

        // Mock auditTrailService (void 方法)
        doNothing().when(auditTrailService).createAuditTrail(any(org.eclipse.rdf4j.model.IRI.class), eq(mockConnection));

        // Mock neoneEventRepository (返回 NeoneEvent 对象)
        NeoneEvent mockEvent = mock(NeoneEvent.class);
        doReturn(mockEvent).when(neoneEventRepository).addEvent(any(), any(), any(), any(), any(), eq(mockConnection));

        // Mock aclRepository (void 方法)
        doNothing().when(aclRepository).grantDefaultAccess(any(org.eclipse.rdf4j.model.IRI.class), any(org.eclipse.rdf4j.model.IRI.class), eq(false), eq(mockConnection));

        // 执行测试 - 调用 createLogisticsObject(LogisticsObject logisticsObject)
        logisticsObjectService.createLogisticsObject(testLogisticsObject);

        // 验证调用
        verify(transaction, times(1)).transactionallyDo(any(java.util.function.BiConsumer.class));
        verify(loRepository, times(1)).persist(eq(testLogisticsObject), eq(mockConnection));
        verify(metadataRepository, times(1)).persist(any(com.efreight.base.module.one.record.neone.model.onerecord.LogisticsObjectMetadata.class), eq(mockConnection));
        verify(snapshotService, times(1)).createSnapshot(eq(testLogisticsObject), eq(1), eq(mockConnection));
        verify(auditTrailService, times(1)).createAuditTrail(any(org.eclipse.rdf4j.model.IRI.class), eq(mockConnection));
        verify(neoneEventRepository, times(1)).addEvent(any(), any(), any(), any(), any(), eq(mockConnection));
        verify(aclRepository, times(1)).grantDefaultAccess(any(org.eclipse.rdf4j.model.IRI.class), any(), eq(false), eq(mockConnection));

        System.out.println("✅ 测试通过");
    }
}

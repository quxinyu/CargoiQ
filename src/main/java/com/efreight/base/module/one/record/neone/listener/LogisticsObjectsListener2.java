package com.efreight.base.module.one.record.neone.listener;

import com.efreight.base.api.feign.IStudioApiService;
import com.efreight.base.api.vo.NeoneNotifyEbaseVO;
import com.efreight.base.common.core.utils.SpringUtils;
import com.efreight.base.module.one.record.neone.model.entity.NeOneLogisticsObjects;
import com.efreight.base.module.one.record.neone.model.entity.NeOneSubscriptions;
import com.efreight.base.module.one.record.neone.service.OneRecordResolvedLogisticsObjectService;
import com.efreight.base.module.one.record.neone.service.SubscriptionsService;
import com.google.common.eventbus.Subscribe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fu yuan hui
 * @since 2024-09-18 14:45:13 星期三
 */
@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor
public class LogisticsObjectsListener2 {

    private final OneRecordResolvedLogisticsObjectService resolvedDataService;

    private final IStudioApiService iStudioApiService;

    @Subscribe
    public void listenerLogisticsObjectCreated(NeOneLogisticsObjects base) {
//        if (LoModuleType.LOGISTICS_COMPANY.name().equals(base.getLoModuleType())) {
//            return;
//        }
//        String bodyText = base.getBodyText();
//        NeOneResolvedData loRecord = ReverseResolveOneRecordBodyTools.parse(bodyText);
//        loRecord.setNeOneLogisticsObjectsId(base.getId());
//        NeOneResolvedLogisticsObject neOneResolvedFromOneRecord = NeOneResolvedLogisticsObject.create(loRecord);
//        neOneResolvedFromOneRecord.setLoIri(base.getIri());
//        neOneResolvedFromOneRecord.setLoId(base.getIri().substring(base.getIri().lastIndexOf("/") + 1));
//        neOneResolvedFromOneRecord.setLoModuleType(LoModuleType.LOGISTICS_OBJECT.name());
//        this.resolvedDataService.save(neOneResolvedFromOneRecord);

        List<NeOneSubscriptions> subscriptions = getSubscriptions(base);
        if (CollectionUtils.isEmpty(subscriptions)) {
            log.warn(">>>>>>>>>>>>>>>>>>>>没有订阅者, 当前物流对象无需发送通知 :{} ", base);
            return;
        }
        // TODO 查看下游是否还有订阅 暂时只考虑ebase
        subscriptions = subscriptions.stream()
                .filter(bean -> ObjectUtils.isNotEmpty(bean.getEbaseFlowId()))
                .collect(Collectors.toList());
        int successCount = 0;
        int failCount = 0;
        for (NeOneSubscriptions sub : subscriptions) {
            try {
                NeoneNotifyEbaseVO neoneNotifyEbaseVO = new NeoneNotifyEbaseVO();
                neoneNotifyEbaseVO.setFlowId(sub.getEbaseFlowId());
                neoneNotifyEbaseVO.setContent(base.getBodyText());
                iStudioApiService.neoneNotifyEbase(neoneNotifyEbaseVO);
                successCount++;
                log.info(">>>>>>>>>>>>>>>>>>>>订阅者通知成功, flowId:{}, loId:{}", sub.getEbaseFlowId(), base.getLoId());
            } catch (Exception e) {
                failCount++;
                log.error(">>>>>>>>>>>>>>>>>>>>订阅者通知失败, flowId:{}, loId:{}, 错误信息:{}", sub.getEbaseFlowId(), base.getLoId(), e.getMessage(), e);
                // 继续通知下一个订阅者，不中断循环
            }
        }
        log.info(">>>>>>>>>>>>>>>>>>>>物流对象通知完成, loId:{}, 总订阅数:{}, 成功:{}, 失败:{}",
                base.getLoId(), subscriptions.size(), successCount, failCount);

    }

    /**
     * 获取订阅信息
     *
     * @return
     */
    private List<NeOneSubscriptions> getSubscriptions(NeOneLogisticsObjects neOneLogisticsObjects) {
        SubscriptionsService subscriptionsService = SpringUtils.getBean("subscriptionsServiceImpl");
        return subscriptionsService.getSubscriptions(neOneLogisticsObjects);
    }
}

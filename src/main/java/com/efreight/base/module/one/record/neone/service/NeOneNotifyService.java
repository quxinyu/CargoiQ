package com.efreight.base.module.one.record.neone.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.efreight.base.module.one.record.neone.model.entity.NeOneNotifications;
import com.efreight.base.module.one.record.neone.model.vo.NeOneNotificationsVO;

/**
 * 接收通知 service接口
 *
 * @author fu yuan hui
 * @since 2024-09-11 17:28:20 星期三
 */
public interface NeOneNotifyService extends IService<NeOneNotifications> {

    /**
     * 接收外部系统通知
     *
     * @param notifyBody 通知体
     */
    void receiveNotify(String notifyBody);

    IPage<?>  notifyPage(NeOneNotificationsVO requestParam);

    String notifyDetails(Long id);
}

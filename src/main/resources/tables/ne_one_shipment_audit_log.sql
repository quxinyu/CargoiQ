CREATE TABLE IF NOT EXISTS `ne_one_shipment_audit_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `shipment_data_ids` VARCHAR(512) DEFAULT NULL COMMENT '本次操作涉及的shipment数据ID，多个逗号分隔',
  `operate_no` VARCHAR(512) DEFAULT NULL COMMENT '操作单号，多个逗号分隔',
  `operation_type` VARCHAR(64) NOT NULL COMMENT '操作类型：SEND_CHECK/CHECK/AUTO_CHECK',
  `operator_name` VARCHAR(64) NOT NULL COMMENT '操作人',
  `operator_ip` VARCHAR(64) DEFAULT NULL COMMENT '操作IP',
  `result_status` VARCHAR(32) NOT NULL COMMENT '结果状态：SUCCESS/FAIL',
  `result_message` VARCHAR(1000) DEFAULT NULL COMMENT '结果摘要或失败原因',
  `request_body` TEXT COMMENT '原始请求体JSON',
  `trace_id` VARCHAR(128) DEFAULT NULL COMMENT '链路追踪ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_shipment_data_ids` (`shipment_data_ids`),
  KEY `idx_operate_no` (`operate_no`),
  KEY `idx_operation_type` (`operation_type`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='货代运单核验审计日志';

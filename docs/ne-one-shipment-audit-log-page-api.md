# 审计记录分页接口文档

## 接口说明

- 接口名称：审计记录分页查询
- Controller：`NeOneShipmentAuditLogController`
- 方法：`page`
- 请求方式：`GET`
- 实际路径：`/freight-forwarder/audit-log/page`
- 前端兼容路径：`/api/freight-forwarder/audit-log/page`

用途：

- 根据操作单号、操作人分页查询 `sendCheck`、`check`、`autoCheck` 产生的审计记录。

代码位置：

- Controller:
  [`/Users/quxinyu/claude-sandbox/eft-module-cargoid/src/main/java/com/efreight/base/module/one/record/neone/controller/NeOneShipmentAuditLogController.java`](/Users/quxinyu/claude-sandbox/eft-module-cargoid/src/main/java/com/efreight/base/module/one/record/neone/controller/NeOneShipmentAuditLogController.java:1)
- 请求对象:
  [`/Users/quxinyu/claude-sandbox/eft-module-cargoid/src/main/java/com/efreight/base/module/one/record/neone/model/request/NeOneShipmentAuditLogRequest.java`](/Users/quxinyu/claude-sandbox/eft-module-cargoid/src/main/java/com/efreight/base/module/one/record/neone/model/request/NeOneShipmentAuditLogRequest.java:1)
- 返回记录实体:
  [`/Users/quxinyu/claude-sandbox/eft-module-cargoid/src/main/java/com/efreight/base/module/one/record/neone/model/entity/NeOneShipmentAuditLog.java`](/Users/quxinyu/claude-sandbox/eft-module-cargoid/src/main/java/com/efreight/base/module/one/record/neone/model/entity/NeOneShipmentAuditLog.java:1)

## 请求参数说明

说明：

- 该接口为 `GET` 接口，不使用 JSON body。
- 参数通过 query string 传递。

### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `current` | `long` | 否 | 当前页码，默认 `1` |
| `size` | `long` | 否 | 每页条数，默认 `10` |
| `operateNo` | `string` | 否 | 操作单号，支持模糊查询 |
| `operatorName` | `string` | 否 | 操作人，支持模糊查询 |

### 请求参数来源对象说明

请求参数对象为 `NeOneShipmentAuditLogRequest`，继承 `BaseParam`：

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| `current` | `long` | 当前页码 |
| `size` | `long` | 每页条数 |
| `total` | `long` | 分页总数占位字段，请求时一般无需传 |
| `operateNo` | `string` | 操作单号 |
| `operatorName` | `string` | 操作人 |

## 请求示例

### 示例 1：查询第一页

```http
GET /freight-forwarder/audit-log/page?current=1&size=10
```

### 示例 2：按单号查询

```http
GET /freight-forwarder/audit-log/page?current=1&size=10&operateNo=123
```

### 示例 3：按操作人查询

```http
GET /api/freight-forwarder/audit-log/page?current=1&size=10&operatorName=gha
```

## 返回结构说明

接口返回统一使用 `Result<?>` 包装，结构如下：

```json
{
  "code": "200",
  "message": "success",
  "data": {
    "records": [],
    "total": 0,
    "size": 10,
    "current": 1,
    "pages": 0
  }
}
```

### 顶层返回字段说明

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| `code` | `string` | 返回状态码，`200` 表示成功 |
| `message` | `string` | 返回消息 |
| `data` | `object` | 分页结果对象 |

### data 分页字段说明

`data` 为 MyBatis-Plus 分页对象，当前接口重点关注以下字段：

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| `records` | `array` | 当前页数据列表 |
| `total` | `long` | 总记录数 |
| `size` | `long` | 每页条数 |
| `current` | `long` | 当前页码 |
| `pages` | `long` | 总页数 |

### records 数据项说明

`records` 中每一项对应一条审计记录，字段来源于 `NeOneShipmentAuditLog`：

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| `id` | `long` | 审计记录主键 |
| `shipmentDataIds` | `string` | 本次操作涉及的 `NeOneShipmentData` 主键 ID，多个以逗号分隔 |
| `operateNo` | `string` | 操作单号，多个以逗号分隔 |
| `operationType` | `string` | 操作类型，可选值：`SEND_CHECK`、`CHECK`、`AUTO_CHECK` |
| `operatorName` | `string` | 操作人，当前固定为 `gha` |
| `operatorIp` | `string` | 操作来源 IP |
| `resultStatus` | `string` | 操作结果，可选值：`SUCCESS`、`FAIL` |
| `resultMessage` | `string` | 结果摘要或失败原因 |
| `requestBody` | `string` | 原始请求体 JSON 字符串 |
| `traceId` | `string` | 链路追踪 ID |
| `createTime` | `string(datetime)` | 审计记录创建时间，格式：`yyyy-MM-dd HH:mm:ss` |

## 返回示例

```json
{
  "code": "200",
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "shipmentDataIds": "101,102",
        "operateNo": "999-12345675,999-12345676",
        "operationType": "CHECK",
        "operatorName": "gha",
        "operatorIp": "127.0.0.1",
        "resultStatus": "SUCCESS",
        "resultMessage": "total=2,success=1,fail=1",
        "requestBody": "[{\"id\":\"101\",\"checkCode\":\"E001\",\"checkDescription\":\"字段校验失败\",\"checkResult\":\"0\"}]",
        "traceId": "2d9b2d8c2d7f4e55a9d1",
        "createTime": "2026-04-25 11:30:25"
      }
    ],
    "total": 1,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

## 补充说明

- `operateNo`、`operatorName` 当前均为模糊查询。
- 返回结果按 `createTime` 倒序排列，最新审计记录排在前面。
- 如果前端统一走 `/api` 前缀，请使用兼容路径：
  `/api/freight-forwarder/audit-log/page`

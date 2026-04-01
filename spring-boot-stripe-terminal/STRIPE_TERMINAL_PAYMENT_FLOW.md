# Stripe Terminal S710 支付流程说明

## 1. 方案概览

本项目采用 Stripe Terminal 的 Server-Driven 集成模式，S710 作为刷卡设备，后端负责创建并驱动支付流程。

核心思路：

- 业务前端发起收银请求
- 后端创建 `PaymentIntent`（`card_present`）
- 后端调用 Reader API 让 S710 执行收款
- 通过 Webhook 或轮询确认支付结果
- 按需执行手动捕获（`manual capture`）

## 2. 一次性初始化流程

### 2.1 配置密钥

配置文件 `src/main/resources/config/application.properties` 中需要配置：

- `stripe.api-key`
- `stripe.webhook-secret`
- `stripe.terminal.location-id`（可选，建议配置）

### 2.2 创建 Location

调用：

- `POST /api/terminal/locations`

返回的 `tml_xxx` 即 Terminal Location ID，可作为默认门店配置。

### 2.3 注册 S710 读卡器

调用：

- `POST /api/terminal/readers`

参数中的 `registrationCode` 为 S710 屏幕上的配对码，绑定后可在 Stripe Terminal 中管理该设备。

### 2.4 验证设备状态

调用：

- `GET /api/terminal/readers`
- `GET /api/terminal/readers/{readerId}`

确认 Reader `status` 为 `online` 后再发起支付。

## 3. 正常支付主流程

### Step 1: 创建并发起支付

前端调用：

- `POST /api/terminal/payments`

关键参数：

- `amount`：金额，单位分
- `currency`：币种，如 `usd`
- `readerId`：目标 S710 设备 ID
- `captureMethod`：`manual` 或 `automatic`
- `referenceId`：业务订单号（建议强制唯一）

### Step 2: S710 收款交互

后端将支付任务下发给 S710，设备展示收款界面，顾客完成刷卡/插卡/挥卡。

### Step 3: 支付结果确认

推荐方式：Webhook（实时且可靠）

- `terminal.reader.action_succeeded`
- `terminal.reader.action_failed`
- `payment_intent.amount_capturable_updated`
- `payment_intent.succeeded`
- `payment_intent.payment_failed`

兜底方式：轮询

- `GET /api/terminal/readers/{readerId}`
- `GET /api/terminal/payments/{paymentIntentId}`

### Step 4: 手动捕获（仅 manual 模式）

若 `captureMethod=manual`，授权成功后调用：

- `POST /api/terminal/payments/capture`

完成资金最终入账。

## 4. 异常与补偿流程

### 4.1 取消当前设备动作

调用：

- `POST /api/terminal/readers/cancel-action`

用于顾客取消、收银员中断、设备界面需重置等场景。

### 4.2 常见失败类型

- 读卡器离线
- 用户取消
- 发卡行拒绝
- 网络超时
- Reader busy

处理建议：

- 记录 Stripe 失败码与失败信息
- 业务订单状态进入“待重试”或“已失败”
- 避免创建重复 PaymentIntent 导致重复扣款风险

## 5. 现有接口清单

### Reader / Location

- `POST /api/terminal/locations`
- `GET /api/terminal/locations`
- `POST /api/terminal/readers`
- `GET /api/terminal/readers`
- `GET /api/terminal/readers/{readerId}`
- `POST /api/terminal/readers/simulated`（测试）
- `POST /api/terminal/readers/simulate-payment`（测试）

### Payment

- `POST /api/terminal/payments`
- `GET /api/terminal/payments/{paymentIntentId}`
- `POST /api/terminal/payments/capture`
- `POST /api/terminal/readers/cancel-action`

### Webhook

- `POST /api/webhook/stripe`

## 6. 落地建议

- 订单号 `referenceId` 保证全局唯一
- Webhook 处理必须做幂等（按 `event.id` 去重）
- 支付状态机建议：`INIT` -> `PROCESSING` -> `AUTHORIZED` -> `CAPTURED` / `FAILED`
- `manual capture` 场景增加超时补偿任务
- 正式环境不要使用测试密钥，密钥通过环境变量注入

## 7. 页面与调试入口

- POS 页面：`http://localhost:8089/` 或 `http://localhost:8089/index`
- 当前项目已移除 Swagger，建议通过 Postman 或前端页面联调


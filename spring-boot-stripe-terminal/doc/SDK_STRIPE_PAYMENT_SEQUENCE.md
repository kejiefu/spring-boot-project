# Stripe Terminal SDK 页面：支付时序图（Mermaid）

本文档描述 `GET /sdk` 示例页（`sdk.html` + `sdk-pos.js`）与后端 `/api/terminal`、Stripe 云端、Terminal JS SDK、读卡器之间的交互顺序。

---

## 1. 页面加载与 SDK 初始化（含 connection-token）

```mermaid
sequenceDiagram
    autonumber
    participant U as 用户
    participant B as 浏览器 sdk-pos.js
    participant P as PageController
    participant API as TerminalController
    participant TS as TerminalService
    participant SA as Stripe API
    participant SDK as Terminal JS SDK
    participant R as 读卡器

    U->>B: 打开 /sdk
    B->>P: GET /sdk
    P-->>B: 渲染 sdk.html

    B->>API: GET /api/terminal/sdk/config
    API->>TS: getSdkDiscoverConfig()
    TS-->>API: defaultLocationId, simulated
    API-->>B: ResultVO.data

    U->>B: 点击「初始化 SDK」
    B->>SDK: StripeTerminal.create(onFetchConnectionToken)
    SDK->>B: 回调请求 connection token
    B->>API: POST /api/terminal/connection-token
    API->>TS: createConnectionToken()
    TS->>SA: ConnectionToken.create
    SA-->>TS: secret
    TS-->>API: secret
    API-->>B: ResultVO.data.secret
    B->>SDK: 返回 secret
    SDK-->>B: SDK 就绪

    U->>B: 发现读卡器 / 连接
    B->>SDK: discoverReaders(simulated, location?)
    SDK->>SA: 发现终端
    SA-->>SDK: discoveredReaders
    SDK-->>B: 列表展示

    B->>SDK: connectReader(reader)
    SDK->>R: 建立连接
    R-->>SDK: connected
    SDK-->>B: 已连接读卡器
```

---

## 2. 收款主流程（创建 PI → collect → process → 可选 capture / 查询）

```mermaid
sequenceDiagram
    autonumber
    participant U as 用户
    participant B as 浏览器 sdk-pos.js
    participant API as TerminalController
    participant TS as TerminalService
    participant SA as Stripe API
    participant SDK as Terminal JS SDK
    participant R as 读卡器

    U->>B: 点击「创建意图并收款」
    B->>API: POST /api/terminal/payments/intents
    Note right of B: amount(分), currency, description, referenceId, captureMethod

    API->>TS: createPaymentIntentForSdk(...)
    TS->>SA: PaymentIntent.create(card_present)
    SA-->>TS: PaymentIntent(id, client_secret, ...)
    TS-->>API: PaymentIntent
    API-->>B: ResultVO.data

    B->>SDK: collectPaymentMethod(client_secret 字符串)
    SDK->>R: 提示刷卡/插卡/挥卡
    R-->>SDK: 采集卡信息
    SDK-->>B: paymentIntent

    B->>SDK: processPayment(paymentIntent)
    SDK->>SA: 处理授权/扣款
    SA-->>SDK: 更新后 PaymentIntent
    SDK-->>B: status 如 succeeded / requires_capture

    alt captureMethod 为 manual 且需入账
        U->>B: 点击「手动 Capture」
        B->>API: POST /api/terminal/payments/capture
        API->>TS: capturePayment(paymentIntentId)
        TS->>SA: PaymentIntent.capture
        SA-->>TS: PaymentIntent
        TS-->>API: PaymentIntent
        API-->>B: ResultVO.data
    end

    opt 查询 PaymentIntent 状态
        U->>B: 点击「查询状态」
        B->>API: GET /api/terminal/payments/{paymentIntentId}
        API->>TS: getPaymentIntent(id)
        TS->>SA: PaymentIntent.retrieve
        SA-->>TS: PaymentIntent
        TS-->>API: PaymentIntent
        API-->>B: ResultVO.data
    end
```

---

## 3. 说明

- **实线 HTTP**：仅上述路径会到达本 Spring 应用；`discoverReaders` / `collectPaymentMethod` / `processPayment` 与 Stripe 的交互由 **Terminal JS SDK** 在浏览器侧发起，不经过 `/api/terminal` 除已列接口外的其它路径。
- **与 Server-Driven 区别**：SDK 页不使用 `POST /api/terminal/payments`（后端直接 `processPaymentIntent` 到读卡器），而是先创建 PI 再由前端 SDK 驱动收款。

# build-up 项目开发注意事项

### 入参定义
- `POST/PUT` 方法的 `@RequestBody` 如果定义成 `bean` 可以用 `spring validation` 验证参数, 例如 `@NotNull`
- 注意 `spring validation` 注解只有在第一层上有效, 多层参数要验证的可以用下面方法
- 如果定义的 `bean` 实现了接口 `IRequest`, 则框架会自动调用该方法去验证参数, 不需要在代码中显式调用

### 出参定义(统一异常处理)
- 端点发生未处理异常时, 框架会根据端点返回值类型 查找 `fail(...)` 方法(查找条件看下方), 如果找到了, 反射执行该方法构造一个实例, 返回给客户端
- 如果未找到则会调用 `DefaultResponse.fail()` 构造一个实例返回给客户端
- 如果特殊情况返回值无法定义 `bean`, 例如直接写 `http response` 或以前代码返回 `Map` 的情况, 可以通过注解 `@FallbackResponse` 指定异常时调用哪个类里的 `fail(...)` 方法
- 注解 `@FallbackResponse` 优先级比方法返回值优先级高. 如果添加了注解, 则优先执行注解提供的类型的 `fail(...)` 方法.
- 如果注解的类型中没有定义 `fail(...)` 方法, 则使用 `DefaultResponse.fail()` 方法

### 日志记录
- 作为服务端收到的请求和返回的响应的报文`不需要`编码记录, 框架自动记录
- 作为客户端通过 `feign`, `restTemplate`, `JsonClientUtils`, `XmlClientUtils`, `HttpUtil` 请求外部接口发送和收到的报文`不需要`编码记录, 框架自动记录
- 通过第三方 SDK 例如阿里云请求外部接口的报文`需要`编码记录
- RabbitMQ 生产消费的报文`不需要`编码记录, 框架自动记录

#### `fail(...)` 方法条件
- 静态方法
- 方法名为 fail
- 返回值类型不能为 void
- 两个入参
- 第一个参数类型必须为 int, Integer, String 中的一个
- 第二个参数类型必须为 String

- 示例
```
private static Object fail(int code, String msg)
private static String fail(Integer code, String msg)
public static RsResponse fail(String code, String msg)
```

#### 调用创客
- 使用 HttpUtil.httpPostCk() 方法
- 如果需要签名使用第二个重载, 不要在业务代码中进行签名, 该功能自动执行
- 业务参数`不支持`传入 String, `支持`使用 Bean, Map, JSONObject

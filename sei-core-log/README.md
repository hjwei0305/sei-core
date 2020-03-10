# slf4j-spring-boot-starter

<p align="center">
    <img src="https://img.shields.io/badge/JDK-1.8+-green.svg" />
    <img src="https://img.shields.io/badge/Current%20Version-v1.2.0-brightgreen" />
    <img src="https://img.shields.io/:License-apache-yellowgreen.svg" />
    <a href='https://gitee.com/xsxgit/slf4j-spring-boot-starter/stargazers'>
        <img src='https://gitee.com/xsxgit/slf4j-spring-boot-starter/badge/star.svg?theme=dark' alt='star' />
    </a>
</p>

### 介绍
一个注解搞定日志的组件，减少到处编写日志的烦恼，还可定位代码哟

### 软件架构
依赖spring-boot-starter-aop

### 原理说明
AOP + Reflect

### 作用范围
任意由**spring**调用的方法

### 当前版本
*1.2.0* (已提交中央仓库)

### 安装教程

```
mvn clean install
```

### 使用说明

#### 一、准备工作
1. 添加依赖：

```
<dependency>
    <groupId>wiki.xsx</groupId>
    <artifactId>slf4j-spring-boot-starter</artifactId>
    <version>1.2.0</version>
</dependency>
```
2. 开启日志：

yml方式：

```yaml
logging:
  level:
    wiki.xsx.core: 对应级别
```
properties方式：

```properties
logging.level.wiki.xsx.core=对应级别
```

#### 二、开始使用（使用DEBUG模式）
##### @ParamLog注解示例：

1. 日志注解标记：
```java
@RestController
public class TestParamLogController {

    @ParamLog(value = "ParamLog-test1")
    @GetMapping("/paramLogTest1")
    public Map<String, Object> logTest1(HttpServletRequest request, Map<String, Object> param) {
        Map<String, Object> result = new HashMap<>(3);
        result.put("code", 200);
        result.put("msg", "success");
        result.put("data", param);
        return result;
    }

    @ParamLog(value = "ParamLog-test2", paramFilter = {"request"})
    @GetMapping("/paramLogTest2")
    public Map<String, Object> logTest2(HttpServletRequest request, Map<String, Object> param) {
        Map<String, Object> result = new HashMap<>(3);
        result.put("code", 200);
        result.put("msg", "success");
        result.put("data", param);
        return result;
    }

    @ParamLog(value = "ParamLog-test3", paramFilter = {"request"}, callback = LogTestCallback.class)
    @GetMapping("/paramLogTest3")
    public Map<String, Object> logTest3(HttpServletRequest request, List<Object> param) {
        Map<String, Object> result = new HashMap<>(3);
        result.put("code", 200);
        result.put("msg", "success");
        result.put("data", param);
        return result;
    }
}
```
2. 日志回调声明（实现wiki.xsx.core.log.LogCallback接口）：
```java
// 加入IOC容器，否则调用失败
@Component
@Slf4j
public class LogTestCallback implements LogCallback {
    @Override
    public void callback(Annotation annotation, MethodInfo methodInfo, Map<String, Object> paramMap, Object result) {
        log.info(methodInfo.getClassAllName()+"."+methodInfo.getMethodName()+"方法的回调函数执行成功");
    }
}
```
3. 测试：
```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestParamLogControllerTest {

    @Autowired
    private TestParamLogController testParamLogController;

    @Test
    public void test() {
        List<Object> list = new ArrayList<>(2);
        list.add("test-list");
        Map<String, Object> paramMap = new LinkedHashMap<>(5);
        paramMap.put("key1", "hello-world");
        paramMap.put("key2", 11);
        paramMap.put("key3", 11.11D);
        paramMap.put("key4", new String[]{"hello", "world"});
        paramMap.put("key5", new Number[]{111, 112, 113});
        list.add(paramMap);
        this.testParamLogController.logTest1(null, paramMap);
        this.testParamLogController.logTest2(null, paramMap);
        this.testParamLogController.logTest3(null, list);
    }
}
```
4. 日志打印效果：
```
2020-01-16 21:51:11.932 DEBUG 27340 --- [           main] wiki.xsx.core.log.LogProcessor           : 调用方法：【wiki.xsx.log.controller.TestParamLogController.logTest1(TestParamLogController.java:23)】，业务名称：【ParamLog-test1】，接收参数：【{request=null, param={key1=hello-world, key2=11, key3=11.11, key4=[hello, world], key5=[111, 112, 113]}}】
2020-01-16 21:51:11.939 DEBUG 27340 --- [           main] wiki.xsx.core.log.LogProcessor           : 调用方法：【wiki.xsx.log.controller.TestParamLogController.logTest2(TestParamLogController.java:33)】，业务名称：【ParamLog-test2】，接收参数：【{param={key1=hello-world, key2=11, key3=11.11, key4=[hello, world], key5=[111, 112, 113]}}】
2020-01-16 21:51:11.940 DEBUG 27340 --- [           main] wiki.xsx.core.log.LogProcessor           : 调用方法：【wiki.xsx.log.controller.TestParamLogController.logTest3(TestParamLogController.java:43)】，业务名称：【ParamLog-test3】，接收参数：【{param=[test-list, {key1=hello-world, key2=11, key3=11.11, key4=[hello, world], key5=[111, 112, 113]}]}】
2020-01-16 21:51:11.941  INFO 27340 --- [           main] wiki.xsx.log.controller.LogTestCallback  : wiki.xsx.log.controller.TestParamLogController.logTest3方法的回调函数执行成功
```

##### @ResultLog注解示例：

1. 日志注解标记：
```java
@RestController
public class TestResultLogController {

    @ResultLog(value = "ResultLog-test1")
    @GetMapping("/resultLogTest1")
    public Map<String, Object> logTest1(HttpServletRequest request, Map<String, Object> param) {
        Map<String, Object> result = new HashMap<>(3);
        result.put("code", 200);
        result.put("msg", "success");
        result.put("data", param);
        return result;
    }

    @ResultLog(value = "ResultLog-test2", callback = LogTestCallback.class)
    @GetMapping("/resultLogTest2")
    public Map<String, Object> logTest2(HttpServletRequest request, List<Object> param) {
        Map<String, Object> result = new HashMap<>(3);
        result.put("code", 200);
        result.put("msg", "success");
        result.put("data", param);
        return result;
    }
}
```
2. 日志回调声明（实现wiki.xsx.core.log.LogCallback接口）：
```java
// 加入IOC容器，否则调用失败
@Component
@Slf4j
public class LogTestCallback implements LogCallback {
    @Override
    public void callback(Annotation annotation, MethodInfo methodInfo, Map<String, Object> paramMap, Object result) {
        log.info(methodInfo.getClassAllName()+"."+methodInfo.getMethodName()+"方法的回调函数执行成功");
    }
}
```
3. 测试：
```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestResultLogControllerTest {

    @Autowired
    private TestResultLogController testResultLogController;

    @Test
    public void test() {
        List<Object> list = new ArrayList<>(2);
        list.add("test-list");
        Map<String, Object> paramMap = new LinkedHashMap<>(5);
        paramMap.put("key1", "hello-world");
        paramMap.put("key2", 11);
        paramMap.put("key3", 11.11D);
        paramMap.put("key4", new String[]{"hello", "world"});
        paramMap.put("key5", new Number[]{111, 112, 113});
        list.add(paramMap);
        this.testResultLogController.logTest1(null, paramMap);
        this.testResultLogController.logTest2(null, list);
    }
}
```
4. 日志打印效果：
```
2020-01-16 22:09:15.873 DEBUG 9280 --- [           main] wiki.xsx.core.log.LogProcessor           : 调用方法：【wiki.xsx.log.controller.TestResultLogController.logTest1(TestResultLogController.java:23)】，业务名称：【ResultLog-test1】，返回结果：【{msg=success, data={key1=hello-world, key2=11, key3=11.11, key4=[hello, world], key5=[111, 112, 113]}, code=200}】
2020-01-16 22:09:15.874 DEBUG 9280 --- [           main] wiki.xsx.core.log.LogProcessor           : 调用方法：【wiki.xsx.log.controller.TestResultLogController.logTest2(TestResultLogController.java:33)】，业务名称：【ResultLog-test2】，返回结果：【{msg=success, data=[test-list, {key1=hello-world, key2=11, key3=11.11, key4=[hello, world], key5=[111, 112, 113]}], code=200}】
2020-01-16 22:09:15.874  INFO 9280 --- [           main] wiki.xsx.log.controller.LogTestCallback  : wiki.xsx.log.controller.TestResultLogController.logTest2方法的回调函数执行成功
```

##### @ThrowingLog注解示例：

1. 日志注解标记：
```java
@RestController
public class TestThrowingLogController {

    @ThrowingLog(value = "ThrowingLog-test1")
    @GetMapping("/throwingLogTest1")
    public Map<String, Object> logTest1(HttpServletRequest request, Map<String, Object> param) {
        Map<String, Object> result = new HashMap<>(3);
        result.put("code", 200);
        result.put("msg", "success");
        result.put("data", 1/0);
        return result;
    }

    @ThrowingLog(value = "ThrowingLog-test2", callback = LogTestCallback.class)
    @GetMapping("/throwingLogTest2")
    public Map<String, Object> logTest2(HttpServletRequest request, Map<String, Object> param) {
        Map<String, Object> result = new HashMap<>(3);
        result.put("code", 200);
        result.put("msg", "success");
        result.put("data", 1/0);
        return result;
    }
}
```
2. 日志回调声明（实现wiki.xsx.core.log.LogCallback接口）：
```java
// 加入IOC容器，否则调用失败
@Component
@Slf4j
public class LogTestCallback implements LogCallback {
    @Override
    public void callback(Annotation annotation, MethodInfo methodInfo, Map<String, Object> paramMap, Object result) {
        log.info(methodInfo.getClassAllName()+"."+methodInfo.getMethodName()+"方法的回调函数执行成功");
    }
}
```
3. 测试：
```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestThrowingLogControllerTest {

    @Autowired
    private TestThrowingLogController testThrowingLogController;

    @Test
    public void test() {
        List<Object> list = new ArrayList<>(2);
        list.add("test-list");
        Map<String, Object> paramMap = new LinkedHashMap<>(5);
        paramMap.put("key1", "hello-world");
        paramMap.put("key2", 11);
        paramMap.put("key3", 11.11D);
        paramMap.put("key4", new String[]{"hello", "world"});
        paramMap.put("key5", new Number[]{111, 112, 113});
        list.add(paramMap);
        try {
            this.testThrowingLogController.logTest1(null, paramMap);
        }catch (Exception e) {

        }
        try {
            this.testThrowingLogController.logTest2(null, paramMap);
        }catch (Exception e) {

        }
    }
}
```
4. 日志打印效果：
```
2020-01-16 22:13:58.226 ERROR 22184 --- [           main] wiki.xsx.core.log.LogProcessor           : 调用方法：【wiki.xsx.log.controller.TestThrowingLogController.logTest1】，业务名称：【ThrowingLog-test1】，异常信息：

java.lang.ArithmeticException: / by zero
...
2020-01-16 22:13:58.227 ERROR 22184 --- [           main] wiki.xsx.core.log.LogProcessor           : 调用方法：【wiki.xsx.log.controller.TestThrowingLogController.logTest2】，业务名称：【ThrowingLog-test2】，异常信息：

java.lang.ArithmeticException: / by zero
...
2020-01-16 22:13:58.228  INFO 22184 --- [           main] wiki.xsx.log.controller.LogTestCallback  : wiki.xsx.log.controller.TestThrowingLogController.logTest2方法的回调函数执行成功
```

##### @Log注解示例：
1. 日志注解标记：
```java
@RestController
public class TestLogController {

    @Log(value = "Log-test1")
    @GetMapping("/logTest1")
    public Map<String, Object> logTest1(HttpServletRequest request, Map<String, Object> param) {
        Map<String, Object> result = new HashMap<>(3);
        result.put("code", 200);
        result.put("msg", "success");
        result.put("data", param);
        return result;
    }

    @Log(value = "Log-test2", paramFilter = {"request"})
    @GetMapping("/logTest2")
    public Map<String, Object> logTest2(HttpServletRequest request, Map<String, Object> param) {
        Map<String, Object> result = new HashMap<>(3);
        result.put("code", 200);
        result.put("msg", "success");
        result.put("data", param);
        return result;
    }

    @Log(value = "Log-test3", paramFilter = {"request"}, callback = LogTestCallback.class)
    @GetMapping("/logTest3")
    public Map<String, Object> logTest3(HttpServletRequest request, List<Object> param) {
        Map<String, Object> result = new HashMap<>(3);
        result.put("code", 200);
        result.put("msg", "success");
        result.put("data", param);
        return result;
    }
}
```
2. 日志回调声明（实现wiki.xsx.core.log.LogCallback接口）：
```java
// 加入IOC容器，否则调用失败
@Component
@Slf4j
public class LogTestCallback implements LogCallback {
    @Override
    public void callback(Annotation annotation, MethodInfo methodInfo, Map<String, Object> paramMap, Object result) {
        log.info(methodInfo.getClassAllName()+"."+methodInfo.getMethodName()+"方法的回调函数执行成功");
    }
}
```
3. 测试：
```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestLogControllerTest {

    @Autowired
    private TestLogController testLogController;

    @Test
    public void test() {
        List<Object> list = new ArrayList<>(2);
        list.add("test-list");
        Map<String, Object> paramMap = new LinkedHashMap<>(5);
        paramMap.put("key1", "hello-world");
        paramMap.put("key2", 11);
        paramMap.put("key3", 11.11D);
        paramMap.put("key4", new String[]{"hello", "world"});
        paramMap.put("key5", new Number[]{111, 112, 113});
        list.add(paramMap);
        this.testLogController.logTest1(null, paramMap);
        this.testLogController.logTest2(null, paramMap);
        this.testLogController.logTest3(null, list);
    }
}
```
4. 日志打印效果：
```
2020-01-16 22:16:25.736 DEBUG 8304 --- [           main] wiki.xsx.core.log.LogProcessor           : 调用方法：【wiki.xsx.log.controller.TestLogController.logTest1(TestLogController.java:23)】，业务名称：【Log-test1】，接收参数：【{request=null, param={key1=hello-world, key2=11, key3=11.11, key4=[hello, world], key5=[111, 112, 113]}}】
2020-01-16 22:16:25.743 DEBUG 8304 --- [           main] wiki.xsx.core.log.LogProcessor           : 调用方法：【wiki.xsx.log.controller.TestLogController.logTest1(TestLogController.java:23)】，业务名称：【Log-test1】，返回结果：【{msg=success, data={key1=hello-world, key2=11, key3=11.11, key4=[hello, world], key5=[111, 112, 113]}, code=200}】
2020-01-16 22:16:25.744 DEBUG 8304 --- [           main] wiki.xsx.core.log.LogProcessor           : 调用方法：【wiki.xsx.log.controller.TestLogController.logTest2(TestLogController.java:33)】，业务名称：【Log-test2】，接收参数：【{param={key1=hello-world, key2=11, key3=11.11, key4=[hello, world], key5=[111, 112, 113]}}】
2020-01-16 22:16:25.744 DEBUG 8304 --- [           main] wiki.xsx.core.log.LogProcessor           : 调用方法：【wiki.xsx.log.controller.TestLogController.logTest2(TestLogController.java:33)】，业务名称：【Log-test2】，返回结果：【{msg=success, data={key1=hello-world, key2=11, key3=11.11, key4=[hello, world], key5=[111, 112, 113]}, code=200}】
2020-01-16 22:16:25.744 DEBUG 8304 --- [           main] wiki.xsx.core.log.LogProcessor           : 调用方法：【wiki.xsx.log.controller.TestLogController.logTest3(TestLogController.java:43)】，业务名称：【Log-test3】，接收参数：【{param=[test-list, {key1=hello-world, key2=11, key3=11.11, key4=[hello, world], key5=[111, 112, 113]}]}】
2020-01-16 22:16:25.744 DEBUG 8304 --- [           main] wiki.xsx.core.log.LogProcessor           : 调用方法：【wiki.xsx.log.controller.TestLogController.logTest3(TestLogController.java:43)】，业务名称：【Log-test3】，返回结果：【{msg=success, data=[test-list, {key1=hello-world, key2=11, key3=11.11, key4=[hello, world], key5=[111, 112, 113]}], code=200}】
2020-01-16 22:16:25.744  INFO 8304 --- [           main] wiki.xsx.log.controller.LogTestCallback  : wiki.xsx.log.controller.TestLogController.logTest3方法的回调函数执行成功
```

### 其他说明

#### 日志类型
1. @ParamLog：参数类型，仅打印参数
2. @ResultLog：结果类型，仅打印结果
3. @ThrowingLog：异常类型，仅打印异常
4. @Log：综合类型，打印参数+结果+异常

#### 日志参数
1. value：业务名称
2. level：日志级别，默认DEBUG
3. position：代码定位，默认DEFAULT
4. paramFilter：参数名过滤，默认{}
5. callback：日志回调，默认VoidLogCallback.class（实现wiki.xsx.core.log.LogCallback接口，且需放入IOC容器中）

#### 日志级别
1. DEBUG(默认): 调试级别
2. INFO: 信息级别
3. WARN: 警告级别
4. ERROR: 错误级别

#### 回调接口说明
```java
public interface LogCallback {

    /**
     * 回调方法
     * @param annotation 当前使用注解
     * @param methodInfo 方法信息
     * @param paramMap 参数字典
     * @param result 方法调用结果
     */
    void callback(
            Annotation annotation,
            MethodInfo methodInfo,
            Map<String, Object> paramMap,
            Object result
    );
}
```

#### 特别说明
1. 日志级别为DEBUG时，默认开启代码定位，方便调试
2. 其他级别默认关闭代码定位，减少不必要的开支，如需要可手动开启(position=Position.ENABLED)

### 性能测试(仅供参考)
#### 电脑配置

|  |  |
| :------: | :------: |
| CPU | AMD Athlon(tm) II X4 640 Processor(3000 Mhz) |
| 内存 | 8.00 GB (1333 MHz) |
| 硬盘 | Apacer A S510S 128GB SATA Disk Device |
| 测试工具 | Apache JMeter 5.1.1 |
| 测试方式 | http请求测试日志打印，循环5次取最后1次 |

#### 测试结果
加入代码定位功能：

| 日志类型 | 并发数 | 单次平均耗时(毫秒) | 吞吐量(请求次数/每秒)
| :------: | :------: | :------: | :------: |
| @ParamLog | 1000 | 136 | 484 |
| @ResultLog | 1000 | 86 | 417 |
| @Log | 1000 | 29 | 425 |

取消代码定位功能：

| 日志类型 | 并发数 | 单次平均耗时(毫秒) | 吞吐量(请求次数/每秒)
| :------: | :------: | :------: | :------: |
| @ParamLog | 1000 | 274 | 491 |
| @ResultLog | 1000 | 66 | 519 |
| @Log | 1000 | 108 | 483 |

1. linux 下改变环境变量：
````
export PATH=/usr/local/mongodb/bin:$PATH
````
//配置完后可以通过echo $PATH查看配置结果。

生效方法：立即生效

有效期限：临时改变，只能在当前的终端窗口中有效，当前窗口关闭后就会恢复原有的path配置

用户局限：仅对当前用户。

2. 双数据源时，从库的sql 需要在表的前边加数据库名称在查询。
3. vim 下： dd  删除当前行； yy 复制当前行；u:撤销当前操
4. jsp EL表达式展示json出错解决: 将表达式用单引号引用，因为json内容里面有双引号，造成读取截断失败，正确的表达式如下：
<input type="hidden" name="huserInfo" id="huserInfo" value='${huserInfo}'>
5. linux 中tar命令：

压缩：tar -czvf  file.tar  filename

解压：

tar -xvf file.tar //解压 tar包
tar -xzvf file.tar.gz //解压tar.gz

6. 涉及到list 多线程异步动态增加删除时，arraylist 不可用，此时可以用concurrent 包里的ConcurrentLinkedDeque，队列.

ConcurrentLinkedQueue 是单向链表结构的无界并发队列。元素操作按照 FIFO (first-in-first-out 先入先出) 的顺序。适合“单生产，多消费”的场景。内存一致性遵循对ConcurrentLinkedQueue的插入操作先行发生于(happen-before)访问或移除操作。

ConcurrentLinkedDeque 是双向链表结构的无界并发队列.

ConcurrentLinkedQueue .toArray(T[] a) 可以转成array数组。 比如：

queue.toArray(new StompSession[0].
注意toArray方法：
List接口的toArray()方法就是直接调用Arrays.copyOf(elementData, size)，将list中的元素对象的引用装在一个新的生成数组中。

List接口的toArray(T[] a)方法会返回指定类型（必须为list元素类型的父类或本身）的数组对象，如果a.length小于list元素个数就直接调用Arrays的copyOf()方法进行拷贝并且返回新数组对象(不是a,是新生成了一个数组对象)，新数组中也是装的list元素对象的引用，否则先调用System.arraycopy()将list元素对象的引用装在a数组中，如果a数组还有剩余的空间，则在a[size]放置一个null，size就是list中元素的个数，这个null值可以使得toArray(T[] a)方法调用者可以判断null后面已经没有list元素了。

基准测试ArrayList的toArray方法：

    //速度排名 1
    @Benchmark
    public Object[] simple() {
        return LIST.toArray();
    }

    //速度排名 2，性能最好，推荐使用
    @Benchmark
    public Character[] zeroCached() {
        return LIST.toArray(EMPTY_CHAR_ARRAY);
    }

    //速度排名 3
    @Benchmark
    public Character[] zero() {
        return LIST.toArray(new Character[0]);
    }

    //速度排名 4
    @Benchmark
    public Character[] sized() {
        return LIST.toArray(new Character[LIST.size()]);
    }
结果：

Benchmark                   Mode  Cnt   Score   Error  Units
ToArrayPerfTest.simple      avgt    5  37.278 ± 0.669  ns/op
ToArrayPerfTest.sized       avgt    5  59.346 ± 0.272  ns/op
ToArrayPerfTest.zero        avgt    5  49.682 ± 1.067  ns/op
ToArrayPerfTest.zeroCached  avgt    5  47.982 ± 1.024  ns/op
7.  SpringBoot启动加载数据CommandLineRunner:

SpringBoot应用程序在启动时，会遍历CommandLineRunner接口的实例并运行他们的run()方法。也可以利用@Order注解或者Order接口来规定所有CommandLineRunner实例的运行顺序。

8. java8: Map.computeIfAbsent(k,(k-v)->v):

computeIfAbsent:存在时返回存在的值，不存在时返回新值

9.  ReentrantReadWriteLock: 读写互斥锁，只有读读共享，有写都会互斥，以此可得出读读共享，写写互斥，读写互斥，写读互斥。 基准测试BenchMark 后发现 lock 与sync性能差不多

10. 循环依赖：使用@Async异步注解导致该Bean在循环依赖时启动报BeanCurrentlyInCreationException异常的根本原因分析:

@Slf4j
@Service
public class B {
@Lazy
@Autowired
private B b;

    public void test() {
        this.b.tran();
    }

    //必须为 public 以便能够被代理
    @Async
    @Transactional
    public void tran() {
        log.info("ddd");
    }
}

如果是 @Transactional 可以不加 @Lazy

如果是 @Async 必须加 @Lazy , 否则 启动 spring boot 报错.. 原理参考 使用@Async异步注解导致该Bean在循环依赖时启动报BeanCurrentlyInCreationException异常的根本原因分析，以及提供解决方案【享

https://blog.csdn.net/f641385712/article/details/92797058



11. 【读阿里巴巴开发手册有感】-1：



比较的时候 java.sql.TimeStamp 的毫秒不会参与比较，为 000, 所以同一个秒内的两个时间， 一个为new Date,一个为new Timestamp， 用 before 和 after 比较时,Date 的总是大于 TimeStamp,所以程序种不要使用TimeStamp

12.  StringBuilder:

1）循环中需要使用StringBuilder来操作字符串拼接工作，不要使用string += str，因为反编译出的字节码文件显示每次循环都会 new 出一个 StringBuilder 对象，然后进行append 操作，最后通过 toString 方法返回 String 对象，造成内存资源浪费，这虽然减少了jvm常量池的压力，但也无疑增加了jvm中新生代的压力(增加了垃圾回收的几率)

2）普通代码中使用加号连接字符串会被jvm优化成使用StringBuilder拼接

13.  局域网拷贝：

scp a.tar root@局域网的ip地址:/usr/local/xgj /urs/local/xgj

进入跳板机，输入l可查看外网地址和内网地址

14.  nginx 所在目录：/var/nginx 或者 /etc/nginx/

日志所在目录：/var/log/nginx/access.log



15. 

## 创建时间插入时更新，更新时间在更新时才更新
ALTER TABLE cdk_wd_sysapi_log_oms
MODIFY COLUMN created_date timestamp NULL DEFAULT CURRENT_TIMESTAMP,
MODIFY COLUMN last_up_date timestamp NULL DEFAULT null ON UPDATE CURRENT_TIMESTAMP;

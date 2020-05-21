# second_kill
该系统是基于Spring Boot、SpringMVC、mybatis实现的商城秒杀系统。该系统的功能有展示秒杀商品至前端、查看可秒杀商品详情、用户登录认证和对商品进行秒杀，秒杀成功的话生成订单，并发送邮件通知用户，如用户超时未支付则失效该订单。

### 高并发条件下，商品的超卖或数据不一致的问题

**解决方案：使用分布式锁**

1. redis分布式锁：

   - 方法；通过对redis的SETNX + EXPIRE联合使用

   - 原因：redis本身就是一个基于内存的、单线程的非关系型数据库

   - 缺点：redis服务器挂掉的时候可能出现死锁，即锁一直存在

   - ``` java
         private StringRedisTemplate redisTemplate;
     
     	ValueOperations valueOperations = redisTemplate.opsForValue();
     	final String key = "key";
     	final String value = "value";
     	Boolean cacheRes = valueOperations.setIfAbsent(key, value);
     ```

   - 

2. redisson分布式锁

   - 方法：基于redisson提供的可重入锁。通过RedissonClient对象多的RLock对象

   - redisson：面向redis的操作框架，基于Redis的驻内存网络数据库

   - 原因：如果负责储存某些分布式锁的某些Redis节点宕机以后，而且这些锁正好处于锁住的状态时，这些锁会出现锁死的状态。为了避免这种情况的发生，Redisson内部提供了一个监控锁的看门狗，它的作用是在Redisson实例被关闭前，不断的延长锁的有效期。默认情况下，看门狗的检查锁的超时时间是30秒钟，也可以通过修改[Config.lockWatchdogTimeout](https://github.com/redisson/redisson/wiki/2.-配置方法#lockwatchdogtimeout监控锁的看门狗超时单位毫秒)来另行指定。

     另外Redisson还通过加锁的方法提供了`leaseTime`的参数来指定加锁的时间。超过这个时间后锁便自动解开了。

     [redisson分布式锁参考链接](https://github.com/redisson/redisson/wiki/8.-分布式锁和同步器)

3. ZooKeeper分布式锁

   - 方法：InterProcessMutex 可重入锁

   - Zookeeper分布式锁，能有效的解决分布式问题，不可重入问题，实现起来较为简单。

     但是，Zookeeper实现的分布式锁其实存在一个缺点，那就是性能并不太高。因为每次在创建锁和释放锁的过程中，都要动态创建、销毁瞬时节点来实现锁功能。ZK中创建和删除节点只能通过Leader服务器来执行，然后Leader服务器还需要将数据同不到所有的Follower机器上。

     所以，在高性能，高并发的场景下，不建议使用Zk的分布式锁。
     [zookeeper分布式锁参考链接](https://blog.csdn.net/crazymakercircle/java/article/details/85956246)

### 高并发条件下订单id重复

使用雪花算法生成分布式唯一id，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞，高效率

### 业务解耦

使用rabbitmq对秒杀成功发送邮件与主业务解耦

### 登录认证

集成Shiro进行登录



其它-持续更新
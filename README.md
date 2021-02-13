# ufire-springcloud-platform
        基于一致性hash算法实现websocket分布式集群的尝试，提供模拟宕机演示解决单点故障demo，实现websocket服务的扩容。
    基于jenkins +github hook+docker-compose 实现自动化持续部署。
### [demo](http://websocket.ufiredong.cn/ufire-websocket-ui/)
### [jenkins](http://jenkins.ufiredong.cn)  user:test password: test
### [转载分布式WebSocket集群解决方案](https://www.cnblogs.com/dk1024/p/14015486.html) 
### 技术栈
    nacos 服务发现与注册
    reids pub sub 
    springboot
    springcloud getway
    consistent hash 一致性哈希算法
    websocket 协议
    rabbitmq
    docker-java api
    nginx
![image](https://github.com/ufiredong/ufire-springcloud-platform/blob/feature/dev/img.png)
### Demo使用方法
    1、指定user发送信息，在输入框中输入 @user1：hello 点击send 即可发送。
    2、宕机模拟 stop当前服务容器即可。
    3、扩容模拟 新增一个websocket容器 由于容器启动需要时间 可能需要几十秒左右，需要重置的用户将在收到通知之后5s内重新连接。
    4、由于ECS服务器内存问题，最多只能支持5台websocket容器同时运行。
    5、代码目前有些地方还尚未进行优化，有时候会出现bug,如果时间长未看到效果，建议stop所有服务，重新从1台websocket容器开始模拟。
![1](https://github.com/ufiredong/ufire-springcloud-platform/blob/feature/dev/1.png)
![2](https://github.com/ufiredong/ufire-springcloud-platform/blob/feature/dev/2.png)
### 为什么websocket的session不能被序列化，不能被共享
        如果我们要搭建一个http服务的集群，我们可以对httpSession进行序列化存到redis中，然后同步到其他服务节点。因为
     http服务是无状态的,即使http 1.1以后有了长连接的概念keep alive时间是短暂的,说明http连接不是持久化的，而我们的
     websocket是tcp持久化连接，这是一个长连接，websocket在成功建立连接后服务端存在2个socket对象，客户端存在1个，
     服务端2个一个负责监听（监听某个端口，将http协议升级为websocket协议）,一个socket负责与客户端的socket建立1对1
     的连接关系 http服务在进行完一次request请求之后除了服务端负责监听的socket还存在,负责通信的2个socket随着request
     请求的结束而被释放。http服务的session并不是维护socket连接的，它只是标识与服务的对应关系。而websocket的session
     是用于维护长连接中socket对应关系的,是持久化的,是真实存在的。因为socket通信必须是1对1的。所以这种情况下session不
     能被共享,结合TCP/IP协议会更好理解一点。
### 统一入口 nginx
        nginx 这里作用于服务代理，由于我的 ufire-springcloud-platform 项目整个都在docker容器网络内,目前只有 getway
     暴露9888端口,这里由nginx代理getway这样的话，docker微服网络内的服务只能由getway去实现负载路由,不易被入侵，将
     来我们直接在getway实现鉴权就可以了。
### 网关 getway
        主要负载http message信息发送和ws建立连接。
    我们新建 ConsistencyChooseRule重写choose 方法传入 HashRingConfig 对象，通过 HashRingConfig的getServer(String userId)
    方法获取userId作为key该路由到节点,HashRingConfig对象里维护着一个HashRingEntity对象。
    具体属性如下:
           public class HashRingEntity {
               // 服务节点hash集合
               private SortedMap<Integer, String> serverMap;
               // 当前在线用户hash集合
               private SortedMap<Integer, String> userMap;
               // 上次服务节点hash集合
               private SortedMap<Integer, String> lastTimeServerMap;
           }
           
        我们需要在getway网关维护一个hash环，当服务节点新增（扩容）或者删除（宕机）及时更新hash环，这里我们通过redis的
     消息订阅去实现，nacos注册中心检测到up或者down事件之后会推送消息到getway，此时getway本地增加虚拟节点缓存更新hash环。
### websocket服务扩容 
        既然实现hash一致性,我们在新增websocket服务容器的时候之后，肯定会影响以后连接的路由映射，
        假设websocket CacheB上线了,该服务器的ip地址刚好被映射到key1和cacheA之间。那么key1对应的用户每次要发消息时都
    跑去CacheB发送消息，结果明显是发送不了消息，因为CacheB没有key1对应的session。
    此时我们有两种解决方案。
    方案A简单，动作大：
        nacos监听到节点UP事件之后，根据现有集群信息，更新哈希环。并且断开所有session连接，让客户端重新连接，此时客户
    端会连接到更新后的哈希环节点，以此避免消息无法送达的情况。
    方案B复杂，动作小：
        我们先看看没有虚拟节点的情况，假设CacheC和CacheA之间上线了服务器CacheB。所有映射在CacheC到CacheB的用户发消息
    时都会去CacheB里面找session发消息。也就是说CacheB一但上线，便会影响到CacheC到CacheB之间的用户发送消息。所以我们
    只需要将CacheA断开CacheC到CacheB的用户所对应的session，让客户端重连。
        我们采取的是方案B，这里我利用了rabbitmq topic routingKey 实现的，具体思路如下：
    我们在新增重启一个websocket服务的时候，自动注册到注册中心之余，自动声明一个queue，reset-queue+ip地址。ip地址是随
    机的，每个queue和routingKey形成对应关系，这样当我们新上线一个websocket服务的时候，就会更新hashRing 网关是消息生
    产者他会通过计算得出哪些user需要重置,会得到一个重置list.这时候我们将需要重新连接的user消息推送到它当前所在的queue
    这样我们作为消费端的websocket服务就会监听到要消费的消息，知道哪些user需要重新连接，然后通知webosocket客户端执行
    ws.close()关闭掉这条连接，因为有重试机制的存在，web客户端会重新连接到它应当路由到的节点。
### 遇到的问题
        一开始,我想利用nacos的上下线功能实现模拟宕机,但是nacos的下线功能，仅仅是把当前服务从websocket微服list中
      remove掉了,nacos的健康机制显示它还是健康，如果下线的服务中sessionPool不为空存在长链接，并不会断掉，服务的下线只
      会影响以后链接的路由动向,为了模拟宕机只能kill掉那个服务的进程，docker提供的docker-java api可以实现容器编排管理
      功能.我们直接stop掉当前服务就相当于服务宕机了,客户端的重连机制会重新连接到可用的服务，解决单点故障的问题，实现
      服务的高可用。
    

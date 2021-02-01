# ufire-springcloud-platform
    学习微服-基于一致性hash算法实现 websocket 分布式集群的尝试。
### [dmeo](http://websocket.ufiredong.cn/ufire-websocket-ui/)
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
### 为什么websocket的session不能被序列化，不能被共享
        如果我们要搭建一个http服务的集群，我们可以对httpSession进行序列化存到redis中，然后同步到其他服务节点。因为
     http服务是无状态的,即使http 1.1以后有了长连接的概念keep alive时间是短暂的,说明http连接不是持久化的，而我们的
     websocket是tcp持久化连接，这是一个长连接，websocket在成功建立连接后服务端和客户端内核中分别存在2个socket对象，
     服务端2个一个负责监听（监听某个端口，将http协议升级为websocket协议）一个socket负责与客户端的socket建立1对1
     的连接关系 http服务在进行完一次request请求之后除了服务端负责监听的socket还存在负责通信的2个socket随着request
     请求的结束而被释放。http服务的session并不是维护socket连接的，而websocket的session是用于维护长连接中socket对
     应关系的是持久化的,是真实存在的。因为socket通信必须是1对1的。所以这种情况下session不能被共享,结合TCP/IP协议会
     更好理解一点。
### 为什么要用consistent hash 一致性哈希算法
        一致性hash在很多地方都被广泛应用，比如redis集群,sharding jdbc和memorycache中都有被应用,网上大部分websocket
     的集群方案很多都是基于mq消息队列广播或者topic模式实现的，这里我们可以参照一致性hash的原理，只要我们能确保每次发
     送信息或者连接上线都能准确的映射路由到指定的服务节点。具体实现原理请查阅资料,不在复述。
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
    serverMap 当前在
### websocket服务单点故障

### 遇到的问题
        一开始,我想利用nacos的上下线功能实现模拟宕机,但是nacos的下线功能，仅仅是把当前服务从websocket微服list中
      remove掉了,nacos的健康机制显示它还是健康，如果下线的服务中sessionPool不为空存在长链接，并不会断掉，服务的下线只
      会影响以后链接的路由动向,为了模拟宕机只能kill掉那个服务的进程，docker提供的docker-java api可以实现容器编排管理
      功能.我们直接stop掉当前服务就相当于服务宕机了,客户端的重连机制会重新连接到可用的服务，解决单点故障的问题，实现
      服务的高可用。
    

# dubbo-zk-upgrade

dubbo的zookeeper注册中心升级迁移方案示例代码

1. `docker-compose.yaml` 文件中有新老版本的zookeeper

2. `provider`和`consumer`分别是基于dubbo的接口提供方和调用方，`provider-api`是两者的通信协议

## 方案一(已验证可行)：

1. 先部署新老两版本的两份zookeeper
2. 然后接口提供方会把接口注册到两个zookeeper中，需要重新服务
3. 接口调用方把zookeeper改成新的地址，重启服务
4. 接口提供方把老的zookeeper配置下掉，重启服务
5. 把部署的老版本的zookeeper服务下掉

代价：所有项目都需要改代码支持新老版本的zookeeper

## 方案二(验证不可行)

因为调用方会将zookeeper注册表缓存到文件中，对于`invoker -> provider`这种单向的调用，zk集群可以关停一次性直接升级。但是后面需要先重启provider让接口重新注册到zk，再重启invoker。否则先重启invoker无法在zk中找到接口。

考虑到公司内有循环依赖的情况，所以应用会同时成为`invoker`和`provider`，这些应用就没法儿启动。

## 方案三

让zk集群里的单个机器，逐个升级。zk集群应该会在不同版本的机器间同步注册信息。这个还需要验证升级的版本存不存在兼容问题。


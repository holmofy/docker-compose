# dubbo-zk-upgrade

dubbo的zookeeper注册中心升级迁移方案示例代码

1. `docker-compose.yaml` 文件中有新老版本的zookeeper

2. `provider`和`consumer`分别是基于dubbo的接口提供方和调用方，`provider-api`是两者的通信协议


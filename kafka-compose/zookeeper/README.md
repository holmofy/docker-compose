Zookeeper

https://zookeeper.apache.org/releases.html

用当前最新版本`apache-zookeeper-3.6.2-bin`

```
tar -xzf apache-zookeeper-3.6.2-bin.tar.gz
```

参考配置文档：https://zookeeper.apache.org/doc/r3.6.2/index.html

配置`conf/zoo.cfg`：

```properties
tickTime=2000
# The number of ticks that the initial 
# synchronization phase can take
initLimit=10
# The number of ticks that can pass between 
# sending a request and getting an acknowledgement
syncLimit=5
# the directory where the snapshot is stored.
# do not use /tmp for storage, /tmp here is just 
# example sakes.
# dataDir=/tmp/zookeeper
dataDir=/run/zookeeper
# the port at which the clients will connect
clientPort=22181
# Set to "false" to disable the AdminServer. 
# By default the AdminServer is enabled.
admin.enableServe=true
# The port the embedded Jetty server listens on. 
# Defaults to 8080.
admin.serverPort=28080
# The URL for listing and issuing commands relative to the root URL. 
# Defaults to "/commands".
admin.commandURL=/commands
```

启动

```sh
bin/zkServer.sh --config ./conf start
```

坑：

1、zookeeper会在`dataDir`目录创建一个`zookeeper_server.pid`文件保存进程id，所以要保证`dataDir`目录存在并有权限

2、`admin.enableServe`默认会启动一个jetty提供管理HTTP服务查看zookeeper当前状态。注意端口有没有被占用，如果启动失败查看`logs`目录下的日志。



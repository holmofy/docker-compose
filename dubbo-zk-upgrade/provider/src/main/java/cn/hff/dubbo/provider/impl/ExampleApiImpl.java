package cn.hff.dubbo.provider.impl;

import cn.hff.dubbo.provider.ExampleApi;
import com.alibaba.dubbo.config.annotation.Service;

@Service
public class ExampleApiImpl implements ExampleApi {
    @Override
    public String helloWorld() {
        return "Hello World";
    }

    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}

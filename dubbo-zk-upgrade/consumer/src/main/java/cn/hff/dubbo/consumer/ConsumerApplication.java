package cn.hff.dubbo.consumer;

import cn.hff.dubbo.provider.ExampleApi;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@DubboComponentScan(basePackageClasses = ConsumerApplication.class)
@SpringBootApplication
public class ConsumerApplication {

    @Reference
    private ExampleApi exampleApi;

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @GetMapping("/helloWorld")
    public String helloWorld() {
        return exampleApi.helloWorld();
    }

    @GetMapping("/hello")
    public String sayHello(String name) {
        return exampleApi.sayHello(name);
    }

}

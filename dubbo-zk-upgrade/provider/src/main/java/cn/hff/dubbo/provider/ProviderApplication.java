package cn.hff.dubbo.provider;

import cn.hff.dubbo.provider.impl.ExampleApiImpl;
import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@DubboComponentScan(basePackageClasses = ExampleApiImpl.class)
public class ProviderApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ProviderApplication.class, args);
        new CountDownLatch(1).await();
    }

}

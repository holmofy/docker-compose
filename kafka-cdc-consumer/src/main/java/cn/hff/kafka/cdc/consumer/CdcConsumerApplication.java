package cn.hff.kafka.cdc.consumer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * https://www.baeldung.com/spring-kafka
 * https://www.baeldung.com/spring-cloud-stream
 */
@Slf4j
@EnableKafka
@EnableScheduling
@SpringBootApplication
public class CdcConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CdcConsumerApplication.class, args);
    }

    private int idGenerator = 20000;

    @Value("${mysql.datasource.url}")
    String mysqlUrl;

    @Value("${mysql.datasource.user}")
    String user;

    @Value("${mysql.datasource.passwd}")
    String passwd;

    @Autowired
    KafkaListenerContainerFactory containerFactory;

    @PostConstruct
    public void init() {
        log.error("init:" + containerFactory.getClass() + ":" + containerFactory);
    }

    @Scheduled(fixedDelay = 1000 * 3)
    public void writeMySql() throws SQLException {
        Connection connection = DriverManager.getConnection(mysqlUrl, user, passwd);
        Statement statement = connection.createStatement();
        String sql = String.format("insert into customers(id, first_name, last_name, email)"
                + " value(%d,'%s','%s','%s')", idGenerator++,
            RandomStringUtils.randomAlphabetic(3),
            RandomStringUtils.randomAlphabetic(3),
            RandomStringUtils.randomAlphabetic(3)
        );
        log.error("execute sql:" + sql);
        statement.execute(sql);
        statement.close();
        connection.close();
    }

    @KafkaListener(topics = "customers", groupId = "1")
    public void process(String msg) {
        log.error("receive msg: " + msg);
    }

}

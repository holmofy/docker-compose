package cn.hff.kafka.cdc.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.support.KafkaUtils;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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

    ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        log.error("init:" + ClassUtils.getCanonicalName(containerFactory) + ":" + containerFactory);
        log.error("init:" + ClassUtils.getCanonicalName(objectMapper) + ":" + objectMapper);
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

    @KafkaListener(topics = "customers", groupId = "consumer-group")
    public void process(List<ConsumerRecord> records) throws IOException {
        for (ConsumerRecord record : records) {
            log.error("receive msg key: " + objectMapper.readTree((byte[]) record.key()));
            log.error("receive msg value: " + objectMapper.readTree((byte[]) record.value()));
        }
    }

    @KafkaListener(topics = "customers", groupId = "customer-group")
    public void singleProcess(ConsumerRecord<Object, Object> record) throws IOException {
        log.error("receive msg key: " + objectMapper.readTree((byte[]) record.key()));
        log.error("receive msg value: " + objectMapper.readTree((byte[]) record.value()));
    }

    /**
     * ConsumerRecords与List<CustomerRecord>的区别：
     * https://docs.spring.io/spring-kafka/reference/html/#listener-group-id
     */
    @KafkaListener(topics = "customers", groupId = "customer-group")
    public void process(ConsumerRecords<Object, Object> records) throws IOException {
        for (ConsumerRecord record : records) {
            log.error("json", record.headers());
            log.error("receive msg key: " + objectMapper.readTree((byte[]) record.key()));
            log.error("receive msg value: " + objectMapper.readTree((byte[]) record.value()));
        }
    }

}

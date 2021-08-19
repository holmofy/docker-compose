create database www;

CREATE TABLE www.nginx_access_log_consumer (
    `id` Int,
    `remote` String,
    `server` String,
    `user` String
) ENGINE = Kafka SETTINGS kafka_broker_list = 'kafka:9092',
kafka_topic_list = 'nginx_access',
kafka_group_name = 'nginx_access.clickhouse',
kafka_format = 'JSONEachRow',
kafka_num_consumers = 1;

CREATE TABLE www.nginx_access (
    `id` Int,
    `access_time` DateTime,
    `remote` String,
    `server` String,
    `user` String
) ENGINE = MergeTree()
ORDER BY
    (id, access_time, server)
PARTITION BY toDate(access_time);

CREATE MATERIALIZED VIEW www.nginx_access_log TO www.nginx_access AS
SELECT
    *,
    _timestamp as access_time
FROM
    www.nginx_access_log_consumer;

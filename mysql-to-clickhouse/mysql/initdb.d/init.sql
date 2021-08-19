create database www;

GRANT REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'replicator' IDENTIFIED BY 'replpass';
GRANT SELECT, RELOAD, SHOW DATABASES, REPLICATION SLAVE, REPLICATION CLIENT  ON *.* TO 'debezium-cdc' IDENTIFIED BY 'debezium-passwd';

CREATE TABLE www.nginx_access (
    `id` int primary key auto_increment,
    `remote` varchar(15),
    `server` varchar(256),
    `user` varchar(256)
) ENGINE = InnoDB DEFAULT CHARSET=utf8;
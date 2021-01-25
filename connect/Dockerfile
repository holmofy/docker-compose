FROM debezium/kafka:1.4

LABEL maintainer="Debezium Community"

EXPOSE 8083
VOLUME ["/kafka/data","/kafka/logs","/kafka/config"]

COPY docker-entrypoint.sh /
COPY --chown=kafka:kafka log4j.properties $KAFKA_HOME/config/log4j.properties
COPY docker-maven-download.sh /usr/local/bin/docker-maven-download

#
# Set up the plugins directory ...
#
ENV KAFKA_CONNECT_PLUGINS_DIR=$KAFKA_HOME/connect \
    EXTERNAL_LIBS_DIR=$KAFKA_HOME/external_libs \
    CONNECT_PLUGIN_PATH=$KAFKA_CONNECT_PLUGINS_DIR \
    MAVEN_DEP_DESTINATION=$KAFKA_HOME/libs \
    CONFLUENT_VERSION=5.5.1 \
    AVRO_VERSION=1.9.2 \
    AVRO_JACKSON_VERSION=2.10.2 \
    APICURIO_VERSION=1.3.2.Final

RUN mkdir "$KAFKA_CONNECT_PLUGINS_DIR" "$EXTERNAL_LIBS_DIR"

#
# The `docker-entrypoint.sh` script will automatically discover the child directories
# within the $KAFKA_CONNECT_PLUGINS_DIR directory (e.g., `/kafka/connect`), and place
# all of the files in those child directories onto the Java classpath.
#
# The general recommendation is to create a separate child directory for each connector
# (e.g., "debezium-connector-mysql"), and to place that connector's JAR files
# and other resource files in that child directory.
#
# However, use a single directory for connectors when those connectors share dependencies.
# This will prevent the classes in the shared dependencies from appearing in multiple JARs
# on the classpath, which results in arcane NoSuchMethodError exceptions.
#
RUN docker-maven-download confluent kafka-connect-avro-converter "$CONFLUENT_VERSION" 9c1846872e6706166b7f2d7ae3922d3e && \
    docker-maven-download confluent kafka-connect-avro-data "$CONFLUENT_VERSION" 08579b13feb7421de5a7774fd16223db && \
    docker-maven-download confluent kafka-avro-serializer "$CONFLUENT_VERSION" 6f3673e5d6028136bf7e206f42aecb59 && \
    docker-maven-download confluent kafka-schema-serializer "$CONFLUENT_VERSION" 0f8db6e28b0f21aa999ec72ca50c0192 && \
    docker-maven-download confluent kafka-schema-registry-client "$CONFLUENT_VERSION" fdf60e3774342726dafddeb33962dfde && \
    docker-maven-download confluent common-config "$CONFLUENT_VERSION" 73d3339e57b1cd0c433daf98f9cb2c88 && \
    docker-maven-download confluent common-utils "$CONFLUENT_VERSION" b837b31f144799698c53c93e3ac82bba && \
    docker-maven-download central com/fasterxml/jackson/core jackson-core "$AVRO_JACKSON_VERSION" 5514a46e38331f8c8262ea63bf36483e && \
    docker-maven-download central com/fasterxml/jackson/core jackson-databind "$AVRO_JACKSON_VERSION" 057751b4e2dd1104be8caad6e9a3e589 && \
    docker-maven-download central com/fasterxml/jackson/core jackson-annotations "$AVRO_JACKSON_VERSION" d9e46501509b1751d64c495ab088a6cf && \
    docker-maven-download central org/apache/avro avro "$AVRO_VERSION" cb70195f70f52b27070f9359b77690bb && \
    docker-maven-download apicurio "$APICURIO_VERSION" a25a604ae3e194bc5deba0040aae2a98

ENV KAFKA_CONNECT_JDBC_DIR=$KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-jdbc \
    KAFKA_CONNECT_ES_DIR=$KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-elasticsearch


###
# Install Debezium
###
ENV DEBEZIUM_VERSION="1.4.0.Final" \
    MAVEN_REPO_CENTRAL="" \
    MAVEN_REPOS_ADDITIONAL="" \
    MAVEN_DEP_DESTINATION=$KAFKA_CONNECT_PLUGINS_DIR \
    MONGODB_MD5=5f6e622c501b9450cb18c6513e4cd314 \
    MYSQL_MD5=f9b7fb89e5b0fcfab61547d8b4364200 \
    POSTGRES_MD5=a57d653d21c01646bc7d2611d84bc268 \
    SQLSERVER_MD5=b08e6a4c4620b6a168729ae3d185abd5 \
    ORACLE_MD5=4c05854befe07b92c83a66e8ad4d43e4 \
    DB2_MD5=23dd3705bef05d415b26144be49d30d7 \
    VITESS_MD5=6c4b93fc4d9088d30dbb59f9f054ff5e \
    SCRIPTING_MD5=826a9c60104d2d58fe983c38b0a4d05b

RUN docker-maven-download debezium mongodb "$DEBEZIUM_VERSION" "$MONGODB_MD5" && \
    docker-maven-download debezium mysql "$DEBEZIUM_VERSION" "$MYSQL_MD5" && \
    docker-maven-download debezium postgres "$DEBEZIUM_VERSION" "$POSTGRES_MD5" && \
    docker-maven-download debezium sqlserver "$DEBEZIUM_VERSION" "$SQLSERVER_MD5" && \
    docker-maven-download debezium-additional incubator oracle "$DEBEZIUM_VERSION" "$ORACLE_MD5" && \
    docker-maven-download debezium-additional db2 db2 "$DEBEZIUM_VERSION" "$DB2_MD5" && \
    docker-maven-download debezium-additional vitess vitess "$DEBEZIUM_VERSION" "$VITESS_MD5" && \
    docker-maven-download debezium-optional scripting "$DEBEZIUM_VERSION" "$SCRIPTING_MD5"

###
# Install Other Plugins
###

ARG KAFKA_JDBC_VERSION=5.3.2
ARG KAFKA_ELASTICSEARCH_VERSION=5.3.2

# Deploy Kafka Connect JDBC
# Deploy Clickhouse JDBC Driver
# https://github.com/confluentinc/kafka-connect-jdbc/issues/557
RUN mkdir $KAFKA_CONNECT_JDBC_DIR && cd $KAFKA_CONNECT_JDBC_DIR && \
	curl -sO https://packages.confluent.io/maven/io/confluent/kafka-connect-jdbc/$KAFKA_JDBC_VERSION/kafka-connect-jdbc-$KAFKA_JDBC_VERSION.jar && \
	curl -sO https://repo1.maven.org/maven2/ru/yandex/clickhouse/clickhouse-jdbc/0.1.52/clickhouse-jdbc-0.1.52-shaded.jar

# Deploy Confluent Elasticsearch sink connector
RUN mkdir $KAFKA_CONNECT_ES_DIR && cd $KAFKA_CONNECT_ES_DIR && \
        curl -sO https://packages.confluent.io/maven/io/confluent/kafka-connect-elasticsearch/$KAFKA_ELASTICSEARCH_VERSION/kafka-connect-elasticsearch-$KAFKA_ELASTICSEARCH_VERSION.jar && \
        curl -sO https://repo1.maven.org/maven2/io/searchbox/jest/6.3.1/jest-6.3.1.jar && \
        curl -sO https://repo1.maven.org/maven2/org/apache/httpcomponents/httpcore-nio/4.4.4/httpcore-nio-4.4.4.jar && \
        curl -sO https://repo1.maven.org/maven2/org/apache/httpcomponents/httpclient/4.5.1/httpclient-4.5.1.jar && \
        curl -sO https://repo1.maven.org/maven2/org/apache/httpcomponents/httpasyncclient/4.1.1/httpasyncclient-4.1.1.jar && \
        curl -sO https://repo1.maven.org/maven2/org/apache/httpcomponents/httpcore/4.4.4/httpcore-4.4.4.jar && \
        curl -sO https://repo1.maven.org/maven2/commons-logging/commons-logging/1.2/commons-logging-1.2.jar && \
        curl -sO https://repo1.maven.org/maven2/commons-codec/commons-codec/1.9/commons-codec-1.9.jar && \
        curl -sO https://repo1.maven.org/maven2/org/apache/httpcomponents/httpcore/4.4.4/httpcore-4.4.4.jar && \
        curl -sO https://repo1.maven.org/maven2/io/searchbox/jest-common/6.3.1/jest-common-6.3.1.jar && \
        curl -sO https://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.6/gson-2.8.6.jar

ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["start"]

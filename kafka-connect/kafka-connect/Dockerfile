FROM debezium/connect-base:1.5

USER root
# refs: https://github.com/debezium/docker-images
COPY ./plugins/ KAFKA_CONNECT_PLUGINS_DIR
COPY ./docker-entrypoint.sh /docker-entrypoint.sh
COPY ./wait-worker.sh /wait-worker.sh
COPY ./init-connector.sh /init-connector.sh

RUN mkdir -p /init-connector.d
VOLUME "/init-connector.d"

ENTRYPOINT [ "/docker-entrypoint.sh" ]
CMD [ "start" ]
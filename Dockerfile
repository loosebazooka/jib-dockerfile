FROM debian
COPY goose.sh /app/
ENTRYPOINT sh /app/goose.sh

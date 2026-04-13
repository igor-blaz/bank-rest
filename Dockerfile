FROM ubuntu:latest
LABEL authors="igor"

ENTRYPOINT ["top", "-b"]
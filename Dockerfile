FROM ubuntu:latest
LABEL authors="seth"

ENTRYPOINT ["top", "-b"]
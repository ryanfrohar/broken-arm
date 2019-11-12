FROM arm32v7/python:latest

USER root

RUN apt-get update
RUN apt-get install -y sqlite3 libsqlite3-dev
RUN mkdir /db
RUN /usr/bin/sqlite3 /db/test.db

LABEL com.circleci.preserve-entrypoint=true

ENTRYPOINT sqlite3

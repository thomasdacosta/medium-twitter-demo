# Twitter API

[![Build Status](https://travis-ci.org/thomasdacosta/twitter-demo.svg?branch=master)](https://travis-ci.org/thomasdacosta/twitter-demo)

API desenvolvida em **Spring Boot** e **Java** para exibição dos usuários com maiores seguidores.

### Tecnologias
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Java 8](https://www.oracle.com/technetwork/pt/java/javase/downloads/jdk8-downloads-2133151.html)
- [twitter4j](http://twitter4j.org/en/index.html)
- [Redis](https://redis.io/)
- [Docker](https://www.docker.com/)
- [Apache Maven](https://maven.apache.org/)
- [Eclipse IDE](https://www.eclipse.org/)

### Consumidor

Esta imagem é dependente do consumidor chamado [twitter-consumer](https://hub.docker.com/repository/docker/thomasdacosta/twitter-consumer). 

Utilizar o **docker-compose** para subida de todos os serviços:

```
version: '3.2'
services:
  db:
    image: redis
    ports:
      - "6379:6379"
    entrypoint: redis-server --appendonly yes --requirepass twitterdemo
    volumes:
      - "db_data:/data"
  consumer:
    image: thomasdacosta/twitter-consumer:latest
    depends_on:
      - db
  api:
    image: thomasdacosta/twitter-api:latest
    ports:
      - "8080:8080"
    depends_on:
      - db
      - consumer
volumes:
    db_data:
```

### Gerar imagem do container

Utilizar o comando abaixo para gerar a imagem do container:

```
docker image build --build-arg JAR_FILE=target/twitter-api.jar --tag thomasdacosta/twitter-api:latest .
```

Enviar imagem para o [Docker Hub](https://hub.docker.com/u/thomasdacosta)

```
docker push thomasdacosta/twitter-api:latest
```

### Persistência de Dados no Redis

Esta imagem utiliza o **Redis** para obter as informações com a lista de usuários com maiores seguidores.

Para executar o **Redis** utilize o comando abaixo:

```
docker run -p 6379:6379 --name redis -d redis redis-server --appendonly yes --requirepass twitter
```

### Executar a Aplicação

Os parâmetros da aplicação, deverão ser configurados na variável de ambiente **JAVA_OPTS**:

```
docker run -e JAVA_OPTS="-Dspring.redis.port=6379 \
-Dspring.redis.host=localhost \
-Dspring.redis.password=twitter \
-Dredis.ssl=false" -p 8080:8080 thomasdacosta/twitter-api
```


---

Thomás da Costa - [https://thomasdacosta.com.br](https://thomasdacosta.com.br)
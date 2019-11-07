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
    environment:
      - "JAVA_OPTS=-Dtwitter4j.debug=true -Dtwitter4j.oauth.consumerKey=**** -Dtwitter4j.oauth.consumerSecret=**** -Dtwitter4j.oauth.accessToken=**** -Dtwitter4j.oauth.accessTokenSecret=**** -Dspring.redis.port=6379 -Dspring.redis.host=db -Dspring.redis.password=twitterdemo -Dredis.ssl=false -DhashTag=#azure,#microsoft"
  api:
    image: thomasdacosta/twitter-api:latest
    ports:
      - "8080:8080"
    depends_on:
      - db
      - consumer
    environment:
      - "JAVA_OPTS=-Dspring.redis.port=6379 -Dspring.redis.host=db -Dspring.redis.password=twitterdemo -Dredis.ssl=false"
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

### Obtendo informações da API

```
curl -X GET \
  http://localhost:8080/users/azure \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Host: localhost:8080' \
  -H 'Postman-Token: af26c992-8625-4524-97c4-6c156ca67269,3f65e6a6-5c59-494c-937f-0db4cde4b47f' \
  -H 'User-Agent: PostmanRuntime/7.19.0' \
  -H 'cache-control: no-cache'
```

Retorno da API com o ranking dos usuários com mais seguidores:


```
[
    {
        "user": "keshavbeniwal2",
        "followers": "52448",
        "ranking": "1",
        "hashtag": "azure"
    },
    {
        "user": "solarwinds",
        "followers": "15579",
        "ranking": "2",
        "hashtag": "azure"
    },
    {
        "user": "netec",
        "followers": "10031",
        "ranking": "3",
        "hashtag": "azure"
    },
    {
        "user": "oliver_hoess",
        "followers": "7645",
        "ranking": "4",
        "hashtag": "azure"
    },
    {
        "user": "dsscreenshot",
        "followers": "7145",
        "ranking": "5",
        "hashtag": "azure"
    }
]
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
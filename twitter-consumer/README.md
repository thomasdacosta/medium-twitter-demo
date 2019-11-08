# Twitter Consumer

[![Build Status](https://travis-ci.org/thomasdacosta/twitter-demo.svg?branch=master)](https://travis-ci.org/thomasdacosta/twitter-demo)

Consumidor de tweets desenvolvido em **Spring Boot** e **Java** para sumarizar os usuários com maior numero de seguidores por hashtag

### Tecnologias
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Java 8](https://www.oracle.com/technetwork/pt/java/javase/downloads/jdk8-downloads-2133151.html)
- [twitter4j](http://twitter4j.org/en/index.html)
- [Redis](https://redis.io/)
- [Docker](https://www.docker.com/)
- [Apache Maven](https://maven.apache.org/)
- [Eclipse IDE](https://www.eclipse.org/)

### Gerar imagem do container

Utilizar o comando abaixo para gerar a imagem do container:

```
docker image build --build-arg JAR_FILE=target/twitter-consumer.jar --tag thomasdacosta/twitter-consumer:latest .
```

Enviar imagem para o [Docker Hub](https://hub.docker.com/u/thomasdacosta)

```
docker push thomasdacosta/twitter-consumer:latest
```

### Gerar imagem do container via Maven

A aplicação possui o **Spotify dockerfile-maven** para geração das imagens. Foi criado um profile especifico para esta estapa:

```
mvn clean install -P docker

```

### Persistência de Dados no Redis

Esta imagem utiliza o **Redis** para persistir a quantidade de seguidores por usuário e hashtag. As informações são gravadas em **hash** no formato **twitter-users_\<hashtag\>**, onde a chave é o **id do usuário no Twitter** e o valor é a **quantidade de seguidores**.

Para executar o **Redis** utilize o comando abaixo:

```
docker run -p 6379:6379 --name redis -d redis redis-server --appendonly yes --requirepass twitter
```

### Executar a Aplicação

A aplicação utiliza a biblioteca **[twitter4j](http://twitter4j.org/en/index.html)** para acessar a API do Twitter. Os parâmetros de acesso a aplicação do Twiiter, deverão ser configurados na variável de ambiente **JAVA_OPTS** conforme exemplo abaixo:

```
docker run -e JAVA_OPTS="-Dtwitter4j.debug=true \
-Dtwitter4j.oauth.consumerKey=****** \
-Dtwitter4j.oauth.consumerSecret=****** \
-Dtwitter4j.oauth.accessToken=****** \
-Dtwitter4j.oauth.accessTokenSecret=****** \
-Dspring.redis.port=6379 \
-Dspring.redis.host=localhost \
-Dspring.redis.password=twitter \
-Dredis.ssl=false \
-DhashTag=#azure,#microsoft" --network="host" thomasdacosta/twitter-consumer
```

### Configurar Hashtags

As **hastags** são configuradas no parâmetro **-DhashTag** separadas por virgula. Neste caso, a aplicação efetua a busca das informações no Twitter e sumariza no Redis.


---

Thomás da Costa - [https://thomasdacosta.com.br](https://thomasdacosta.com.br)

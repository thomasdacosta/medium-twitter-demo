package br.com.thomasdacosta;

import redis.embedded.RedisServer;

import java.io.IOException;


public class EmbededRedisServer {

    private int redisPort;
    
    private RedisServer redisServer;

    public void startRedis() throws IOException {
        redisServer = new RedisServer(redisPort);
        redisServer.stop();
        redisServer.start();
    }
    
    public void stopRedis() {
        redisServer.stop();
    }

	public int getRedisPort() {
		return redisPort;
	}

	public void setRedisPort(int redisPort) {
		this.redisPort = redisPort;
	}
    
}

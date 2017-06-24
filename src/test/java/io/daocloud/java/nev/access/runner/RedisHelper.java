package io.daocloud.java.nev.access.runner;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import io.daocloud.java.nev.access.runner.utils.ConfigProperties;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * Created by yann on 2017/6/23.
 */
public class RedisHelper {

    private static ConfigProperties configProperties = ConfigProperties.configProperties();

    private RedisClient redisClient = RedisClient.create(configProperties.redisUrl());

    @Test
    public void init_redis() {
        StatefulRedisConnection<String, String> statefulRedisConnection = redisClient.connect();
        for (int i = 1; i < 20001; i++) {
            statefulRedisConnection.sync()
                    .rpush(configProperties.redisVinsKey(), "LDCTEST00000" + StringUtils.leftPad(String.valueOf(i), 5, "0"));
        }
    }
}

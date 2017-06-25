package io.daocloud.java.nev.access.runner.service;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.support.ConnectionPoolSupport;
import io.daocloud.java.nev.access.runner.utils.ConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yann on 2017/6/23.
 */

@Slf4j
public class VinPoolService {
    private static VinPoolService vinPoolService;
    private static ConfigProperties configProperties = ConfigProperties.configProperties();
    private RedisClient redisClient;
    private GenericObjectPool<StatefulRedisConnection<String, String>> pool;
    private static Boolean redisEnable = configProperties.redisEnable();
    private static String vinPre = configProperties.vinPre();
    private AtomicInteger currentVin = new AtomicInteger(configProperties.vinBegin());

    private VinPoolService() {
        if (redisEnable) {
            redisClient = RedisClient.create(configProperties.redisUrl());
            pool = ConnectionPoolSupport
                    .createGenericObjectPool(() -> redisClient.connect(), new GenericObjectPoolConfig());

        }
    }

    public synchronized static VinPoolService vinPool() {
        if (vinPoolService == null) {
            vinPoolService = new VinPoolService();
        }

        return vinPoolService;
    }

    public String getEnableVin() {
        if (redisEnable) {
            try (StatefulRedisConnection<String, String> connection = pool.borrowObject()) {
                return connection.sync().lpop(configProperties.redisVinsKey());
            } catch (Exception e) {
                log.error("e:", e);
                throw new RuntimeException();
            }
        } else {
            return vinPre + StringUtils.leftPad(String.valueOf(currentVin.getAndIncrement()), 5, "0");
        }


    }

    public void backVin(String vin) {
        if (redisEnable) {
            try (StatefulRedisConnection<String, String> connection = pool.borrowObject()) {
                connection.sync().rpush(configProperties.redisVinsKey(), vin);
            } catch (Exception e) {
                log.error("e:", e);
                throw new RuntimeException();
            }
        }
    }
}

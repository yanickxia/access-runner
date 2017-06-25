package io.daocloud.java.nev.access.runner.utils;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.EnvironmentConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * Created by yann on 2017/4/19.
 * this is config class, it's singleton
 */

@Log4j2
public class ConfigProperties {
    private CompositeConfiguration config;
    private static ConfigProperties configProperties;
    @Getter
    private static Properties kafkaProperties = new Properties();

    private ConfigProperties() {
        try {

            String configFile = System.getenv("CONFIG_FILE");
            if (Objects.isNull(configFile)) {
                configFile = "config.properties";
            }

            try (InputStream file = getClass().getResourceAsStream("/" + configFile)) {
                kafkaProperties.load(file);
            } catch (IOException e) {
                log.error("kafkaConsumer config error, please check config.properties, ", e);
                Runtime.getRuntime().exit(-1);
            }

            config = new CompositeConfiguration();

            // Compose config file
            Configuration fileConfig = new Configurations().properties(new File(configFile));
            Configuration environmentConfiguration = new EnvironmentConfiguration();

            config.addConfiguration(environmentConfiguration);
            config.addConfiguration(fileConfig);

            log.info("nev access point config is : ");
            config.getKeys().forEachRemaining(key -> {
                log.info("key: [ {} ] , value [ {} ] ", key, config.getString(key));
            });

        } catch (ConfigurationException e) {
            log.error("read properties error, please check config.properties, ", e);
            Runtime.getRuntime().exit(-1);
        }
    }

    public synchronized static ConfigProperties configProperties() {
        if (configProperties != null) {
            return configProperties;
        }
        configProperties = new ConfigProperties();
        return configProperties;
    }

    public Boolean redisEnable() {
        return config.getBoolean("redis.enable");
    }

    public String vinPre() {
        return config.getString("vin.pre");
    }

    public Integer vinBegin() {
        return config.getInt("vin.begin");
    }

    public String redisUrl() {
        return config.getString("redis.url");
    }

    public String redisVinsKey() {
        return config.getString("redis.vin.key");
    }

    public Integer connectPeriod() {
        return config.getInt("connect.period");
    }

    public Integer connectNumber() {
        return config.getInt("connect.number");
    }

    public String connectIp() {
        return config.getString("connect.ip");
    }

    public Integer connectPort() {
        return config.getInt("connect.port");
    }
}

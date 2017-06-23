package io.daocloud.java.nev.access.runner.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by yann on 2017/6/23.
 */
public class ScheduledService {

    private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() / 2);
    public static ScheduledExecutorService scheduledService() {
        return scheduledExecutorService;
    }

}

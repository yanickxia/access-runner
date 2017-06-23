package in.daocloud.java.nev.access.runner;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yann on 2017/6/23.
 */
public class VinPool {
    private static List<String> vinPools = new ArrayList<>();
    private static VinPool vinPool = new VinPool();

    static {
        for (int i = 0; i < 50000; i++) {
            vinPools.add("LDCTEST00000" + StringUtils.leftPad(String.valueOf(i), 5));
        }
    }


    public static VinPool vinPool() {
        return vinPool;
    }

    static void dot(){

    }

    public static String getEnableVin() {
        return vinPools.size() > 0 ? vinPools.get(0) : null;
    }

    public static void back(String vin) {
        vinPools.add(vin);
    }
}

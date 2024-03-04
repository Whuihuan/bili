package top.misec.utils;

import java.util.Random;

import lombok.extern.slf4j.Slf4j;
import top.misec.config.ConfigLoader;

/**
 * 随机延时.
 *
 * @author junzhou
 */
@Slf4j
public class SleepUtils {
    private static int defaultTime = ConfigLoader.helperConfig.getTaskConfig().getTaskIntervalTime();

    public static void randomSleep() {
        if (defaultTime == 0) {
            //兼容云函数旧版本配置
            defaultTime = 10;
        }

        Random random = new Random();
        int sleepTime = (int) ((random.nextDouble() + 0.5) * defaultTime * 1000);
        log.info("-----随机暂停{}ms-----\n", sleepTime);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            log.warn("延时异常", e);
        }
    }
}

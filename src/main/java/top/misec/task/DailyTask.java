package top.misec.task;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import top.misec.api.ApiList;
import top.misec.utils.HttpUtils;
import top.misec.utils.PushUtils;
import top.misec.utils.SleepUtils;
import top.misec.config.ConfigLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static top.misec.task.TaskInfoHolder.STATUS_CODE_STR;
import static top.misec.task.TaskInfoHolder.calculateUpgradeDays;

/**
 * 日常任务 .
 *
 * @author @JunzhouLiu @Kurenai
 * @since 2020/10/11 20:44
 */
@Slf4j
public class DailyTask {

    private final List<Task> dailyTasks;

    public DailyTask() {
        dailyTasks = new ArrayList<>();
        /**
         * 观看分享视频
         */
        dailyTasks.add(new VideoWatch());
        /**
         * 漫画签到
         */
        dailyTasks.add(new MangaSign());
        /**
         * 投币任务
         */
        dailyTasks.add(new CoinAdd());
        /**
         * 银瓜子换硬币
         */
        if (ConfigLoader.helperConfig.getTaskConfig().silver2Coin) {
            dailyTasks.add(new Silver2Coin());
        }
        /**
         * 直播签到（已下线）
         */
//        dailyTasks.add(new LiveChecking());
        /**
         * 直播间送礼
         */
        if(Boolean.TRUE.equals(ConfigLoader.helperConfig.getTaskConfig().getGiveGift())){
            dailyTasks.add(new GiveGift());
        }
        /**
         * 充电任务
         */
        if(Boolean.TRUE.equals(ConfigLoader.helperConfig.getTaskConfig().getMonthEndAutoCharge())) {
            dailyTasks.add(new ChargeMe());
        }
        /**
         * 漫画权益领取
         */
        dailyTasks.add(new GetVipPrivilege());
        /**
         * 赛事预测
         */
        if(Boolean.TRUE.equals(ConfigLoader.helperConfig.getTaskConfig().getMatchGame())){
            dailyTasks.add(new MatchGame());
        }
        /**
         * 漫画阅读
         */
        dailyTasks.add(new MangaRead());
        Collections.shuffle(dailyTasks);
        /**
         * 硬币日志
         */
        dailyTasks.add(0, new CoinLogs());
    }

    /**
     * get daily task status.
     *
     * @return jsonObject 返回status对象
     * @value {"login":true,"watch":true,"coins":50,"share":true,"email":true,"tel":true,"safe_question":true,"identify_card":false}
     * @author @srcrs
     */
    public static JsonObject getDailyTaskStatus() {
        JsonObject jsonObject = HttpUtils.doGet(ApiList.REWARD);
        int responseCode = jsonObject.get(STATUS_CODE_STR).getAsInt();
        if (responseCode == 0) {
            log.info("请求本日任务完成状态成功");
            return jsonObject.get("data").getAsJsonObject();
        } else {
            log.warn(jsonObject.get("message").getAsString());
            return HttpUtils.doGet(ApiList.REWARD).get("data").getAsJsonObject();
            //偶发性请求失败，再请求一次。
        }
    }

    public void doDailyTask() {
        /**
         * 判断Cookies是否有效
         */
        UserCheck userCheck = new UserCheck();
        if(!userCheck.isCookieValid()){
            PushUtils.doPush();
            return;
        }
        try {
            dailyTasks.forEach(task -> {
                log.info("------{}开始------", task.getName());
                try {
                    task.run();
                } catch (Exception e) {
                    log.error("任务[{}]运行失败", task.getName(), e);
                }
                log.info("------{}结束------\n", task.getName());
                SleepUtils.randomSleep();
            });
            log.info("本日任务已全部执行完毕");
            calculateUpgradeDays();
        } catch (Exception e) {
            log.error("任务运行异常", e);
        } finally {
            PushUtils.doPush();
        }
    }
}


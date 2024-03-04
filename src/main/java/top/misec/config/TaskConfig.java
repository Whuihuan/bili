package top.misec.config;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * task config.
 *
 * @author JunzhouLiu
 */
@Data
@Accessors(chain = true)
public class TaskConfig {
    /**
     * 【基础设置】
     * 
     * 任务之间的执行间隔
     */
    public Integer taskIntervalTime;

    /**
     * 【基础设置】
     * 
     * 是否跳过本日任务
     */
    public Boolean skipDailyTask;

    /**
     * 【基础设置】
     * 
     * 操作UA
     */
    public String userAgent;

    /**
     * 【签到】
     * 
     * 手机端漫画签到时的平台
     */
    public String devicePlatform;

    /**
     * 【投币】
     * 
     * 投币优先级
     */
    public Integer coinAddPriority;

    /**
     * 【投币】
     * 
     * 每日投币数量
     */
    public Integer numberOfCoins;

    /**
     * 【投币】
     * 
     * 预留的硬币数
     */
    public Integer reserveCoins;

    /**
     * 【投币】
     * 
     * 投币时是否点赞
     */
    public Integer selectLike;

    /**
     * 【充电】
     * 
     * 年度大会员月底是否用 B 币券自动充电
     */
    public Boolean monthEndAutoCharge;


    /**
     * 【充电】
     * 
     * 给指定 up 主充电
     */
    public String chargeForLove;

    /**
     * 【充电】
     * 
     * 充电日期
     */
    public Integer chargeDay;

    /**
     * 【直播间】
     * 
     * 是否自动给主播送礼
     */
    public Boolean giveGift;

    /**
     * 【直播间】
     * 
     * 送礼主播直播间
     */
    public String upLive;

    /**
     * 【直播间】
     * 
     * 银瓜子兑换硬币
     */
    public Boolean silver2Coin;

    /**
     * 【赛事预测】
     * 
     * 是否开启赛事预测
     */
    public Boolean matchGame;

    /**
     * 【赛事预测】
     * 
     * 是否压赔率高的
     */
    public Boolean showHandModel;

    /**
     * 【赛事预测】
     * 
     * 单次预测的硬币数量
     */
    public Integer predictNumberOfCoins;

    /**
     * 【赛事预测】
     * 
     * 预留的硬币数
     * 低于此数量不执行赛事预测
     */
    public Integer minimumNumberOfCoins;
}
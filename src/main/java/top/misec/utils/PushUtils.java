package top.misec.utils;

import lombok.extern.slf4j.Slf4j;
import top.misec.config.ConfigLoader;
import top.misec.config.Constant;
import top.misec.config.PushConfig;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 消息推送 .
 *
 * @author @JunzhouLiu @Kurenai
 * @since 2020/10/21 17:39
 */

@Slf4j
public class PushUtils {
    private static final Pattern START_PATTERN = Pattern.compile("------([a-zA-Z\\u4e00-\\u9fa5^\\u5f00\\u59cb]+)[\\u5f00\\u59cb]{2}------");

    private static final Pattern END_PATTERN = Pattern.compile("------([a-zA-Z\\u4e00-\\u9fa5^\\u7ed3\\u675f]+)[\\u7ed3\\u675f]{2}------");

    private static final Pattern PAUSE_PATTERN = Pattern.compile("-----[0-9ms\\u968f\\u673a\\u6682\\u505c]+-----");

    public static void doPush() {
        PushConfig.PushInfo pushInfo = ConfigLoader.helperConfig.getPushConfig().getPushInfo();

        if (null == pushInfo) {
            log.info("未配置正确的ftKey和chatId,本次执行将不推送日志");
            return;
        }
        String rawContent = ReadFileUtils.readFile(Constant.LOG_FILE_PATH);
        pushInfo.getTarget().doPush(pushInfo.getMetaInfo(), simplifyContent(rawContent));
    }


    public static String simplifyContent(String rawContent) {
        String content = rawContent;
        content = content.replaceAll("\\n{2,}", "\n");

        Matcher startMatcher = START_PATTERN.matcher(content);
        while (startMatcher.find()) {
            content = content.replace(startMatcher.group(0), "[" + startMatcher.group(1) + "]");
        }
        Matcher endMatcher = END_PATTERN.matcher(content);
        while (endMatcher.find()) {
            content = content.replace(endMatcher.group(0) + "\n", "");
        }
        Matcher pauseMatcher = PAUSE_PATTERN.matcher(content);
        while (pauseMatcher.find()) {
            content = content.replace(pauseMatcher.group(0), "");
        }
        return content;
    }
}

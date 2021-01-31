package cool.zzy.sems.core;

import cool.zzy.sems.context.model.Config;
import cool.zzy.sems.core.db.mapper.ConfigMapper;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/31 14:49
 * @since 1.0
 */
public class GlobalConfig {
    private static volatile Config config;

    public GlobalConfig(ConfigMapper configMapper) {
        config = configMapper.getActiveConfig();
    }

    public static Config getConfig() {
        return config;
    }

    public static void setConfig(Config config) {
        GlobalConfig.config = config;
    }
}
